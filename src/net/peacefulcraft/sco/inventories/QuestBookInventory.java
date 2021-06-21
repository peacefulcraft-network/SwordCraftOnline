package net.peacefulcraft.sco.inventories;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.storage.tasks.InventoryLoadTask;
import net.peacefulcraft.sco.storage.tasks.InventorySaveTask;

public class QuestBookInventory extends BukkitInventoryBase implements Listener {

    private SCOPlayer quester;
        public SCOPlayer getQuester() { return this.quester; }

    protected Long inventoryId;

    public Long getInventoryId() {
        return inventoryId;
    }

    protected Long ownerId;

    public Long getOwnerId() {
        return ownerId;
    }

    protected CompletableFuture<Void> inventoryReadyPromise;

    public CompletableFuture<Void> inventoryReadyPromise() {
        return this.inventoryReadyPromise;
    }

    // Private inventory storing quest items
    public QuestBookInventory(SCOPlayer s, Long inventoryId, Long ownerId) {
        this.quester = s;
        this.inventoryId = inventoryId;
        this.ownerId = ownerId;

        this.inventoryReadyPromise = CompletableFuture.runAsync(() -> {
            new InventoryLoadTask(inventoryId).fetchInventory().thenAcceptAsync((items) -> {

                Bukkit.getScheduler().runTask(SwordCraftOnline.getPluginInstance(), () -> {
                    this.inventory = Bukkit.getServer().createInventory(null, items.size(), "Quest Book Inventory");

                    this.setInventoryContents(items);

                    this.inventoryReadyPromise.complete(null);
                });
            });
        });

        //TODO: Create and load complete quest inventory
    }

    public void destroy() {

    }

    @Override
    public boolean isInventory(Inventory inventory) {
        return this.inventory == inventory;
    }

    @Override
    public void openInventory(SCOPlayer s) {
        if (!this.inventoryReadyPromise.isDone()) {
            throw new RuntimeException(
                "Attempted to open Quest Book Inventory " + this.inventoryId + " before it completed initializing."
            );
        }
        SwordCraftOnline.getInventoryListeners().onInventoryOpen(s.getPlayer().openInventory(this.inventory), this);
    }

    @Override
    public void onClickThisInventory(InventoryClickEvent ev, ItemIdentifier cursorItem, ItemIdentifier clickedItem) {
        if(!isQuestBook(cursorItem) && !(isQuestBook(clickedItem))) {
            ev.setCancelled(true);
            return;
        }

        if(cursorItem.getMaterial().equals(Material.AIR)) {
            return;
        }
    }

    @Override
    public void onClickThatInventory(InventoryClickEvent ev, ItemIdentifier cursorItem, ItemIdentifier clickedItem) {

    }

    @Override
    public void onThisInventoryDrag(InventoryDragEvent ev, HashMap<Integer, ItemIdentifier> items) {

    }

    @Override
    public void onThatInventoryDrag(InventoryDragEvent ev, HashMap<Integer, ItemIdentifier> items) {

    }

    @Override
    public void onInventoryClose(InventoryCloseEvent ev) {
        (new InventorySaveTask(this.inventoryId, this.ownerId, InventoryType.QUEST_BOOK, this.generateItemIdentifiers()))
            .saveInventory().thenAccept((inventoryId) -> {
                SwordCraftOnline.logDebug("Inventory " + this.inventoryId + " saved successfully.");
            });
        this.quester.getQuestBookManager().syncQuestBookInventory(this);
    }

    @Override
    public void closeInventory() {
        inventory.getViewers().forEach((viewer) -> {
            viewer.closeInventory();
        });
    }

    private boolean isQuestBook(ItemIdentifier ident) {
        return ident.getName().equalsIgnoreCase("Quest");
    }
    
}