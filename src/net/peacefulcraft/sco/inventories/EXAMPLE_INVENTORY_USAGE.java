package net.peacefulcraft.sco.inventories;

import org.bukkit.entity.Player;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

public class EXAMPLE_INVENTORY_USAGE {

	public void main() {
		Player p = null;
		SCOPlayer s = new SCOPlayer(p);
		
		//This will probably autocast everything properly...
		SwordSkillInventory inv = s.getInventoryManager().getInventory(SwordSkillInventory.class);
	
	}
	
}
