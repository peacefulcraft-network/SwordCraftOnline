package net.peacefulcraft.sco.inventories;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.peacefulcraft.sco.storage.StorageTaskCallbackTask;

public abstract class InventoryBase{
	
	private long inventoryId;
		public long getInventoryId() { return inventoryId; }
		public void setInventoryId(long inventoryId) { this.inventoryId = inventoryId; }

	public abstract InventoryType getType();
		
	protected Inventory inventory;
		public Inventory getInventory() { return inventory; }
		
		
	public InventoryBase(){
		this.initializeDefaultLoadout();
	}

	/**
	 * @param long inventoryId: Database ID of the inventory that is to be loaded.
	 * @param boolean async: Whether the task should be dispatched to an async worker thread or block the current thread.
	 */
	public InventoryBase(long inventoryId, boolean async) {
	}

	/**
	 * @param long inventoryId: Database ID of the inventory that is to be loaded.
	 * @param boolean async: Whether the task should be dispatched to an async worker thread or block the current thread.
	 * @param StorageTaskCallbackTask callback: A task to execute after the inventory fetch task. Runs on success or failure.
	 * @param boolean sychrnoizeCallback: Should the callback be scheduled onto the main server thread, or run on the same thread as the fetch job
	 */
	public InventoryBase(long inventoryId, boolean async, StorageTaskCallbackTask callback, boolean sychrnoizeCallback) {

	}
	
	/**
	 * Default configuration for inventory
	 */
	public abstract void initializeDefaultLoadout();
	
	/**
	 * Create a new inventory of the desired size, keeping || removing items
	 * accordingly
	 */
	public abstract void resizeInventory(int size);
	
	/**
	 * Save the inventory
	 */
	public abstract void saveInventory();
	
	public void fillRow(int row, int amount, ItemStack item) {
		for(int i = 8-amount; i<=8; i++) {
			inventory.setItem( row * 9 + i, item);
		}
	}
    
	public void emptyRow(int row) {
		fillRow(row, 1, new ItemStack(Material.AIR));
	}
	
	public void addButton(int row, int col, ItemStack item) {
		inventory.setItem(row * 9 + col, item);
	}
	
	public void addButton(int row, int col, ItemStack item, String name, String lore, Boolean hidden) {
		inventory.setItem(row * 9 + col, createButtomItem(item, name, lore, hidden)); 
	}
	
	private ItemStack createButtomItem(ItemStack item, String name, String lore, Boolean hidden) {
			ItemMeta im = item.getItemMeta();
			im.setDisplayName(name);
			im.setLore(Arrays.asList(lore));
			if(hidden == true) {
				im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			}
			item.setItemMeta(im);
			return item;
	}
	
}
