package net.peacefulcraft.sco.storage.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.items.CustomDataHolder;
import net.peacefulcraft.sco.items.ItemIdentifier;

public class InventorySaveSlotTask {
	private Long inventoryId;
	private Integer slot;
	private ItemIdentifier item;

	/**
	 * Used for updating a single slot in an existing inventory
	 * @param inventoryId The inventory to update
	 * @param slot The slot in the inventory to update
	 * @param item The item to place in the slot of the requested inventory
	 */
	public InventorySaveSlotTask(Long inventoryId, Integer slot, ItemIdentifier item) {
		this.inventoryId = inventoryId;
		this.slot = slot;
		this.item = item;
	}

	public CompletableFuture<Void> saveInventory() {
		return CompletableFuture.runAsync(() -> {
			try(
				Connection con = SwordCraftOnline.getHikariPool().getConnection();
			) {
				PreparedStatement stmt_update = con.prepareStatement(
					"UPDATE `inventory_item` SET `item_identifier`=?, `tier`=?, `quantity`=?, `custom_item_data`=? WHERE `inventory_id`=? AND `slot`=?"
				);
				stmt_update.setString(1, this.item.getClass().getSimpleName().replaceAll("Item", ""));
				stmt_update.setString(2, this.item.getTier().toString().toUpperCase());
				stmt_update.setInt(3, this.item.getQuantity());

				if (this.item instanceof CustomDataHolder) {
					stmt_update.setString(4, ((CustomDataHolder) this.item).getCustomData().toString());
				} else {
					stmt_update.setString(4, "{}");
				}

				stmt_update.executeUpdate();
				stmt_update.close();
				con.close();
			} catch (SQLException ex) {
        ex.printStackTrace();
        SwordCraftOnline.logSevere("A database error occured while saving single inventory slot for " + this.inventoryId + " slot " + this.slot);
        throw new CompletionException(ex);
      }
		});
	}
}
