package net.peacefulcraft.sco.inventories;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class InventoryBase{
	
	private Player observer;
		public Player getObserver() { return observer; }
	
	private InventoryType type;
		public InventoryType getType() { return type; }
		
	protected Inventory inventory;
		public Inventory getInventory() { return inventory; }
		
		
	public InventoryBase(Player observer, InventoryType inventoryType){
		this.observer = observer;
		this.type = inventoryType;	
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
			
	/**
	 * Open inventory for player
	 */
	public void openInventory() {
		observer.openInventory(inventory);
	}
	
	/**
	 * close inventory for player
	 */
	public void closeInventory() {
		observer.closeInventory();
		this.saveInventory();
	};
	
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
