package net.peacefulcraft.sco.storage.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.commands.Guild;
import net.peacefulcraft.sco.storage.StorageTaskCallbackTask;
import net.peacefulcraft.sco.storage.StorageTaskOutcome;

public class GuildCreateAsyncTask extends BukkitRunnable{
	
	private long guildId;
	private String guildName;
	private String guildDescription;
	private String creatorUUID;
	
	private StorageTaskOutcome outcome;
	private StorageTaskCallbackTask callbackTask;
	ArrayList<HashMap<String, Object>> generatedKeys;
	
	public GuildCreateAsyncTask(String name, String description, UUID creatorUUID, StorageTaskCallbackTask callbackTask) {
		this.guildName = name;
		this.guildDescription = description;
		this.creatorUUID = creatorUUID.toString().replaceAll("-", "");
		this.callbackTask = callbackTask;
	}
	
	@Override
	public void run() {
		Connection con = null;
		query: try {
			con = SwordCraftOnline.getHikariPool().getConnection();
			con.setAutoCommit(false);

			/*
			 * Create the Guild
			 */
			PreparedStatement stmt_insert = con.prepareStatement(
				"INSERT INTO `guild` (name, description) VALUES(?,?)",
				Statement.RETURN_GENERATED_KEYS
			);
			stmt_insert.setString(1, guildName);
			stmt_insert.setString(2, guildDescription);
			stmt_insert.executeUpdate();
			stmt_insert.close();
			
			ResultSet res = stmt_insert.getGeneratedKeys();
			if(res.next()) {
				guildId = stmt_insert.getGeneratedKeys().getLong(1);
			} else {
				outcome = StorageTaskOutcome.DUPLICATE_KEY_FAILURE;
				break query;
			}
			
			/*
			 * Join the creator to the guild with Ownership rank
			 */
			stmt_insert = con.prepareStatement(
				"INSERT INTO `player_guild VALUES(?,?,?,?)"
			);
			stmt_insert.setLong(1, guildId);
			stmt_insert.setString(2, creatorUUID);
			stmt_insert.setString(3, "Commander"); // Set default guild founder title to Commander
			stmt_insert.setString(4, Guild.GuildRank.OWNER.toString());
			stmt_insert.executeUpdate();
			stmt_insert.close();

			/*
			 * Audit log for Guild creation and player join
			 */
			stmt_insert = con.prepareStatement(
				"INSERT INTO `guild_audit` (gid, actor, action, data) VALUES(?,?,?,?)"
			);
			stmt_insert.setLong(1, guildId);
			stmt_insert.setString(2, creatorUUID);
			stmt_insert.setString(3, Guild.GuildAuditAction.CREATE_GUILD.toString());
			stmt_insert.setString(4, null);
			stmt_insert.executeUpdate();

			stmt_insert.setString(3, Guild.GuildAuditAction.JOIN.toString());
			stmt_insert.executeUpdate();
			
			stmt_insert.setString(3, Guild.GuildAuditAction.PROMOTE.toString());
			stmt_insert.setString(4, Guild.GuildRank.OWNER.toString());
			
			stmt_insert.close();
			con.commit();
			
			outcome = StorageTaskOutcome.SUCESS;
			generatedKeys = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> guildId = new HashMap<String, Object>();
			guildId.put("guildId", guildId);
			generatedKeys.add(guildId);
		} catch(SQLException e) {
			e.printStackTrace();
			outcome = StorageTaskOutcome.FAILURE;
		} finally {
			Bukkit.getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), () -> {
				callbackTask.run(outcome, generatedKeys);
			});
			
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
