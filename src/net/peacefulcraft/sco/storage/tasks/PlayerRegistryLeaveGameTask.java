package net.peacefulcraft.sco.storage.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Supplier;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

public class PlayerRegistryLeaveGameTask {
    
    private UUID uuid;
    private String name;
    private SCOPlayer s;

    private long playerRegistryid;

    public PlayerRegistryLeaveGameTask(SCOPlayer s) {
        this.s = s;
        this.uuid = s.getUUID();
        this.name = s.getName();
    }

    public CompletableFuture<Long> writePlayerData() {
        Supplier<Long> supplier = () -> {
            try (
                Connection con = SwordCraftOnline.getHikariPool().getConnection();
            ) {

                con.setAutoCommit(false);

                PreparedStatement stmt_select = con.prepareStatement(
                    "SELECT `id` FROM `player` WHERE `uuid`=?"
                );
                stmt_select.setString(1, this.uuid.toString().replace("-", ""));
                ResultSet res = stmt_select.executeQuery();

                if (res.next()) {
                    this.playerRegistryid = res.getLong("id");    

                    List<String> completedQuests = s.getQuestBookManager().getCompletedQuests();
                    if (completedQuests != null || !completedQuests.isEmpty()) {
                        PreparedStatement stmt_update = con.prepareStatement(
                            "UPDATE `player` SET completed_quests=? WHERE id=?"
                        );
                        stmt_update.setString(1, String.join("$$", completedQuests));
                        stmt_update.setLong(2, this.playerRegistryid);
                        stmt_update.executeUpdate();

                        SwordCraftOnline.logDebug("Updated player registry for user " + this.uuid.toString() + " | " + this.playerRegistryid);
                    } else {
                        SwordCraftOnline.logDebug("Failed to update completed_quest data on player(" + this.uuid.toString() + ")");
                    }
                }

                con.commit();

                return this.playerRegistryid;

            } catch(SQLException ex) {
                ex.printStackTrace();
                throw new CompletionException(ex);
            }
        };

        return CompletableFuture.supplyAsync(supplier);
    }
}
