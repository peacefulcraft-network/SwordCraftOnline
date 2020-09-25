package net.peacefulcraft.sco.storage.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CompletableFuture;

import org.bukkit.scheduler.BukkitRunnable;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.inventories.InventoryType;
import net.peacefulcraft.sco.items.CustomDataHolder;
import net.peacefulcraft.sco.items.ItemIdentifier;

public class InventorySaveTask extends BukkitRunnable {
  
  private long inventoryId;
  private long ownerId;
  private InventoryType type;
  private ItemIdentifier[] items;

  private CompletableFuture<Void> promise;
    public CompletableFuture<Void> getCompletableFuture() { return this.promise; }

  /**
   * @param inventoryId If applicable, the database id of the inventory
   * @param ownerId The registry id of the inventory owner
   * @param items The list of items in the inventory
   * @param itemQuantities Array with item slot quantities with indexes matching those of items[] param
   * @param callback The storage task to execute after the task completes
   */
  public InventorySaveTask(long inventoryId, long ownerId, InventoryType type, ItemIdentifier[] items) {
    this.inventoryId = inventoryId;
    this.ownerId = ownerId;
    this.type = type;
    this.items = items;
    this.promise = new CompletableFuture<Void>();
  }

  @Override
  public void run() {
    try (
      Connection con = SwordCraftOnline.getHikariPool().getConnection();
    ) {
      con.setAutoCommit(false);

      if (inventoryId > 0) {
        PreparedStatement stmt_update = con.prepareStatement("UPDATE `inventory` SET `size`=? WHERE `id`=?");
        stmt_update.setLong(1, items.length);
        stmt_update.setLong(2, inventoryId);
        stmt_update.executeUpdate();
        stmt_update.close();
      } else {
        PreparedStatement stmt_insert_inventory = con.prepareStatement("INSERT INTO `inventory`(type, size, player_id) VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        stmt_insert_inventory.setString(1, type.toString());
        stmt_insert_inventory.setInt(2, items.length);
        stmt_insert_inventory.setLong(3, ownerId);
        stmt_insert_inventory.executeUpdate();

        ResultSet keys = stmt_insert_inventory.getGeneratedKeys();
        if (keys.next()) {
          inventoryId = keys.getLong(1);
        } else {
          SwordCraftOnline.logSevere("Failed to insert new inventory for entity " + ownerId + ". Aborting inventory save.");
          return;
        }
      }

      PreparedStatement stmt_insert_item = con.prepareStatement(
        "INSERT INTO `inventory_item` VALUES(?, ?, ?, ?, ?, ?) " + 
        "ON DUPLICATE KEY UPDATE " +
        "`item_identifier`= VALUES(item_identifier)," +
        "`tier`=VALUES(tier)," +
        "`quantity`=VALUES(quantity)," +
        "`custom_item_data`=VALUES(custom_item_data)"
      );
      PreparedStatement stmt_delete_item = con.prepareStatement("DELETE FROM `inventory_item` WHERE `inventory_id`=? AND `slot`=?");

      for(int i=0; i<items.length; i++) {
        if (items[i] == null) {
          stmt_delete_item.setLong(1, inventoryId);
          stmt_delete_item.setInt(2, i);
          stmt_delete_item.executeUpdate();
        } else {
          stmt_insert_item.setLong(1, inventoryId);
          stmt_insert_item.setInt(2, i);
          stmt_insert_item.setString(3, items[i].getClass().getSimpleName());
          stmt_insert_item.setString(4, items[i].getTier().toString().toUpperCase());
          stmt_insert_item.setInt(5, items[i].getQuantity());
          if (items[i] instanceof CustomDataHolder) {
            stmt_insert_item.setString(6, ((CustomDataHolder) items[i]).getCustomData().toString());
          } else {
            stmt_insert_item.setString(6, "{}");
          }

          stmt_insert_item.executeUpdate();
        }
      }

      con.commit();
      promise.complete(null);
    } catch (SQLException ex) {
      ex.printStackTrace();
      SwordCraftOnline.logSevere("A database error occured while saving inventory " + inventoryId + " for entity " + ownerId);
      promise.completeExceptionally(ex);
    }
  }
}