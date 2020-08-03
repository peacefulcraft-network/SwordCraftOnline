package net.peacefulcraft.sco.storage.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.scheduler.BukkitRunnable;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemRegistry;

public class SyncItemRegistryTask extends BukkitRunnable {

  private ItemRegistry localItems;

  public SyncItemRegistryTask(ItemRegistry localItems) {
    this.localItems = localItems;
  }

  @Override
  public void run() {
    try (
      Connection con = SwordCraftOnline.getHikariPool().getConnection();
      PreparedStatement stmt_select = con.prepareStatement("SELECT `id` FROM `item_registry` WHERE `name`=? AND `rarity`=?");
      PreparedStatement stmt_insert = con.prepareStatement("INSERT INTO `item_registry` (name, rarity) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
    ) {
      con.setAutoCommit(false);

      for (ItemIdentifier localItem : localItems.getLocalSkills()) {
        stmt_select.setString(1, localItem.getName());
        stmt_select.setString(2, localItem.getTier().toString().toUpperCase());
        ResultSet stmt_select_res = stmt_select.executeQuery();

        long databaseId = 0;
        if (stmt_select_res.next()) {
          databaseId = stmt_select_res.getLong(1);
        } else {
          stmt_insert.setString(1, localItem.getName());
          stmt_insert.setString(2, localItem.getTier().toString().toUpperCase());
          stmt_insert.executeUpdate();
          ResultSet stmt_insert_res = stmt_insert.getGeneratedKeys();
          if (stmt_insert_res.next()) {
            databaseId = stmt_insert_res.getLong(1);
          }
        }

        con.commit();

        if (databaseId == 0) {
          SwordCraftOnline.logSevere("Unable to register item " + localItem.getName() + " " + localItem.getTier());
        } else {
          localItem.setDatabaseID(databaseId);
        }
      }

      localItems.setInitialized();
    } catch (SQLException e) {
      e.printStackTrace();
      SwordCraftOnline.logSevere("An error occured while synchronizing the item registry with the database.");
    }
  }
}