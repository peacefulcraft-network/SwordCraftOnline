package net.peacefulcraft.sco.inventories.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.gamehandle.player.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.CustomGUI;
import net.peacefulcraft.sco.inventories.CustomGUI.Row;
import net.peacefulcraft.sco.inventories.CustomGUI.onClick;

public class SwordSkillInventory implements Listener
{
	private CustomGUI SkillInventory = new CustomGUI("Sword Skill Inventory", 4, new onClick() {
		@Override
	    public boolean click(Player p, CustomGUI menu, Row row, int slot, ItemStack item) {
	        if(row.getRow() == 0 || row.getRow() == 2){
	            //TODO: Logic
	        }
	        return true;
	    }
	});
	
	public SwordSkillInventory() {
		for(int i = 0; i<=8; i++) {
			SkillInventory.addButton(SkillInventory.getRow(1), i, new ItemStack(Material.RED_STAINED_GLASS_PANE), "", "");
		}
		
	}
	
	@EventHandler
	public void onRightclick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		
		if(!(p.getInventory().getItemInMainHand().getType() == Material.ENCHANTED_BOOK)) return;
		if(!(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Sword Skill Tome"))) return;
		
		SCOPlayer s = GameManager.findSCOPlayer(p);
		if(s == null) {return;}
		
		menuOpen(p);
	}
	
	@EventHandler
	public void moveEvent(InventoryMoveItemEvent e) {
		ItemStack item = e.getItem();
		
		ItemStack pane = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemMeta meta = pane.getItemMeta();
		meta.setDisplayName("");
		pane.setItemMeta(meta);
		
		if(e.getDestination().contains(pane)) {
			if(!(item.getItemMeta().getLore().contains("Sword Skill"))) { 
				e.setCancelled(true);
				return;
			}
		}
	}
	
	private void menuOpen(Player p) {
		SkillInventory.fillRow(SkillInventory.getRow(0), 8-slotUnlock(p), new ItemStack(Material.BLACK_STAINED_GLASS_PANE), "Locked Tome Page", "Gain more experience to unlock");
		
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
