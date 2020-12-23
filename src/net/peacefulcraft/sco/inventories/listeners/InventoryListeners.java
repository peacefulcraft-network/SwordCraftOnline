package net.peacefulcraft.sco.inventories.listeners;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.SCOInventory;
import net.peacefulcraft.sco.items.AirItem;
import net.peacefulcraft.sco.items.CustomDataHolder;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;

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
    ItemIdentifier clickedItemId = this.resolveItemIdentifier(clickedItem);
    ItemIdentifier cursorItemId = this.resolveItemIdentifier(cursorItem);

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

    thisInventory.onClickThisInventory(ev, cursorItemId, clickedItemId);
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
      ItemIdentifier itemId = this.resolveItemIdentifier(slot.getValue());
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

    thisInventory.onThisInventoryDrag(ev, itemIdentifiers);
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
    this.activeViews.remove(ev.getView());
    SCOInventory si = this.inventoryMap.remove(ev.getInventory());
    if (si != null) {
      si.onInventoryClose(ev);
    }
  }

  /**
   * Triggers inventory saving when items are picked up off the floor
   */
  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
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
  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onPlayerDropItemEvent(PlayerDropItemEvent ev) {

    ItemIdentifier itemId = this.resolveItemIdentifier(ev.getItemDrop().getItemStack());
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

  private ItemIdentifier resolveItemIdentifier(ItemStack item) {
    if (item != null && item.getType() != Material.AIR) {
      NBTItem nbti = new NBTItem(item);
      if (nbti.hasKey("identifier")){
        if (ItemIdentifier.itemExists(nbti.getString("identifier"))) {
          String name = nbti.getString("identifier");
          ItemTier tier = ItemTier.COMMON;
          if (nbti.hasKey("tier")) {
            try {
              tier = ItemTier.valueOf(nbti.getString("tier"));
            } catch(IllegalArgumentException ex) {
              SwordCraftOnline.logWarning("Item " + name + " had invalid ItemTier " + nbti.getString("tier") + " falling back to COMMON");
            }
          } else {
            SwordCraftOnline.logWarning("Item " + name + " has no ItemTier encoded. Assuming COMMON");
          }

          ItemIdentifier identifier = ItemIdentifier.generateIdentifier(name, tier, item.getAmount());
          if (identifier instanceof CustomDataHolder) {
            ((CustomDataHolder) identifier).parseCustomItemData(item);
          }

          return identifier;

        } else {
          SwordCraftOnline.logWarning("Found unregonizable item with identifier " + nbti.getString("identifier"));
        }
      } else {
        SwordCraftOnline.logWarning("Found unrecognizable item of material " + item.getType() + " and display name " + item.getItemMeta().getDisplayName());
      }
    }

    return new AirItem(ItemTier.COMMON, 0);
  }
}
