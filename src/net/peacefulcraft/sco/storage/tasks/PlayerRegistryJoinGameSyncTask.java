package net.peacefulcraft.sco.storage.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import net.peacefulcraft.sco.SwordCraftOnline;

/**
 * 
 * Registers a player in the global server registry and updates
 * their username / last login
 * 
 */
public class PlayerRegistryJoinGameSyncTask {
	
	private UUID uuid;
	private String name;
	
	public PlayerRegistryJoinGameSyncTask(UUID uuid, String name) {
		this.uuid = uuid;
		this.name = name;
	}
	
	public void run() {
		Connection con = null;
		try {
			con = SwordCraftOnline.getHikariPool().getConnection();
			
			PreparedStatement stmt_insert = con.prepareStatement(
				"INSERT INTO `player` (uuid, name) VALUES(?, ?)"
				+ "ON DUPLICATE KEY UPDATE name=VALUES(name), last_login=NOW()"
			);
			
			stmt_insert.setString(1, uuid.toString().replaceAll("-", ""));
			stmt_insert.setString(2, name);
			stmt_insert.executeUpdate();
			
			stmt_insert.close();
			
		} catch(SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

}
