package net.peacefulcraft.sco.inventories.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.CustomGUI;
import net.peacefulcraft.sco.inventories.SwordSkillInventory;
import net.peacefulcraft.sco.items.utilities.BlockedSlot;
import net.peacefulcraft.sco.items.utilities.UnlockSlot;
import net.peacefulcraft.sco.swordskills.skills.SkillBase;

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
		SwordSkillInventory inv = s.getData().getInventories().getInventory(SwordSkillInventory.class);
		if(inv == null) {
			newSkillInv(p); 
		}
		
		inv.openInventory();
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		//Detect if ssInv is open, register the sword skills and apply effects.
		
	}
	
	/**
	 * Handle loading skills here based on items in inventory.
	 * TODO: CHANGE TO ON CLOSE
	 */
	private void SkillInvClose(SCOPlayer p, SwordSkillInventory SSInv) {
		Inventory inv = SSInv.getInventory();
		for(int i = 0; i < 9; i++) {
			SkillBase skill = SkillBase.getSkillByName(inv.getItem(i).getItemMeta().getDisplayName().toLowerCase());
			if(skill != null) {
				
			}
		}
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
			return 2;
		} else if(level > 10 && level <= 20) {
			return 3;
		} else if(level > 20 && level <= 30) {
			return 4;
		} else if(level > 30 && level <= 40) {
			return 5;
		} else if(level > 40 && level <= 50) {
			return 6;
		} else if(level > 50 && level <= 60) {
			return 7;
		} else if(level > 70 && level <= 80) {
			return 8;
		}
		return 0;
	}
}
