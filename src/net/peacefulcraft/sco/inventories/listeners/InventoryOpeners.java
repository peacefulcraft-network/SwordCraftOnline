package net.peacefulcraft.sco.inventories.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.SwordSkillInventory;
import net.peacefulcraft.sco.inventory.InventoryType;

public class InventoryOpeners implements Listener{

	@EventHandler
	public void onRightclick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		ItemStack mainItem = p.getInventory().getItemInMainHand();
		
		if(
			mainItem.getType() == Material.BOOK && 
			mainItem.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Sword Skill Tome")
		) {
			swordSkillListener(p, e);
		}
		//else if.....
		
	}
	
		private void swordSkillListener(Player p, PlayerInteractEvent e) {
			
			SCOPlayer s = GameManager.findSCOPlayer(p);
			if(s == null) {return;}
			
			/*
			 * Open the player's sword skill inventory
			 * Creates one if it does not already exist
			 */
			SwordSkillInventory inv = s.getData().getInventories().getInventory(InventoryType.ACTIVE_SKILL);
			
			inv.openInventory();
		}
	
}
