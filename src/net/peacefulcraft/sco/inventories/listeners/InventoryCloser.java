package net.peacefulcraft.sco.inventories.listeners;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import net.peacefulcraft.sco.inventories.InventoryBase;

/**
 * Tracks inventories registered to it and saves them when
 * to disk when they are closed.
 */
public class InventoryCloser implements Listener{
	
	/*
	 * Track open inventories so we can save them on close
	 */
	private static HashMap<UUID, InventoryBase> openInventories;
	
	public InventoryCloser() {
		openInventories = new HashMap<UUID, InventoryBase>();
	}
	
	/**
	 * Register an open inventory to monitor.
	 * When the inventory is closed, it will be saved to the disk.
	 * @param uuid: UUID of player who is accessing inventory
	 * @param inventory: Inventory to track
	 */
	public static void trackInventory(UUID uuid, InventoryBase inventory) {
		openInventories.put(uuid, inventory);
	}
	
	/**
	 * Detect players closing inventories.
	 * If the player was looking at an SCO inventory, save it.
	 * @param e
	 */
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if(openInventories.containsKey(p.getUniqueId())) {
			try {
				openInventories.get(p.getUniqueId()).saveInventory();
				openInventories.remove(p.getUniqueId());
			} catch (FileNotFoundException ex) {
				System.out.println("Error saving player inventory " + p.getName());
			}
		}
	}
}
