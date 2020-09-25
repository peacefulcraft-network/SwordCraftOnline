package net.peacefulcraft.sco.storage.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.bukkit.scheduler.BukkitRunnable;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.inventories.InventoryType;

public class InventoryRegistryLookupTask extends BukkitRunnable {
  
  private long registryId;

  private HashMap<InventoryType, Long> inventoryIds;
    public HashMap<InventoryType, Long> getInventoryIds() { return inventoryIds; }

  private CompletableFuture<Map<InventoryType, Long>> promise;
    public CompletableFuture<Map<InventoryType, Long>> getCompletableFuture() { return this.promise; }

  public InventoryRegistryLookupTask(long registryId) {
    this.registryId = registryId;
    this.promise = new CompletableFuture<Map<InventoryType, Long>>();
  }

  @Override
  public void run() {
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

      this.promise.complete(Collections.unmodifiableMap(this.inventoryIds));
    } catch(SQLException ex)  {
      ex.printStackTrace();
      SwordCraftOnline.logSevere("An error occured while executing InventoryRegistryLookupTask for entity " + registryId);
      this.promise.completeExceptionally(ex);
    }
  }
}