package net.peacefulcraft.sco.inventories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.items.CustomDataHolder;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;

/**
 * Inventory base class backed by Bukkit Inventory. 
 */
public abstract class BukkitInventoryBase implements SCOInventory {
	protected Inventory inventory;
		public Inventory getInventory() { return this.inventory; }

	/**
	 * Sets the inventories contents to the items in the item list. Does not clear
	 * the inventory first. Respects item indexes in the list.
	 * DOES NOT automatically save when called on a persistent inventory.
	 * Save operators must be triggered manually.
	 */
	public void setInventoryContents(List<ItemIdentifier> items) {
		this.generateItemsFromIdentifiers(items);
	}

	/**
	 * Sets the inventories slot to the provided item.
	 * DOES NOT automatically save when called on a persistent inventory.
	 * Save operators must be triggered manually.
	 */
	public HashMap<Integer, ItemStack> addItem(ItemIdentifier item) {
		return inventory.addItem(ItemIdentifier.generateItem(item));
	}
	
	/**
	 * Removes and reutrns the item at the given slot.
	 * DOES NOT automatically save when called on a persistent inventory.
	 * Save operators must be triggered manually.
	 */
	public ItemStack removeItem(int index) {
		ItemStack item = this.inventory.getItem(index);
		this.inventory.setItem(index, new ItemStack(Material.AIR));
		return item;
	}

	/**
	 * Removes the item from the inventory.
	 * DOES NOT automatically save when called on a persistent inventory.
	 * Save operators must be triggered manually.
	 */
	public void removeItem(ItemStack item) {
		this.inventory.removeItem(item);
	}

	/**
	 * Sets the inventories contents to the items in the item list.
	 * Does not clear the inventory first. Respects item indexes in the list.
	 */
	protected void generateItemsFromIdentifiers(List<ItemIdentifier> items) {
		for (int i = 0; i < items.size(); i++) {
			ItemIdentifier itemIdentifier = items.get(i);
			ItemStack item = null;

			if (itemIdentifier instanceof CustomDataHolder) {
				CustomDataHolder customDataItem = (CustomDataHolder) itemIdentifier;
				item = ItemIdentifier.generateItem(itemIdentifier.getName(), itemIdentifier.getTier(),
						itemIdentifier.getQuantity(), customDataItem.getCustomData());
			} else {
				item = ItemIdentifier.generateItem(itemIdentifier.getName(), itemIdentifier.getTier(),
						itemIdentifier.getQuantity());
			}

			inventory.setItem(i, item);
		}
	}

	/**
   * Generate a List of ItemIdentifiers from the contents of the given inventory
   * @param inventory Inventory from which to generate item identifiers
   * @return A list of ItemIdentifires for the contents of the inventory
   */
  public List<ItemIdentifier> generateItemIdentifiers() {
    ArrayList<ItemIdentifier> items = new ArrayList<ItemIdentifier>();
    for(int i=0; i<inventory.getSize(); i++) {
      if (inventory.getItem(i) == null) {
        items.add(ItemIdentifier.generateIdentifier("Air", ItemTier.COMMON, 1));
      } else {
        ItemStack item = inventory.getItem(i);
        NBTItem nbti = new NBTItem(item);

        try {
          items.add(ItemIdentifier.generateIdentifier(
            nbti.getString("identifier"),
            ItemTier.valueOf(nbti.getString("tier").toUpperCase()),
            item.getAmount())
          );
        } catch(Exception ex) {
          SwordCraftOnline.logWarning("Unable to generate item identifier for item " + item.getItemMeta().getDisplayName() + ". It will not be saved to the inventory.");
          items.add(ItemIdentifier.generateIdentifier("Air", ItemTier.COMMON, 1));
        }

        if (items.get(i) instanceof CustomDataHolder) {
          ((CustomDataHolder) items.get(i)).parseCustomItemData(item);
        }
      }
    }

    return items;
  }
}
