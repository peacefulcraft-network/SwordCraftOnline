package net.peacefulcraft.sco.storage.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.scheduler.BukkitRunnable;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventory.InventoryType;
import net.peacefulcraft.sco.item.SkillIdentifier;

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
			PreparedStatement stmt_select = con.prepareStatement(
				"SELECT `id` FROM `swordskills` WHERE `name`=? AND `rarity`=? AND `level`=?"
			);
			PreparedStatement stmt_insert = con.prepareStatement(
				"INSERT INTO `player_swordskill` ON DUPLICATE KEY UPDATE VALUES("
				+ "`uuid`=?, `swordskill`=?, `inventory`=?,`slot`=?"
				+ ")"
			);
			
			for(SkillIdentifier identifier : identifiers) {
				stmt_select.setString(1, identifier.getSkillName());
				stmt_select.setString(2, identifier.getRarity().toString());
				stmt_select.setInt(3, identifier.getSkillLevel());
				ResultSet res = stmt_select.executeQuery();
				
				// If results, moves cursor forward. Otherwise errors because the sword skill was not found.
				if(!res.next()) { 
					SwordCraftOnline.logSevere("[TASK][SavePlayerInventory] Generated error for " + name);
					SwordCraftOnline.logSevere("Sword Skill " + identifier.getSkillName() + " was not found.");
					continue;
				}
				
				int swordSkillId = res.getInt(1);
				
				stmt_insert.setString(1, uuid.toString());
				stmt_insert.setInt(2, swordSkillId);
				stmt_insert.setString(3, t.toString());
				stmt_insert.setInt(4, identifier.getInventoryLocation());
				res = stmt_insert.executeQuery();
				
				if(!res.rowUpdated() && !res.rowInserted()) {
					SwordCraftOnline.logSevere("[TASK][SavePlayerInventory] Generated error for " + name);
					SwordCraftOnline.logSevere("Sword Skill " + identifier.getSkillName() + " failed to save.");
				}
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(con != null) { 
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
