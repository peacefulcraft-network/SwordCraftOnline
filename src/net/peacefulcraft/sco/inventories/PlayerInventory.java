package net.peacefulcraft.sco.inventories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonObject;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.CustomDataHolder;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.WeaponAttributeHolder;
import net.peacefulcraft.sco.storage.tasks.InventoryLoadTask;
import net.peacefulcraft.sco.storage.tasks.InventorySaveTask;
import net.peacefulcraft.sco.swordskills.weaponskills.WeaponModifier;
import net.peacefulcraft.sco.swordskills.weaponskills.WeaponModifier.WeaponModifierType;

public class PlayerInventory extends BukkitInventoryBase {

  private SCOPlayer s;
    public SCOPlayer getSCOPlayer() { return s; }

  private long inventoryId;
    public long getInventoryId() { return this.inventoryId; }

  /** Used for tracking when inventory item list is retrieved (internal) */
  private CompletableFuture<List<ItemIdentifier>> inventoryLoadPromise;
    public CompletableFuture<List<ItemIdentifier>> inventoryLoadPromise() { return this.inventoryLoadPromise; }
  /**
   * Initialize the inventory by the inventories' registry Id. Player's Inventory
   * instance must be bound later on before full initialization is complete.
   * 
   * @param s SCOPlayer to which the invnetory belongs
   * @param inventoryId Inventory registry id of the inventory to load
   */
  public PlayerInventory(SCOPlayer s, long inventoryId) {
    this.s = s;
    this.inventoryId = inventoryId;

    SwordCraftOnline.logDebug("Starting content fetch for PlayerInventory " + inventoryId);
    InventoryLoadTask task = new InventoryLoadTask(inventoryId);
    this.inventoryLoadPromise = task.fetchInventory();
  }

  /**
   * Bind this inventory to this SCOPlayer's Inventory.
   * 
   * @param inventory Inventory to bind to
   * @return A CompletableFuture that resolves once the inventory contents have been placed
   *         in the requetsted inventory. This is the same CompletableFuture that is returned by
   *         PlayerInventory.inventoryReadyPromise().
   */
  public void bindInventory(Inventory inventory) {
    this.inventory = inventory;

    this.inventoryLoadPromise.thenAcceptAsync((items) -> {
      // Get back on the Bukkit thread before we touch the inventory
      Bukkit.getScheduler().runTask(SwordCraftOnline.getPluginInstance(), () -> {
        SwordCraftOnline.logDebug("Generating PlayerInventory " + inventoryId);
        for(int i=0; i<items.size(); i++) {
          ItemIdentifier itemIdentifier = items.get(i);
          ItemStack item = null;

          if (itemIdentifier instanceof CustomDataHolder) {
            CustomDataHolder customDataItem = (CustomDataHolder) itemIdentifier;
            item = ItemIdentifier.generateItem(itemIdentifier.getName(), itemIdentifier.getTier(), itemIdentifier.getQuantity(), customDataItem.getCustomData());
          } else {
            item = ItemIdentifier.generateItem(itemIdentifier.getName(), itemIdentifier.getTier(), itemIdentifier.getQuantity());
          }

          inventory.setItem(i, item);
        }
        s.getPlayer().updateInventory();
      });
    });
  }

  @Override
  public void setInventoryContents(List<ItemIdentifier> items) {
    super.setInventoryContents(items);
    s.getPlayer().updateInventory();
  }

  @Override
  public void setInventorySlots(HashMap<Integer, ItemIdentifier> items) {
    super.setInventorySlots(items);
    s.getPlayer().updateInventory();
  }

  @Override
  public HashMap<Integer, ItemIdentifier> addItem(ItemIdentifier item) {
    HashMap<Integer, ItemIdentifier> leftovers =  super.addItem(item);
    s.getPlayer().updateInventory();
    return leftovers;
  }

  @Override
  public void setItem(Integer slot, ItemIdentifier item) {
    super.setItem(slot, item);
    s.getPlayer().updateInventory();
  }

  @Override
  public void removeItem(ItemStack item) {
    super.removeItem(item);
    s.getPlayer().updateInventory();
  }

  @Override
  public ItemIdentifier removeItem(int index) {
    ItemIdentifier removed = super.removeItem(index);
    s.getPlayer().updateInventory();
    return removed;
  }

  @Override
  public boolean isInventory(Inventory inventory) {
    return this.inventory == inventory;
  }

  @Override
  public void openInventory(SCOPlayer s) {
    s.getPlayer().openInventory(this.inventory);
  }

  @Override
  public void closeInventory() {
    inventory.getViewers().forEach((viewer) -> { viewer.closeInventory(); });
  }

  @Override
  public void onClickThisInventory(InventoryClickEvent ev, ItemIdentifier cursorItem, ItemIdentifier clickedItem) {
    // Only check if we click hotbar and not drop click
    if(ev.getSlotType().equals(SlotType.QUICKBAR) && !ev.getClick().equals(ClickType.DROP)) {

      String cursorWeaponType = (cursorItem instanceof CustomDataHolder) ? 
        ((CustomDataHolder)cursorItem).getCustomData().get("weapon").getAsString() : "";
      String clickedWeaponType = (clickedItem instanceof CustomDataHolder) ?
        ((CustomDataHolder)clickedItem).getCustomData().get("weapon").getAsString() : "";

      HashMap<String, Integer> checked = getHotbarWeapons();

      if(checked.get("sword") >= 1 && cursorWeaponType.equalsIgnoreCase("sword")) {
        ev.setCancelled(true);
      }
      if(checked.get("knife") >= 1 && cursorWeaponType.equalsIgnoreCase("knife")) {
        ev.setCancelled(true);
      }
      if(checked.get("range") >= 1 && cursorWeaponType.equalsIgnoreCase("range")) {
        ev.setCancelled(true);
      }
    }
  }

  @Override
  public void onClickThatInventory(InventoryClickEvent ev, ItemIdentifier cursorItem, ItemIdentifier clickedItem) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onThisInventoryDrag(InventoryDragEvent ev, HashMap<Integer, ItemIdentifier> items) {
    boolean containsHotbar = false;
    for(Integer i : items.keySet()) {
      if(i >= 0 && i <= 8) { containsHotbar = true; break; }
    }

    // Checking old items in cursor.
    // These items should retain their weapon NBT and be consistent to old items.
    // Main difference is item amount
    ItemIdentifier item = ItemIdentifier.resolveItemIdentifier(ev.getOldCursor());
    String weaponType = (item instanceof CustomDataHolder) ?
      ((CustomDataHolder)item).getCustomData().get("weapon").getAsString() : "";

    // If hotbar is modified and the item is a weapon
    if(containsHotbar && !weaponType.equalsIgnoreCase("")) {
      HashMap<String, Integer> checked = getHotbarWeapons();

      if(checked.get(weaponType) != null && checked.get(weaponType) >= 1) {
        ev.setCancelled(true);
      }
    }

  }

  @Override
  public void onThatInventoryDrag(InventoryDragEvent ev, HashMap<Integer, ItemIdentifier> items) {
    // TODO Auto-generated method stub

  }

  /**
   * Called when player picks up item
   * @param ev
   * @param item
   */
  public void onPlayerPickup(EntityPickupItemEvent ev, ItemIdentifier item) {
    if(item.getMaterial().equals(Material.AIR)) { return; }
    String weaponType = (item instanceof CustomDataHolder) ? 
      ((CustomDataHolder)item).getCustomData().get("weapon").getAsString() : "";

    HashMap<String, Integer> checked = getHotbarWeapons();
    if(checked.get(weaponType) != null && checked.get(weaponType) >= 1) {
      for(int i = 9; i <= 35; i++) {
        ItemIdentifier temp = ItemIdentifier.resolveItemIdentifier(this.inventory.getItem(i));
        if(temp != null && temp.getMaterial().equals(Material.AIR)) {
          setItem(i, item);
          ev.getItem().remove();
          ev.setCancelled(true);
          return;
        }
      }
      ev.setCancelled(true);
    }
  }

  /**
   * Called when player changes held item
   * @param ev
   * @param item
   */
  public void onPlayerChangeHeldItem(PlayerItemHeldEvent ev, ItemIdentifier item) {
    HashMap<String, ArrayList<WeaponModifier>> actives = new HashMap<>();
    if(!item.getMaterial().equals(Material.AIR) && item instanceof WeaponAttributeHolder) {
      ArrayList<WeaponModifier> activeMods = WeaponAttributeHolder.parseLore(this.inventory.
        getItem(ev.getNewSlot())).get(WeaponModifierType.ACTIVE);
      if(activeMods == null || activeMods.isEmpty()) { return; }
      actives.put(ChatColor.stripColor(item.getDisplayName()), activeMods);
    }
    GameManager.findSCOPlayer(ev.getPlayer()).applyWeaponModifiers(actives, WeaponModifierType.ACTIVE);
  }

  @Override
  public void onInventoryClose(InventoryCloseEvent ev) {
    HashMap<String, ArrayList<WeaponModifier>> passives = new HashMap<>();

    for(int i = 0; i <= 8; i++) {
      ItemIdentifier item = ItemIdentifier.resolveItemIdentifier(this.inventory.getItem(i));
      if(!item.getMaterial().equals(Material.AIR) && item instanceof WeaponAttributeHolder) {
        ArrayList<WeaponModifier> passMods = WeaponAttributeHolder.parseLore(this.inventory.getItem(i)).get(WeaponModifierType.PASSIVE);
        if(passMods == null || passMods.isEmpty()) { continue; }
        passives.put(ChatColor.stripColor(item.getDisplayName()), passMods);  
      }
    }

    HashMap<String, Integer> check = getHotbarWeapons();
    if(check.get("sword") == 0 && check.get("knife") == 0 && check.get("range") == 0) {
      passives = null;
    }
    GameManager.findSCOPlayer((Player)ev.getPlayer()).applyWeaponModifiers(passives, WeaponModifierType.PASSIVE);

    this.saveInventory();
  }

  public void saveInventory() {
    (new InventorySaveTask(this.inventoryId, this.s.getPlayerRegistryId(), InventoryType.PLAYER, this.generateItemIdentifiers()))
      .saveInventory()
      .thenAccept((inventoryId) -> {
        SwordCraftOnline.logDebug("Inventory " + this.inventoryId + " saved succesfully");
      });
  } 
  
  /**
   * Checks players hotbar for weapon types
   * @return Map of weapon type with amount of weapons
   */
  public HashMap<String, Integer> getHotbarWeapons() {
    int swordCount = 0;
    int knifeCount = 0;
    int rangeCount = 0;
    for(int i = 0; i < 9; i++) {
      ItemIdentifier item = ItemIdentifier.resolveItemIdentifier(this.inventory.getItem(i));
      if(item.getMaterial().equals(Material.AIR)) { continue; }

      if(item instanceof CustomDataHolder) {
        // Handling limiting items. If count exceeds 1 we move it to main inventory
        JsonObject obj = ((CustomDataHolder)item).getCustomData();
        switch(obj.get("weapon").getAsString()) {
          case "sword":
              swordCount++; 
          break; case "knife":
              knifeCount++;
          break; case "range":
              rangeCount++;
        }
      }
    }
    HashMap<String, Integer> out = new HashMap<>();
    out.put("sword", swordCount);
    out.put("knife", knifeCount);
    out.put("range", rangeCount);
    return out;
  }  

  /**
   * Checks inventory hotbar for weapon type
   * 
   * @param weapon
   * @return
   */
  public ItemIdentifier getHotbarWeapon(String weapon) {
    for(int i = 0; i < 9; i++) {
      ItemIdentifier item = ItemIdentifier.resolveItemIdentifier(this.inventory.getItem(i));
      if(item.getMaterial().equals(Material.AIR)) { continue; }

      if(item instanceof CustomDataHolder) {
        JsonObject obj = ((CustomDataHolder)item).getCustomData();
        if(obj.get("weapon").getAsString().equalsIgnoreCase(weapon)) {
          return item;
        }
      }
    }
    return null;
  }
}
