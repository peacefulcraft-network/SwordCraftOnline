package net.peacefulcraft.sco.inventories;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.CustomDataHolder;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.storage.tasks.InventoryLoadTask;
import net.peacefulcraft.sco.storage.tasks.InventorySaveTask;

public class PlayerInventory implements SCOInventory {

  private SCOPlayer s;
    public SCOPlayer getSCOPlayer() { return s; }

  private long inventoryId;
    public long getInventoryId() { return this.inventoryId; }

  /** Used for tracking when inventory item list is retrieved (internal) */
  private CompletableFuture<List<ItemIdentifier>> inventoryLoadPromise;
    public CompletableFuture<List<ItemIdentifier>> inventoryLoadPromise() { return this.inventoryLoadPromise; }

  private Inventory inventory;
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
      });
    });
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
  public void onClickThisInventory(InventoryClickEvent ev) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onClickThatInventory(InventoryClickEvent ev) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onThisInventoryDrag(InventoryDragEvent ev) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onThatInventoryDrag(InventoryDragEvent ev) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onInventoryClose(InventoryCloseEvent ev) {
    this.saveInventory();
  }

  public void saveInventory() {
    (new InventorySaveTask(this.inventoryId, this.s.getPlayerRegistryId(), InventoryType.PLAYER, this.generateItemIdentifiers()))
      .saveInventory()
      .thenAccept((inventoryId) -> {
        SwordCraftOnline.logDebug("Inventory " + this.inventoryId + " saved succesfully");
      });
  }

  @Override
  public List<ItemIdentifier> generateItemIdentifiers() {
    return SCOInventory.generateItemIdentifiers(this.inventory);
  }
  
}