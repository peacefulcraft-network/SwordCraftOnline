package net.peacefulcraft.sco.storage.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.bukkit.scheduler.BukkitRunnable;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.items.CustomDataHolder;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.storage.StorageTaskCallbackTask;
import net.peacefulcraft.sco.storage.StorageTaskOutcome;

public class InventoryLoadTask extends BukkitRunnable {

  private long inventoryId;
  private ItemIdentifier[] items;
    public ItemIdentifier[] getItems() { return items; }

  private StorageTaskCallbackTask callback;

  /**
   * @param inventoryId The ID of the inventory to load
   * @param Inventory The inventory 
   */
  public InventoryLoadTask(long inventoryId, StorageTaskCallbackTask callback) {
    this.inventoryId = inventoryId;
    this.callback = callback;
  }

  @Override
  public void run() {
    try (
      Connection con = SwordCraftOnline.getHikariPool().getConnection();
    ) {
      PreparedStatement stmt_select_inventory = con.prepareStatement("SELECT `size` FROM `inventory` WHERE `id`=?");
      stmt_select_inventory.setLong(1, inventoryId);
      ResultSet res = stmt_select_inventory.executeQuery();

      int invSize = 9;
      if (res.next()) {
        invSize = res.getInt(1);
        items = new ItemIdentifier[invSize];
      } else {
        SwordCraftOnline.logWarning("Attempted to load non-existent inventory " + inventoryId);
        callback.run(StorageTaskOutcome.SUCESS, new ArrayList<HashMap<String, Object>>());
        return;
      }

      PreparedStatement stmt_select_items = con.prepareStatement("SELECT `slot`, `item_identifier`, `tier`, `quantity`, `item_id`, `custom_item_data` FROM `inventory_item` WHERE `inventory_id`=?");
      stmt_select_items.setLong(1, inventoryId);
      res = stmt_select_items.executeQuery();
      
      while(res.next()) {
        ItemIdentifier itemIdentifier = ItemIdentifier.generateIdentifier(res.getString("item_identifier"), ItemTier.valueOf(res.getString("tier")));
        if (itemIdentifier instanceof CustomDataHolder) {
          JsonObject customData = new JsonParser().parse(res.getString("custom_item_data")).getAsJsonObject();
          ((CustomDataHolder) itemIdentifier).setCustomData(customData);
        }

        items[res.getInt("slot")] = itemIdentifier;
      }

    } catch (SQLException ex) {

    } catch (RuntimeException ex) {
      SwordCraftOnline.logSevere("Sucesfully loaded inventory " + inventoryId + ", but an error occured while parsing the inventory. It may be corrupt.");
    }
  }
}