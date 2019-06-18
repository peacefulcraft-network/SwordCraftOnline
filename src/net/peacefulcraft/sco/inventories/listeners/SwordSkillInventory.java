package net.peacefulcraft.sco.inventories.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.gamehandle.player.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.CustomGUI;
import net.peacefulcraft.sco.items.utilities.BlockedSlot;
import net.peacefulcraft.sco.items.utilities.UnlockSlot;

public class SwordSkillInventory implements Listener
{
	private CustomGUI SkillInventory = new CustomGUI("Sword Skill Inventory", 4);
	
	public SwordSkillInventory() {
		for(int i = 0; i<=8; i++) {
			SkillInventory.addButton(SkillInventory.getRow(1), i, (new BlockedSlot()).create(), null, null, null, true);
		}
		
	}
	
	@EventHandler
	public void onRightclick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		
		if(!(p.getInventory().getItemInMainHand().getType() == Material.BOOK)) return;
		if(!(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Sword Skill Tome"))) return;
		
		SCOPlayer s = GameManager.findSCOPlayer(p);
		if(s == null) {return;}
		
		menuOpen(p);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(e.getCurrentItem() != null) {	
			if(e.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE || e.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE ) {
	        	e.setCancelled(true);
	        } else if(e.getCurrentItem().getItemMeta().hasLore()) {
	        	if(!e.getCurrentItem().getItemMeta().getLore().contains("Sword Skill")) {
	        		e.setCancelled(true);
	        	}
	        } 
		}
	}
	
	private void menuOpen(Player p) {
		SkillInventory.emptyRow(SkillInventory.getRow(0));
		SkillInventory.fillRow(SkillInventory.getRow(0), 8-slotUnlock(p), (new UnlockSlot().create()));
		
		SkillInventory.open(p);
	}
	
	private int slotUnlock(Player p) {
		int level = GameManager.findSCOPlayer(p).getLevel();
		if(level >= 0 && level <= 10) {
			return 1;
		} else if(level > 10 && level <= 20) {
			return 2;
		}
		return 0;
	}
}
