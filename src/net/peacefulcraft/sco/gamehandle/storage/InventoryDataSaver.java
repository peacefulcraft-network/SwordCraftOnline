package net.peacefulcraft.sco.gamehandle.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.inventory.Inventory;

import net.peacefulcraft.sco.inventories.InventoryBase;

public class InventoryDataSaver {
	
	private static Connection mysql = null;
	
	public InventoryDataSaver() {
		
	}
	
	public static void onEnable() {
		String ip = "";
		String name = "";
		String username = "";
		String password = "";
		String dbUrl = "jdbc:mysql//" + ip + ":3306/" + name;
		
		try {
			mysql = DriverManager.getConnection(dbUrl, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void onDisable() {
		try {
			if(mysql != null && mysql.isClosed() != true) {
				mysql.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Store inventory data in persistent storage
	 * @param inv: The Inventory
	 * @throws RuntimeException: Inventory fails to save
	 */
	public void saveInventory(Inventory inv) throws RuntimeException {
		//TODO: Save the inventory to persistent storage
		return;
	}
	
	/**
	 * Load inventory data from persistent storage and instantiate an Inventory object
	 * @param
	 */
	public <T extends InventoryBase> T loadInventory(UUID player, Class<T> type) throws RuntimeException{
		//TODO: Load the inventory from persistent storage
		return null;
	}
}
