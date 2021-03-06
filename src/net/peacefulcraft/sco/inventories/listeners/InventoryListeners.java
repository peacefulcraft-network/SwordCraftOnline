package net.peacefulcraft.sco.inventories.listeners;

import java.util.HashMap;
import java.util.Map.Entry;

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
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.PlayerInventory;
import net.peacefulcraft.sco.inventories.SCOInventory;
import net.peacefulcraft.sco.items.ItemIdentifier;

public class InventoryListeners implements Listener {
  
  private HashMap<InventoryView, Boolean> activeViews;
  private HashMap<Inventory, SCOInventory> inventoryMap;

  public InventoryListeners() {
    this.activeViews = new HashMap<InventoryView, Boolean>();
    this.inventoryMap = new HashMap<Inventory, SCOInventory>();
  }

  /**
   * Close all the currently open inventories for a plugin reload
   */
  public void closeActiveViews() {
    this.activeViews.keySet().forEach((view) -> {
      view.close();
    });
  }

  /**
   * Register when a player opens any SCOInventory
   * @param view The InventoryView instance created when the Inventory was opened.
   * @param inventory The SCOInventory instance for the opened Inventory
   */
  public void onInventoryOpen(InventoryView view, SCOInventory inventory) {
    this.activeViews.put(view, true);
    this.inventoryMap.put(inventory.getInventory(), inventory);
  }

  /**
   * Dispatch click events generated inside of SCOInventories to their respective instance.
   * @param ev The InventoryClickEvent that fired.
   */
  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onInventoryClick(InventoryClickEvent ev) {
    ItemStack clickedItem = ev.getCurrentItem();
    ItemStack cursorItem = ev.getCursor();
    ItemIdentifier clickedItemId = ItemIdentifier.resolveItemIdentifier(clickedItem);
    ItemIdentifier cursorItemId = ItemIdentifier.resolveItemIdentifier(cursorItem);

    // Ignore vanilla items
    if (clickedItemId == null && cursorItemId == null) { return; }

    // Prevent static items from being moved
    if (!clickedItemId.isMovable() || !cursorItemId.isMovable()) { ev.setCancelled(true); }
    
    // Route and dispatch event to the inventory
    SCOInventory thisInventory;
    SCOInventory thatInventory;
    if (ev.getClickedInventory() == ev.getView().getBottomInventory()) {
      thisInventory = this.inventoryMap.get(ev.getView().getBottomInventory());
      thatInventory = this.inventoryMap.get(ev.getView().getTopInventory());
    } else {
      thisInventory = this.inventoryMap.get(ev.getView().getTopInventory());
      thatInventory = this.inventoryMap.get(ev.getView().getBottomInventory());
    }

    SwordCraftOnline.logDebug("Resolved this inventory to " + ((thisInventory == null) ? "null" : thisInventory.getInventory().getType()) + " and that inventory to " + ((thatInventory == null) ? "null" : thatInventory.getInventory().getType()));

    if (thisInventory != null) {
      thisInventory.onClickThisInventory(ev, cursorItemId, clickedItemId);
    }

    if (thatInventory != null) {
      thatInventory.onClickThatInventory(ev, cursorItemId, clickedItemId);
    }
  }

  /**
   * Dispatch drag events generated inside of SCOInventories to their respective instance.
   * If any items involved in the drag operation are not dragable, the whole trasaction is cancelled.
   * @param ev The InventoryDragEvent that fired.
   */
  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onInventoryDrag(InventoryDragEvent ev) {
    HashMap<Integer, ItemIdentifier> itemIdentifiers = new HashMap<Integer, ItemIdentifier>();
    for(Entry<Integer, ItemStack> slot : ev.getNewItems().entrySet()) {
      ItemIdentifier itemId = ItemIdentifier.resolveItemIdentifier(slot.getValue());
      itemIdentifiers.put(slot.getKey(), itemId);

      if (!itemId.isMovable()) { ev.setCancelled(true); }
    }

    SCOInventory thisInventory;
    SCOInventory thatInventory; 
    if (ev.getInventory() == ev.getView().getBottomInventory()) {
      thisInventory = this.inventoryMap.get(ev.getView().getBottomInventory());
      thatInventory = this.inventoryMap.get(ev.getView().getTopInventory());
    } else {
      thisInventory = this.inventoryMap.get(ev.getView().getTopInventory());
      thatInventory = this.inventoryMap.get(ev.getView().getBottomInventory());
    }

    if (thisInventory != null) {
      thisInventory.onThisInventoryDrag(ev, itemIdentifiers);
    }
    
    if (thatInventory != null) {
      thatInventory.onThatInventoryDrag(ev, itemIdentifiers);
    }
  }

  /**
   * Unregister a tracked SCOInventories' InventoryView.
   * @param ev The InventoryCloseEvent that fired
   */
  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onInventoryClose(InventoryCloseEvent ev) {
    SCOInventory topInventory = this.inventoryMap.get(ev.getView().getTopInventory());
    SCOInventory bottomInventory = this.inventoryMap.get(ev.getView().getBottomInventory());

    /*
     * Mark inventory as closed.
     * Player inventories don't ever close, nor do they fire open events so
     * we can't remove the view ever or else we'll loose it and bad things happen. 
     */
    if (topInventory != null && !(topInventory instanceof PlayerInventory)) {
      this.inventoryMap.remove(ev.getView().getTopInventory());
      SwordCraftOnline.logDebug("No longer tracking inventory of type " + ev.getView().getTopInventory().getType());
    }
    if (bottomInventory != null && !(bottomInventory instanceof PlayerInventory)) {
      this.inventoryMap.remove(ev.getView().getBottomInventory());
      SwordCraftOnline.logDebug("No longer tracking inventory of type " + ev.getView().getBottomInventory().getType());
    }

    // Short circuit for null values, then make sure this was not the InventoryView for the player's inventory
    if (
      (topInventory != null &&!topInventory.isInventory(ev.getInventory())) &&
      (bottomInventory != null && !bottomInventory.isInventory(ev.getInventory()))
    ) {
      this.activeViews.remove(ev.getView());
      SwordCraftOnline.logDebug("No longer tracking InventoryView for inventory of type " + ev.getView().getType());
    }

    if (topInventory != null) {
      SwordCraftOnline.logDebug("Dispatching inventory close event to inventory type " + ev.getView().getTopInventory().getType());
      topInventory.onInventoryClose(ev);
    } else {
      SwordCraftOnline.logDebug("Ignoring close event for inventory type " + ev.getView().getTopInventory().getType());
    }

    if (bottomInventory != null) {
      SwordCraftOnline.logDebug("Dispatching inventory close event to inventory type " + ev.getView().getBottomInventory().getType());
      bottomInventory.onInventoryClose(ev);
    } else {
      SwordCraftOnline.logDebug("Ignoring close event for inventory type " + ev.getView().getBottomInventory().getType());
    }
  }

  /**
   * Triggers inventory saving when items are picked up off the floor
   * Holds logic for weapon limitations in inventory
   */
  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onEntityPickupItem(EntityPickupItemEvent ev) {
    if (ev.getEntityType() == EntityType.PLAYER) {
      ItemIdentifier item = ItemIdentifier.resolveItemIdentifier(ev.getItem().getItemStack());
      GameManager.findSCOPlayer((Player)ev.getEntity()).getPlayerInventory().onPlayerPickup(ev, item);

      Bukkit.getScheduler().runTask(SwordCraftOnline.getPluginInstance(), () -> {
        GameManager.findSCOPlayer((Player)ev.getEntity()).getPlayerInventory().saveInventory();
      });
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onPlayerChangeHeldItem(PlayerItemHeldEvent ev) {
    ItemIdentifier item = ItemIdentifier.resolveItemIdentifier(
      ev.getPlayer().getInventory().getItem(ev.getNewSlot())
    );
    GameManager.findSCOPlayer(ev.getPlayer()).getPlayerInventory().onPlayerChangeHeldItem(ev, item);
  }

  /**
   * Triggers inventory saving when player drops items
   */
  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onPlayerDropItemEvent(PlayerDropItemEvent ev) {

    ItemIdentifier itemId = ItemIdentifier.resolveItemIdentifier(ev.getItemDrop().getItemStack());
    if (!itemId.isDroppable()) {
      ev.setCancelled(true);
      return;
    }

    Bukkit.getScheduler().runTask(SwordCraftOnline.getPluginInstance(), () -> {
      GameManager.findSCOPlayer(ev.getPlayer()).getPlayerInventory().saveInventory();
    });
  }

  /**
   * Register the player's personal Inventory view. Called when player is joined to SCO
   * @param s The SCOPlayer in question
   * @param v The inventory view in question
   */
  public void registerPlayerInventoryView(SCOPlayer s, InventoryView v) {
    this.activeViews.put(v, true);
    this.inventoryMap.put(s.getPlayerInventory().getInventory(), s.getPlayerInventory());
  }

  /**
   * Unregisters a player's personal Inventory view. Called when a player leaves SCO.
   * @param s The SCOPlayer in question
   * @param v The inventory view in question
   */
  public void unregisterPlayerInventoryView(SCOPlayer s, InventoryView v) {
    this.activeViews.remove(v);
    this.inventoryMap.remove(s.getPlayerInventory().getInventory());
  }
}
