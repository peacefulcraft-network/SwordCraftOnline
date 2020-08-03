package net.peacefulcraft.sco.inventories;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface PlayerObservableInventory {
  /**
   * Called when the user clicks in this inventory (the inventory that implements this interface).
   * @param InventoryClickEvent e: The InventoryClickEvent that was generated by the player's action
   */
  public abstract void onClickThisInventory(InventoryClickEvent e);

  /**
   * Called when the user in the other inventory open on their screen (if applicable).
   * @param InventoryClickEvent e: The InventoryClickEvent that was generated by the player's action
   */
  public abstract void onClickThatInventory(InventoryClickEvent e);
  
  /**
   * Called when the drags items in this inventory (the inventory that implements this interface).
   * @param InventoryClickEvent e: The InventoryClickEvent that was generated by the player's action
   */
  public abstract void onThisInventoryDrag(InventoryClickEvent e);

  /**
   * Called when the user drags items in the other inventory open on their screen (if applicable).
   * @param InventoryClickEvent e: The InventoryClickEvent that was generated by the player's action
   */
  public abstract void onThatInventoryDrag(InventoryClickEvent e);
}