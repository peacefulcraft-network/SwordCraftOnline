package net.peacefulcraft.sco.inventories;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import net.peacefulcraft.sco.items.CustomDataHolder;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;

public abstract class InventoryBase{

	public abstract InventoryType getType();
		
	protected Inventory inventory;
		public Inventory getInventory() { return inventory; }
		
		
	public InventoryBase(){
		this.initializeDefaultLoadout();
	}

	/**
	 * @param items An array of item identifiers to populate the inventory
	 */
	public InventoryBase(ItemIdentifier[] items) {

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

	/**
	 * Get an item in the provided inventory slot and return it's item identifier
	 * @param slot The inventory slot to get the item from
	 * @return The item's item identifier, or null if slot is empty.
	 * 				 If the ItemIdentifier is a CustomDataHolder, the custom item data will 
	 * 				 be parsed into the ItemIdentifier
	 */
	public ItemIdentifier getItem(int slot) {
		ItemStack item = inventory.getItem(slot);
		if (item.getType() == Material.AIR) {
			return null;
		}

		NBTItem nbti = new NBTItem(item);
		String identifierName = nbti.getString("identifier");
		ItemTier tier = ItemTier.valueOf(nbti.getString("tier"));

		ItemIdentifier identifier = ItemIdentifier.generateIdentifier(identifierName, tier);
		if (identifier instanceof CustomDataHolder) {
			((CustomDataHolder) identifier).parseCustomItemData(item);
		}

		return identifier;
	}

	/**
	 * Returns an array of equal size to the inventory with each item's ItemIdentifier
	 * in the coresponding slot. Empty slots will have null values. Uses InventoryBase.getItem();
	 * @return List of mapped ItemIdentifiers from the inventories' contents
	 */
	public ItemIdentifier[] generateItemIdentifiers() {
		ItemIdentifier[] identifiers = new ItemIdentifier[inventory.getSize()];
		for(int i=0; i<identifiers.length; i++) {
			identifiers[i] = getItem(i);
		}

		return identifiers;
	}

	/**
	 * @return Array of item quanities for each slot of the inventory
	 */
	public int[] getInventoryQuantities() {
		int[] quanities = new int[inventory.getSize()];
		for(int i=0; i<quanities.length; i++) {
			quanities[i] = inventory.getItem(i).getAmount();
		}

		return quanities;
	}
}
