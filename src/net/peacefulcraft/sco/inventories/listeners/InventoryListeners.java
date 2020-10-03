package net.peacefulcraft.sco.inventories.listeners;

import java.util.HashMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import net.peacefulcraft.sco.inventories.SCOInventory;

public class InventoryListeners implements Listener {
  
  private HashMap<InventoryView, SCOInventory> activeViews;

  public InventoryListeners() {
    this.activeViews = new HashMap<InventoryView, SCOInventory>();
  }

  /**
   * Close all the currently open inventories for a plugin reload
   */
  public void closeActiveViews() {
    this.activeViews.values().forEach((SCOInventory inventory) -> {
      inventory.closeInventory();
    });;
  }

  /**
   * Register when a player opens their personel Inventory.
   * For any inventory that is not the Player.getInventory() instance,
   * use onInventoryOpen(InventoryView, SCOInventory)
   */
  @EventHandler
  public void onInventoryOpen(InventoryOpenEvent ev) {
    // TODO: Get SCOInventory instance from player's SCOPlayer object
  }

  /**
   * Register when a player opens any SCOInventory
   * @param view The InventoryView instance created when the Inventory was opened.
   * @param inventory The SCOInventory instance for the opened Inventory
   */
  public void onInventoryOpen(InventoryView view, SCOInventory inventory) {
    this.activeViews.put(view, inventory);
  }

  /**
   * Dispatch click events generated inside of SCOInventories to their respective instance.
   * @param ev The InventoryClickEvent that fired.
   */
  @EventHandler
  public void onInventoryClick(InventoryClickEvent ev) {
    SCOInventory inventory = this.activeViews.get(ev.getView());
    if (inventory == null) { return; }

    // figure out how to determine "that" and "that" SCOInventory objects
  }

  /**
   * Dispatch drag events generated inside of SCOInventories to their respective instance.
   * @param ev The InventoryDragEvent that fired.
   */
  @EventHandler
  public void onInventoryDrag(InventoryDragEvent ev) {
    SCOInventory inventory = this.activeViews.get(ev.getView());
    if (inventory == null) { return; }

    // inventory.onInventoryDrag(ev);
  }

  /**
   * Unregister a tracked SCOInventories' InventoryView.
   * @param ev The InventoryCloseEvent that fired
   */
  @EventHandler
  public void onInventoryClose(InventoryCloseEvent ev) {
    this.activeViews.remove(ev.getView());
  }
}
