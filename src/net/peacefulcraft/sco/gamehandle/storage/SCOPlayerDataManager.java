package net.peacefulcraft.sco.gamehandle.storage;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.InventoryManager;

public class SCOPlayerDataManager {
	
	private SCOPlayer s;
		public SCOPlayer getSCOPlayer() { return s; }
	private FileConfiguration c;
	
	private InventoryManager inventories;
		public InventoryManager getInventories() { return inventories; }
	
	public SCOPlayerDataManager(SCOPlayer s) {
		this.s = s;
		inventories = new InventoryManager(this);
		
		initPlayerConfig();
		loadPlayerData();
	}
	
		/**
		 * Load the players' regular configuration file, if it exists
		 * If it does not exist, create it
		 */
		private void initPlayerConfig() {
			// Get file pointer
			File dataFile = new File(
				SwordCraftOnline.getPluginInstance().getDataFolder().getPath() + "/data/" + s.getUUID() + "/playerdata.yml"
			);
	
			if(dataFile.exists()) {
				// If the file exists, load it
				try {
					c.load(dataFile);
				} catch (IOException | InvalidConfigurationException e) {
					e.printStackTrace();
				}
				
			}else {
				// If the file doesn't exist, create it
				try {
					c.save(dataFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	/**
	 * Parse all the config values from the players' default playerdata.yml file
	 */
	public void loadPlayerData() {
		
	}
}
