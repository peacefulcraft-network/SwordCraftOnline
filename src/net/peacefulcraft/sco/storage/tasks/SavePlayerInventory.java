package net.peacefulcraft.sco.storage.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.scheduler.BukkitRunnable;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.InventoryType;
import net.peacefulcraft.sco.items.SkillIdentifier;

public class SavePlayerInventory extends BukkitRunnable{
	
	private SCOPlayer s;
	private UUID uuid;
	private String name;
	private InventoryType t;
	private ArrayList<SkillIdentifier> identifiers;
	
	public SavePlayerInventory(SCOPlayer s, InventoryType t, ArrayList<SkillIdentifier> identifiers) {
		this.s = s;
		this.t = t;
		this.identifiers = identifiers;
		this.uuid = s.getUUID();
		this.name = s.getName();
	}
	
	@Override
	public void run() {
		Connection con = null;
		try {
			con = SwordCraftOnline.getHikariPool().getConnection();
			con.setAutoCommit(false);
			
			PreparedStatement stmt_delete = con.prepareStatement("DELETE FROM `player_swordskills` WHERE `uuid`=? AND `inventory`=?");
			stmt_delete.setString(1, uuid.toString());
			stmt_delete.setString(2, t.toString());
			int numrows = stmt_delete.executeUpdate();
			
			PreparedStatement stmt_insert = con.prepareStatement(
				"INSERT INTO `player_swordskills` VALUES(?,?,?,?)"
			);
			
			for(SkillIdentifier identifier : identifiers) {
				int swordSkillId = SwordCraftOnline.getSCOConfig().getSwordSkillRegistry().getGlobalIdentifier(identifier).getDatabaseId();;
				
				stmt_insert.setString(1, uuid.toString());
				stmt_insert.setInt(2, swordSkillId);
				stmt_insert.setString(3, t.toString());
				stmt_insert.setInt(4, identifier.getInventoryLocation());
				numrows = stmt_insert.executeUpdate();
				
			}
			
			con.commit();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(con != null) { 
				try {
					con.rollback();
					con.setAutoCommit(true);
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
