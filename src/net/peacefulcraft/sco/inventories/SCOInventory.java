package net.peacefulcraft.sco.inventories;

import java.util.List;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.ItemIdentifier;

public interface SCOInventory {
  
  public boolean isInventory(Inventory inventory);

  /**
   * Open the Inventory to the specified player.
   * Notifies the InventoryWatcher of the action.
   * @param s The Player for which to open the Inventory
   */
  public void openInventory(SCOPlayer s);

  /**
   * Close the Inventory if open.
   * The onInventoryClose event will still fire.
   */
  public void closeInventory();

  /**
   * Generate a list of ItemIdifentifiers that represent all the items in the inventory. List indexes
   * are expected to match the item locations in the inventory.
   * @return A list containing the ItemIdeifiers of all items in the inventory, and null values. 
   */
  public List<ItemIdentifier> generateItemIdentifiers();

  /**
   * Called when the user clicks in this inventory (the inventory that implements this interface).
   * @param InventoryClickEvent e: The InventoryClickEvent that was generated by the player's action
   */
  public abstract void onClickThisInventory(InventoryClickEvent ev);

  /**
   * Called when the user in the other inventory open on their screen (if applicable).
   * @param InventoryClickEvent e: The InventoryClickEvent that was generated by the player's action
   */
  public abstract void onClickThatInventory(InventoryClickEvent ev);
  
  /**
   * Called when the drags items in this inventory (the inventory that implements this interface).
   * @param InventoryClickEvent e: The InventoryClickEvent that was generated by the player's action
   */
  public abstract void onThisInventoryDrag(InventoryDragEvent ev);

  /**
   * Called when the user drags items in the other inventory open on their screen (if applicable).
   * @param InventoryClickEvent e: The InventoryClickEvent that was generated by the player's action
   */
  public abstract void onThatInventoryDrag(InventoryDragEvent ev);

  /**
   * Called when this inventories' view instance is closed.
   * @param ev The coresponding InvnetoryCloseEvent
   */
  public abstract void onInventoryClose(InventoryCloseEvent ev);
}
