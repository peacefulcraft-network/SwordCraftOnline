package net.peacefulcraft.sco.storage.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.inventories.InventoryType;
import net.peacefulcraft.sco.items.CustomDataHolder;
import net.peacefulcraft.sco.items.ItemIdentifier;

public class InventorySaveTask  {
  
  private long inventoryId;
  private long ownerId;
  private InventoryType type;
  private List<ItemIdentifier> items;

  /**
   * Used for saving existing inventories
   * @param inventoryId The database id of the inventory
   * @param ownerId The registry id of the inventory owner
   * @param items The list of items in the inventory
   * @param itemQuantities Array with item slot quantities with indexes matching those of items[] param
   */
  public InventorySaveTask(long inventoryId, long ownerId, InventoryType type, List<ItemIdentifier> items) {
    this.inventoryId = inventoryId;
    this.ownerId = ownerId;
    this.type = type;
    this.items = items;
  }

  /**
   * Save the inventory this task was setup to save at construction
   * @return A CompletableFuture that resolves with the inventoryId once the Inventory is saved,
   *         or resolves exceptionally if the an error occured., returning -1 for the inventoryId
   */
  public CompletableFuture<Long> saveInventory() {
    return CompletableFuture.supplyAsync(() -> {
      try (
        Connection con = SwordCraftOnline.getHikariPool().getConnection();
      ) {
        con.setAutoCommit(false);
  
        if (this.inventoryId > 0) {
          PreparedStatement stmt_update = con.prepareStatement("UPDATE `inventory` SET `size`=? WHERE `id`=?");
          stmt_update.setLong(1, items.size());
          stmt_update.setLong(2, inventoryId);
          stmt_update.executeUpdate();
          stmt_update.close();
        } else {
          PreparedStatement stmt_insert_inventory = con.prepareStatement("INSERT INTO `inventory`(type, size, player_id) VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
          stmt_insert_inventory.setString(1, type.toString());
          stmt_insert_inventory.setInt(2, items.size());
          stmt_insert_inventory.setLong(3, ownerId);
          stmt_insert_inventory.executeUpdate();
  
          ResultSet keys = stmt_insert_inventory.getGeneratedKeys();
          if (keys.next()) {
            this.inventoryId = keys.getLong(1);
          } else {
            throw new CompletionException(new Exception("Failed to insert new inventory for entity " + ownerId + ". Aborting inventory save."));
          }
        }
  
        PreparedStatement stmt_insert_item = con.prepareStatement(
          "INSERT INTO `inventory_item` VALUES(?, ?, ?, ?, ?, ?) " + 
          "ON DUPLICATE KEY UPDATE " +
          "`inventory_id`=VALUES(inventory_id)," +
          "`slot`=VALUES(slot)," +
          "`item_identifier`= VALUES(item_identifier)," +
          "`tier`=VALUES(tier)," +
          "`quantity`=VALUES(quantity)," +
          "`custom_item_data`=VALUES(custom_item_data)"
        );
        PreparedStatement stmt_delete_item = con.prepareStatement("DELETE FROM `inventory_item` WHERE `inventory_id`=? AND `slot`=?");
  
        for(int i=0; i<items.size(); i++) {
          if (items.get(i).getName().equalsIgnoreCase("Air")) {
            stmt_delete_item.setLong(1, this.inventoryId);
            stmt_delete_item.setInt(2, i);
            stmt_delete_item.executeUpdate();
          } else {
            stmt_insert_item.setLong(1, this.inventoryId);
            stmt_insert_item.setInt(2, i);
            stmt_insert_item.setString(3, items.get(i).getClass().getSimpleName().replaceAll("Item", ""));
            stmt_insert_item.setString(4, items.get(i).getTier().toString().toUpperCase());
            stmt_insert_item.setInt(5, items.get(i).getQuantity());
            if (items.get(i) instanceof CustomDataHolder) {
              stmt_insert_item.setString(6, ((CustomDataHolder) items.get(i)).getCustomData().toString());
            } else {
              stmt_insert_item.setString(6, "{}");
            }
  
            stmt_insert_item.executeUpdate();
          }
        }
  
        stmt_insert_item.close();
        stmt_delete_item.close();
        con.commit();
        return this.inventoryId;

      } catch (SQLException ex) {
        ex.printStackTrace();
        SwordCraftOnline.logSevere("A database error occured while saving inventory " + inventoryId + " for entity " + ownerId);
        throw new CompletionException(ex);
      }
    });
  }
}