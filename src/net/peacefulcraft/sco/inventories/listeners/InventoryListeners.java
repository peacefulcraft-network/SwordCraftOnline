package net.peacefulcraft.sco.inventories.listeners;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.InventoryView;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
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
  @EventHandler(priority = EventPriority.MONITOR)
  public void onInventoryClick(InventoryClickEvent ev) {
    SCOPlayer s = GameManager.findSCOPlayer((Player) ev.getView().getPlayer());
    registerPlayerInventoryView(s, ev.getView());

    SCOInventory inventory = this.activeViews.get(ev.getView());
    if (inventory == null) { return; }

    // figure out how to determine "that" and "that" SCOInventory objects
  }

  /**
   * Dispatch drag events generated inside of SCOInventories to their respective instance.
   * @param ev The InventoryDragEvent that fired.
   */
  @EventHandler(priority = EventPriority.MONITOR)
  public void onInventoryDrag(InventoryDragEvent ev) {
    SCOPlayer s = GameManager.findSCOPlayer((Player) ev.getView().getPlayer());
    registerPlayerInventoryView(s, ev.getView());

    SCOInventory inventory = this.activeViews.get(ev.getView());
    if (inventory == null) { return; }

    // inventory.onInventoryDrag(ev);
  }

  /**
   * Unregister a tracked SCOInventories' InventoryView.
   * @param ev The InventoryCloseEvent that fired
   */
  @EventHandler(priority = EventPriority.MONITOR)
  public void onInventoryClose(InventoryCloseEvent ev) {
    SCOInventory si = this.activeViews.remove(ev.getView());
    if (si != null) {
      si.onInventoryClose(ev);
    }
  }

  /**
   * Triggers inventory saving when items are picked up off the floor
   */
  @EventHandler(priority = EventPriority.MONITOR)
  public void onEntityPickupItem(EntityPickupItemEvent ev) {
    if (ev.getEntityType() == EntityType.PLAYER) {
      Bukkit.getScheduler().runTask(SwordCraftOnline.getPluginInstance(), () -> {
        GameManager.findSCOPlayer((Player)ev.getEntity()).getPlayerInventory().saveInventory();
      });
    }
  }

  /**
   * Triggers inventory saving when player drops items
   */
  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerDropItemEvent(PlayerDropItemEvent ev) {
    Bukkit.getScheduler().runTask(SwordCraftOnline.getPluginInstance(), () -> {
      GameManager.findSCOPlayer(ev.getPlayer()).getPlayerInventory().saveInventory();
    });
  }

  /**
   * Check if there is already a registered view for a player's personal inventory being open.
   * @param s The SCOPlayer in question
   * @param v The inventory view in question
   */
  private void registerPlayerInventoryView(SCOPlayer s, InventoryView v) {
    if ((v.getType() == InventoryType.CRAFTING || v.getType() == InventoryType.CREATIVE) && !this.activeViews.containsKey(v)) {
      this.onInventoryOpen(v, s.getPlayerInventory());
    }
  }
}
