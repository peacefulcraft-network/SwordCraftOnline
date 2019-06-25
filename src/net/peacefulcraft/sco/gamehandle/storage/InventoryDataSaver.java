package net.peacefulcraft.sco.gamehandle.storage;

import java.util.UUID;

import org.bukkit.inventory.Inventory;

import net.peacefulcraft.sco.inventories.InventoryBase;

public class InventoryDataSaver {
	
	/**
	 * Store inventory data in persistent storage
	 * @param inv: The Inventory
	 * @throws RuntimeException: Inventory fails to save
	 */
	public void saveInventory(Inventory inv) throws RuntimeException {
		//TODO: Save the inventory to persistent storage
		return;
	}
	
	/**
	 * Load inventory data from persistent storage and instantiate an Inventory object
	 * @param
	 */
	public <T extends InventoryBase> T loadInventory(UUID player, Class<T> type) throws RuntimeException{
		//TODO: Load the inventory from persistent storage
		return null;
	}
}
