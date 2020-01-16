package net.peacefulcraft.sco.inventories.listeners;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.SwordSkillInventory;
import net.peacefulcraft.sco.inventory.InventoryBase;
import net.peacefulcraft.sco.inventory.InventoryType;

/**
 * Tracks opening and closing of inventories
 * Controlling item movement in and out of inventories occurs in the InventoryBase children
 * ( SEE net.peacefulcraft.sco.inventories )
 */
public class InventoryActions implements Listener{

	private HashMap<Player, InventoryBase> openInventories;
	
	public InventoryActions() {
		openInventories = new HashMap<Player, InventoryBase>();
	}
	
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
			SwordSkillInventory inv = s.getData().getInventories().getInventory(InventoryType.SWORD_SKILL);
			
			inv.openInventory();
			openInventories.put(p, (InventoryBase) inv);
		}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if(!openInventories.containsKey(e.getPlayer())) { return; }
		
		InventoryBase inv = openInventories.get(e.getPlayer());
		inv.saveInventory();
		
		if(inv instanceof SwordSkillInventory) {
			GameManager.findSCOPlayer((Player) e.getPlayer()).getSwordSkillManager().syncSkillInventory((SwordSkillInventory) inv);
		}
	}
}
