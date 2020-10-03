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

public class SwordSkillInventory implements SCOInventory {

	private long inventoryId;

	public long getInventoryId() {
		return inventoryId;
	}

	private CompletableFuture<Void> inventoryReadyPromise;

	public CompletableFuture<Void> inventoryReadyPromise() {
		return this.inventoryReadyPromise;
	}

	private Inventory inventory;

	public SwordSkillInventory(Long inventoryId) {
		this.inventoryId = inventoryId;

		this.inventoryReadyPromise = CompletableFuture.runAsync(() -> {
			new InventoryLoadTask(inventoryId).fetchInventory().thenAcceptAsync((items) -> {
				// Get back on Bukkit's thread before we touch MC Inventories
				Bukkit.getScheduler().runTask(SwordCraftOnline.getPluginInstance(), () -> {
					this.inventory = Bukkit.getServer().createInventory(null, items.size(), "Sword Skill Inventory");

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

					// Indicate to any tasks waiting for this to complete that we've completed.
					this.inventoryReadyPromise.complete(null);
				});
			});
		});
	}

	@Override
	public boolean isInventory(Inventory inventory) {
		return this.inventory == inventory;
	}

	@Override
	public void openInventory(SCOPlayer s) {
		if (!this.inventoryReadyPromise.isDone()) {
			throw new RuntimeException(
					"Attempted to open Sword Skill Inventory " + this.inventoryId + " before it completed initializing.");
		}

	}

	@Override
	public void closeInventory() {
		inventory.getViewers().forEach((viewer) -> {
			viewer.closeInventory();
		});
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
		// TODO Auto-generated method stub
	}

	@Override
	public List<ItemIdentifier> generateItemIdentifiers() {
		// TODO Auto-generated method stub
		return null;
	}
}
