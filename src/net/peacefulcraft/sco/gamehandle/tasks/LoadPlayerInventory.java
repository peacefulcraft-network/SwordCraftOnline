package net.peacefulcraft.sco.gamehandle.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.scheduler.BukkitRunnable;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventory.InventoryType;
import net.peacefulcraft.sco.item.ItemTier;
import net.peacefulcraft.sco.item.SkillIdentifier;

/**
 * Async task that pulls player's inventory data out of mysql
 */
public class LoadPlayerInventory extends BukkitRunnable{

	private SCOPlayer s;
	private String name;
	private String uuid;
	private InventoryType t;
	
	public LoadPlayerInventory(SCOPlayer s, InventoryType t) {
		this.s = s;
		this.t = t;
		this.name = s.getName();
		this.uuid = s.getPlayer().getUniqueId().toString();
	}
	
	@Override
	public void run() {
		try {
			Connection con = SwordCraftOnline.getHikariPool().getConnection();
			PreparedStatement stmt = con.prepareStatement(""
				+ "SELECT `name`,`level`,`rarity` FROM "
				+ "`player_swordskills` LEFT JOIN `swordskills` ON `swordskill`=`id` "
				+ "WHERE `uuid`=? AND inventory=?"
			);
			stmt.setString(1, uuid);
			stmt.setString(2, t.toString());
			ResultSet res = stmt.executeQuery();
			
			// If results, moves cursor forward. Otherwise returns false - user has no inventories
			if(!res.next()) { return; }
			
			ArrayList<SkillIdentifier> identifiers = new ArrayList<SkillIdentifier>();
			do {
				// Remove all spaces from string
				String itemName = res.getString(1).replaceAll("\\s", "");
				int itemLevel = res.getInt(2);
				ItemTier itemRarity = ItemTier.valueOf(res.getString(3));
				
				// Generate the identifier for this swordskill and add to the list
				identifiers.add(new SkillIdentifier(itemName, itemLevel, itemRarity));
				
			}while(res.next());
			
			con.close();
			
			// Get back on main thread to generate the inventory from identifier list
			(new GeneratePlayerInventory(s, t, identifiers)).runTask(SwordCraftOnline.getPluginInstance());
			
		} catch (SQLException e) {
			SwordCraftOnline.logSevere("[TASK][LoadPlayerInventory] Failed for " + name);
			SwordCraftOnline.logSevere(e.getMessage());
			SwordCraftOnline.logSevere("---------------------------------------------------");
		}
		
	}

}