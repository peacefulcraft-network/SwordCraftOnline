package net.peacefulcraft.sco.inventories;

import java.util.Arrays;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.items.CustomDataHolder;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;

public abstract class InventoryBase{

	public abstract InventoryType getType();
		
	protected Inventory inventory;
		public Inventory getInventory() { return inventory; }
		
		
	/**
	 * Exists for inventories that can't use the items[]
	 * constructor to load the inventory
	 */
	public InventoryBase(){}

	/**
	 * @param items An array of item identifiers to populate the inventory
	 */
	public InventoryBase(ItemIdentifier[] items) {
		int size = items.length + (items.length % 9);
		this.inventory = SwordCraftOnline.getPluginInstance().getServer().createInventory(null, size);
		this.setInventoryContents(items);
	}

	/**
	 * Sets the contents of this inventory to the given items.
	 * Clears the inventory before populating items.
	 * @param items Items to populate the inventory
	 */
	public void setInventoryContents(ItemIdentifier[] items) {
		inventory.clear();

		for(int i=0; i < this.inventory.getSize(); i++) {
      if (items[i] == null) {
        this.inventory.setItem(i, new ItemStack(Material.AIR));
      } else {
        ItemStack item = null;
        if (items[i] instanceof CustomDataHolder) {
          JsonObject customData = ((CustomDataHolder) items[i]).getCustomData();
          item = ItemIdentifier.generateItem(items[i].getName(), items[i].getTier(), items[i].getQuantity(), customData);
        } else {
          item = ItemIdentifier.generateItem(items[i].getName(), items[i].getTier(), items[i].getQuantity());
        }

        this.inventory.setItem(i, item);
      }
    }
	}

	/**
	 * Default configuration for inventory
	 */
	public abstract void initializeDefaultLoadout();
	
	/**
	 * Create a new inventory of the desired size, keeping || removing items
	 * accordingly
	 */
	public abstract void resizeInventory(int newSize);

	/**
	 * Copies items in oldInventory to newInventory, maintaining empty slots.
	 * Expects oldInventory.size() to be <= newInventory.size()
	 * @param oldInventory Smaller (or equal sized) inventory
	 * @param newInventory Larger (or equal sized) inventory
	 */
	public void copyItemsOneToOne(Inventory oldInventory, Inventory newInventory) {
		for(int i=0; i<oldInventory.getSize(); i++) {
			newInventory.setItem(i, oldInventory.getItem(i));
		}
	}
	
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

		ItemIdentifier identifier = ItemIdentifier.generateIdentifier(identifierName, tier, item.getAmount());
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
}
