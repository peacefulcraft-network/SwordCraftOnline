package net.peacefulcraft.sco.inventories.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.CustomGUI;
import net.peacefulcraft.sco.inventories.SwordSkillInventory;
import net.peacefulcraft.sco.items.utilities.BlockedSlot;
import net.peacefulcraft.sco.items.utilities.UnlockSlot;

public class SwordSkillInventoryOpener implements Listener
{
	private CustomGUI SkillInventory = new CustomGUI("Sword Skill Inventory", 4);
	
	public SwordSkillInventoryOpener() {
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
		
		//If player doesnt have inventory open empty inventory
		SwordSkillInventory inv = s.getInventoryManager().getInventory(SwordSkillInventory.class);
		if(inv == null) {
			newSkillInv(p); 
		}
		
		inv.openInventory();
	}
	
	/**
	 * Checks if items can be moved or not
	 * @param e
	 */
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(e.getCurrentItem() != null) {	
			if(e.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE || e.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE ) {
	        	e.setCancelled(true);
	        } else if(e.getCurrentItem().getItemMeta().hasLore()) {
	        	if(!e.getCurrentItem().getItemMeta().getLore().contains("Sword Skill")) {
					System.out.println("Pip");
					e.setCancelled(true);
	        	}
	        } 
		}
	}
	
	private void SkillInvOpen(SCOPlayer p, SwordSkillInventory inv) {
		
	}
	
	/**
	 * 
	 */
	private void newSkillInv(Player p) {
		SkillInventory.emptyRow(SkillInventory.getRow(0));
		SkillInventory.fillRow(SkillInventory.getRow(0), 8-slotUnlock(p), (new UnlockSlot().create()));
		
		SkillInventory.open(p);
	}
	
	/**
	 * Calculates amount of slots unlocked
	 * @param Player p
	 * @return int of lots unlocked
	 */
	private int slotUnlock(Player p) {
		int level = GameManager.findSCOPlayer(p).getLevel();
		if(level >= 0 && level <= 10) {
			return 1;
		} else if(level > 10 && level <= 20) {
			return 2;
		} else if(level > 20 && level <= 30) {
			return 3;
		} else if(level > 30 && level <= 40) {
			return 4;
		} else if(level > 40 && level <= 50) {
			return 5;
		} else if(level > 50 && level <= 60) {
			return 6;
		} else if(level > 70 && level <= 80) {
			return 7;
		}
		return 0;
	}
}
