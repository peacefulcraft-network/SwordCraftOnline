package net.peacefulcraft.sco.storage.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Supplier;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.items.CustomDataHolder;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;

public class InventoryLoadTask {

  private long inventoryId;
  private ArrayList<ItemIdentifier> items;

  /**
   * @param inventoryId The ID of the inventory to load
   * @param Inventory The inventory 
   */
  public InventoryLoadTask(long inventoryId) {
    this.inventoryId = inventoryId;
  }


  public CompletableFuture<List<ItemIdentifier>> fetchInventory() {
    Supplier<List<ItemIdentifier>> supplier = () -> {
      SwordCraftOnline.logDebug("Starting InventoryLoadtask for inventory " + inventoryId);
      try (
        Connection con = SwordCraftOnline.getHikariPool().getConnection();
      ) {
        PreparedStatement stmt_select_inventory = con.prepareStatement("SELECT `size` FROM `inventory` WHERE `id`=?");
        stmt_select_inventory.setLong(1, inventoryId);
        ResultSet res = stmt_select_inventory.executeQuery();

        int invSize = 9;
        if (res.next()) {
          invSize = res.getInt(1);
          items = new ArrayList<ItemIdentifier>(invSize);
        } else {
          SwordCraftOnline.logWarning("Attempted to load non-existent inventory " + inventoryId);
          return (List<ItemIdentifier>) null;
        }
        stmt_select_inventory.close();

        // Populate the empty slots
        for(int i=0; i<invSize; i++) {
          items.add(ItemIdentifier.generateIdentifier("Air", ItemTier.COMMON, 1));
        }

        // Fetch the items
        SwordCraftOnline.logDebug("Fetching items for Inventory " + inventoryId + " with size " + invSize);
        PreparedStatement stmt_select_items = con.prepareStatement("SELECT `slot`, `item_identifier`, `tier`, `quantity`, `custom_item_data` FROM `inventory_item` WHERE `inventory_id`=?");
        stmt_select_items.setLong(1, inventoryId);
        res = stmt_select_items.executeQuery();
        
        // Generate item list
        while(res.next()) {
          ItemIdentifier itemIdentifier = ItemIdentifier.generateIdentifier(res.getString("item_identifier"), ItemTier.valueOf(res.getString("tier")), res.getInt("quantity"));
          if (itemIdentifier instanceof CustomDataHolder) {
            JsonObject customData = new JsonParser().parse(res.getString("custom_item_data")).getAsJsonObject();
            ((CustomDataHolder) itemIdentifier).setCustomData(customData);
            //SwordCraftOnline.logDebug("Set custom data of: " + itemIdentifier.getName() + ", to: " + customData.toString());
          }

          items.set(res.getInt("slot"), itemIdentifier);
        }
        stmt_select_items.close();

        SwordCraftOnline.logDebug("Succesfully fetched inventory " + inventoryId);
        return Collections.unmodifiableList(this.items);

      } catch (SQLException ex) {
        SwordCraftOnline.logSevere("A database error occured while attempting to load inventory " + inventoryId);
        ex.printStackTrace();
        throw new CompletionException(ex);
      } catch (RuntimeException ex) {
        SwordCraftOnline.logSevere("Sucesfully loaded inventory " + inventoryId + ", but an error occured while parsing the inventory. It may be corrupt.");
        ex.printStackTrace();
        throw new CompletionException(ex);
      }
    };

    return CompletableFuture.supplyAsync(supplier);
  }
}