package net.peacefulcraft.sco.storage.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.commands.Guild;
import net.peacefulcraft.sco.storage.StorageTaskCallbackTask;
import net.peacefulcraft.sco.storage.StorageTaskOutcome;

/**
 * GuildDisbanAsyncTask
 */
public class GuildDisbanAsyncTask extends BukkitRunnable{

  private long guildId;

  private StorageTaskOutcome outcome;
  private StorageTaskCallbackTask callbackTask;

  public GuildDisbanAsyncTask(long guildId, StorageTaskCallbackTask callbackTask) {
    this.guildId = guildId;
    this.callbackTask = callbackTask;
  }

  @Override
  public void run() {
    Connection con = null;
    query: try {
      con = SwordCraftOnline.getHikariPool().getConnection();

      /*
        Get members
      */
      PreparedStatement stmt_select_members = con.prepareStatement("SELECT `uuid` FROM `player_guild` WHERE `id`=?");
      stmt_select_members.setLong(1, guildId);
      ResultSet res = stmt_select_members.executeQuery();

      con.setAutoCommit(false);
      
      /*
        Audit log membership deletion
      */
      PreparedStatement stmt_insert_disban_notification = con.prepareStatement("INSERT INTO `guild_audit` (gid, actor, action) VALUES (?,?,?)");
      while (res.next()) {
        stmt_insert_disban_notification.setLong(1, guildId);
        stmt_insert_disban_notification.setString(2, res.getString(1));
        stmt_insert_disban_notification.setString(3, Guild.GuildAuditAction.DISBAN_KICK.toString());
        stmt_insert_disban_notification.executeQuery();
      }
      stmt_insert_disban_notification.close();
      stmt_select_members.close();

      /*
        Delete memberships
      */
      PreparedStatement stmt_delete_members = con.prepareStatement("DELETE FROM `player_guild` WHERE `id`=?");
      stmt_delete_members.setLong(1, guildId);
      int rowsDeleted = stmt_delete_members.executeUpdate();
      if (rowsDeleted < 1) {
        con.rollback();
        outcome = StorageTaskOutcome.FAILURE;
        break query;
      }
      stmt_delete_members.close();

      /*
        Delete the guild
      */
      PreparedStatement stmt_delete_guild = con.prepareStatement("DELETE FROM `guild` WHERE `id`=?");
      stmt_delete_guild.setLong(1, guildId);
      rowsDeleted = stmt_delete_guild.executeUpdate();
      if (rowsDeleted != 1) {
        con.rollback();
        outcome = StorageTaskOutcome.FAILURE;
        break query;
      }
      stmt_delete_guild.close();
      
      /*
        Commit results and indicate sucess
      */
      con.commit();
      outcome = StorageTaskOutcome.SUCESS;

    } catch(SQLException e) {
			e.printStackTrace();
      outcome = StorageTaskOutcome.FAILURE;

		} finally {
			Bukkit.getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), () -> {
				callbackTask.run(outcome, null);
			});
			
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
  }
}