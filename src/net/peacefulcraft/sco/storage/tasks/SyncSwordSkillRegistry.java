package net.peacefulcraft.sco.storage.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.bukkit.scheduler.BukkitRunnable;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.items.SkillIdentifier;

public class SyncSwordSkillRegistry extends BukkitRunnable{

	@Override
	public void run() {
		Connection con = null;
		
		try {
			
			con = SwordCraftOnline.getHikariPool().getConnection();
			con.setAutoCommit(false);
			PreparedStatement stmt_select = con.prepareStatement("SELECT `id` FROM `swordskills` WHERE `name`=? AND `rarity`=? AND `level`=?");
			HashMap<SkillIdentifier, Boolean> skills = SwordCraftOnline.getSCOConfig().getSwordSkillRegistry().getSwordSkills();
			
			for(SkillIdentifier skill : skills.keySet()) {
				stmt_select.setString(1, skill.getSkillName());
				stmt_select.setString(2, skill.getRarity().toString());
				stmt_select.setInt(3, skill.getSkillLevel());
				ResultSet res = stmt_select.executeQuery();
				
				if(res.next()) {
					skill.setDatabaseId(res.getInt(1));
				} else {
					PreparedStatement stmt_insert = con.prepareStatement(
						"INSERT INTO `swordskills` VALUES(,?,?,?)", 
					Statement.RETURN_GENERATED_KEYS);

					stmt_insert.setString(1, skill.getSkillName());
					stmt_insert.setString(2, skill.getRarity().toString());
					stmt_insert.setInt(3, skill.getSkillLevel());
					stmt_insert.executeUpdate();
					
					res = stmt_insert.getGeneratedKeys();
					if(res.next()) {
						skill.setDatabaseId(res.getInt(1));
					} else {
						SwordCraftOnline.logWarning("Unable to register skill " + skill.getSkillName() + " to global registry");
					}
				}
				
				con.commit();
				
			}

			SwordCraftOnline.logInfo("Sychronized registry of " + skills.size() + " sword skills.");
			
		} catch(SQLException e) {

			e.printStackTrace();

		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
