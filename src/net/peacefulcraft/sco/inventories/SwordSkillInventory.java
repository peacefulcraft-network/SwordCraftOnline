package net.peacefulcraft.sco.inventories;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.announcer.Announcer;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.storage.tasks.InventoryLoadTask;
import net.peacefulcraft.sco.storage.tasks.InventorySaveTask;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillTrigger;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

/**
 * Persistent Sword Skill Inventory Loads contents from database using provided
 * information. Automatically saves contents back to database on close.
 */
public class SwordSkillInventory extends BukkitInventoryBase {

	protected SwordSkillCaster s;

	public SwordSkillCaster getInventoryHolder() {
		return s;
	}

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

	public SwordSkillInventory(SwordSkillCaster s, Long inventoryId, Long ownerId) {
		this.s = s;
		this.inventoryId = inventoryId;
		this.ownerId = ownerId;

		this.inventoryReadyPromise = CompletableFuture.runAsync(() -> {
			new InventoryLoadTask(inventoryId).fetchInventory().thenAcceptAsync((items) -> {
				// Get back on Bukkit's thread before we touch MC Inventories
				Bukkit.getScheduler().runTask(SwordCraftOnline.getPluginInstance(), () -> {
					this.inventory = Bukkit.getServer().createInventory(null, items.size(), "Sword Skill Inventory");
					// TODO: Move this to more dynamic blocking solution
					// Currently permanently initializes inventory regardless
					// of item loads
					for (int col = 0; col <= 8; col++) {
						setItem(9 + col, ItemIdentifier.generateIdentifier("BlackSlot", ItemTier.COMMON, 1));
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
		SwordCraftOnline.getInventoryListeners().onInventoryOpen(s.getPlayer().openInventory(this.inventory), this);
	}

	@Override
	public void closeInventory() {
		inventory.getViewers().forEach((viewer) -> {
			viewer.closeInventory();
		});
	}

	@Override
	public void onClickThisInventory(InventoryClickEvent ev, ItemIdentifier cursorItem, ItemIdentifier clickedItem) {
		//SwordCraftOnline.logDebug("Clicked SwordSKill Inventory");

		// We only want sword skill provider items in this inventory
		if(!(cursorItem instanceof SwordSkillProvider) && !(clickedItem instanceof SwordSkillProvider)) { 
			ev.setCancelled(true);
			return;
		}

		// If the cursor item is air we return without cancelling
		if(cursorItem.getMaterial().equals(Material.AIR)) { 
			return;
		}

		SwordSkillProvider cursorProvider = (SwordSkillProvider)cursorItem;
		SwordSkillType cursorType = cursorProvider.getType();

		// Checking skill slot equivalents
		if(cursorType.equals(SwordSkillType.SWORD) && ev.getSlot() != 0) { 
			ev.setCancelled(true);
			if(this.s instanceof SCOPlayer) {
				Announcer.messagePlayer((SCOPlayer)this.s, "You can only equip this type of skill in slot 0.", 0);
			}
			return;
		} else if(cursorType.equals(SwordSkillType.PRIMARY) && ev.getSlot() != 1) {
			ev.setCancelled(true);
			if(this.s instanceof SCOPlayer) {
				Announcer.messagePlayer((SCOPlayer)this.s, "You can only equip this type of skill in slot 1.", 0);
			}
			return;
		} else if(cursorType.equals(SwordSkillType.SECONDARY) && ev.getSlot() != 2) {
			ev.setCancelled(true);
			if(this.s instanceof SCOPlayer) {
				Announcer.messagePlayer((SCOPlayer)this.s, "You can only equip this type of skill in slot 2.", 0);
			}
			return;
		} else if(cursorType.equals(SwordSkillType.PASSIVE) && ev.getSlot() <= 2) {
			ev.setCancelled(true);
			if(this.s instanceof SCOPlayer) {
				Announcer.messagePlayer((SCOPlayer)this.s, "You can only equip this in the left 6 slots.", 0);
			}
			return;
		}

		// Checking placed skill quantity
		if(cursorProvider.getQuantity() > 1) {
			ev.setCancelled(true);
			if(this.s instanceof SCOPlayer) {
				Announcer.messagePlayer((SCOPlayer)this.s, "You can't place more than 1 skill in a slot.", 0);
			}
			return;
		}

		// Checking if skill is already in first row
		if(isSkillRegistered(cursorItem.getName())) {
			ev.setCancelled(true);
			if(this.s instanceof SCOPlayer) {
				Announcer.messagePlayer((SCOPlayer)this.s, "You can't equip the same skill twice.", 0);
			}
			return;
		}
	}

	@Override
	public void onClickThatInventory(InventoryClickEvent ev, ItemIdentifier cursorItem, ItemIdentifier clickedItem) {
		//SwordCraftOnline.logDebug("Clicked player inv while in SwordSkill Inventory");
	}

	@Override
	public void onThisInventoryDrag(InventoryDragEvent ev, HashMap<Integer, ItemIdentifier> items) {
		//SwordCraftOnline.logDebug("Drug items in SwordSkill inventory");
	}

	@Override
	public void onThatInventoryDrag(InventoryDragEvent ev, HashMap<Integer, ItemIdentifier> items) {
		//SwordCraftOnline.logDebug("Drug items in player inv while in SwordSkill Inventory");
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent ev) {
		(new InventorySaveTask(this.inventoryId, this.ownerId, InventoryType.SWORD_SKILL, this.generateItemIdentifiers()))
				.saveInventory().thenAccept((inventoryId) -> {
					SwordCraftOnline.logDebug("Inventory " + this.inventoryId + " saved succesfully");
				});

		this.s.getSwordSkillManager().syncSkillInventory(this);
		this.s.getSwordSkillManager().abilityExecuteLoop(SwordSkillTrigger.PASSIVE, null);
	}

	private boolean isSkillRegistered(String name) {
		for(int i = 0; i < 8; i++) {
			if(this.getItem(i).getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
}
