package net.peacefulcraft.sco.storage.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Supplier;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.inventories.InventoryType;

public class InventoryRegistryLookupTask {
  
  private long registryId;

  private HashMap<InventoryType, Long> inventoryIds;
    public HashMap<InventoryType, Long> getInventoryIds() { return inventoryIds; }

  public InventoryRegistryLookupTask(long registryId) {
    this.registryId = registryId;
  }

  public CompletableFuture<Map<InventoryType, Long>> fetchInventoryIds() {
    Supplier<Map<InventoryType, Long>> supplier = () -> {
      try (
        Connection con = SwordCraftOnline.getHikariPool().getConnection();
      ) {
        PreparedStatement stmt_select = con.prepareStatement("SELECT `id`,`type` FROM `inventory` WHERE `player_id`=?");
        stmt_select.setLong(1, registryId);
        ResultSet res = stmt_select.executeQuery();

        inventoryIds = new HashMap<InventoryType, Long>();
        while(res.next()) {
          inventoryIds.put(InventoryType.valueOf(res.getString("type")), res.getLong("id"));
        }

        return Collections.unmodifiableMap(this.inventoryIds);

      } catch(SQLException ex)  {
        ex.printStackTrace();
        SwordCraftOnline.logSevere("An error occured while executing InventoryRegistryLookupTask for entity " + registryId);
        throw new CompletionException(ex);
      }
    };

    return CompletableFuture.supplyAsync(supplier);
  }
}