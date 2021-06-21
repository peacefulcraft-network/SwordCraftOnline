package net.peacefulcraft.sco.storage.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Supplier;

import net.peacefulcraft.sco.SwordCraftOnline;

/**
 * 
 * Registers a player in the global server registry and updates their username /
 * last login
 * 
 */
public class PlayerRegistryJoinGameTask {

	private UUID uuid;
	private String name;
	private long playerRegistryId;
	private List<String> playerCompletedQuests;

	public long getPlayerRegistryId() {
		return playerRegistryId;
	}

	public List<String> getPlayerCompletedQuests() {
		return this.playerCompletedQuests;
	}

	public PlayerRegistryJoinGameTask(UUID uuid, String name) {
		this.uuid = uuid;
		this.name = name;
	}

	public CompletableFuture<Long> fetchPlayerRegistryId() {
		Supplier<Long> supplier = () -> {
			try (
				Connection con = SwordCraftOnline.getHikariPool().getConnection();
			) {
				
				con.setAutoCommit(false);

				PreparedStatement stmt_select = con.prepareStatement(
					"SELECT `id` FROM `player` WHERE `uuid`=?"
				);
				stmt_select.setString(1, this.uuid.toString().replaceAll("-", ""));
				ResultSet res = stmt_select.executeQuery();
				
				if (res.next()) {
					this.playerRegistryId = res.getLong("id");
					PreparedStatement stmt_update = con.prepareStatement(
						"UPDATE `player` SET last_login=current_timestamp() WHERE id=?"
					);
					stmt_update.setLong(1, this.playerRegistryId);
					stmt_update.executeUpdate();

					SwordCraftOnline.logDebug("Updated player registery for user " + this.uuid.toString() + " | " + this.playerRegistryId);

					// Querying player completed quest data
					PreparedStatement stmt_selectt = con.prepareStatement(
						"SELECT `completed_quests` FROM `player` WHERE `uuid`=?"
					);
					stmt_selectt.setString(1, this.uuid.toString().replaceAll("-",""));
					res = stmt_selectt.executeQuery();

					if (res.next()) {
						String rawCompleted = res.getString("completed_quests");
						if (rawCompleted == null || rawCompleted.isEmpty()) {
							SwordCraftOnline.logDebug("Player(" + this.uuid + ") empty Completed_Quest data.");
						} else {
							this.playerCompletedQuests = Arrays.asList(rawCompleted.split("$$"));
							SwordCraftOnline.logDebug("Player(" + this.uuid + ") compiled Completed_Quest data");
						}
					}

				} else {
					PreparedStatement stmt_insert = con.prepareStatement(
						"INSERT INTO `player` (uuid, name) VALUES(?,?)",
						Statement.RETURN_GENERATED_KEYS
					);
					stmt_insert.setString(1, this.uuid.toString().replaceAll("-", ""));
					stmt_insert.setString(2, this.name);
					if (stmt_insert.executeUpdate() != 1) {
						throw new CompletionException(new Exception("Player(" + this.uuid + ") insert into registry failed"));
					}

					res = stmt_insert.getGeneratedKeys();
					if (!res.next()) {
						throw new CompletionException(new Exception("Player(" + this.uuid + ") insert into registry did not return a generated key"));
					}
					this.playerRegistryId = res.getLong(1);

					SwordCraftOnline.logDebug("Created new player registry entry for user " + this.uuid.toString() + " | " + this.playerRegistryId);
				}
				
				con.commit();

				return this.playerRegistryId;

			} catch(SQLException ex) {
				ex.printStackTrace();
				throw new CompletionException(ex);
			}
		};

		return CompletableFuture.supplyAsync(supplier);
	}
}
