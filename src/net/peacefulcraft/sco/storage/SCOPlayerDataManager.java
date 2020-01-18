package net.peacefulcraft.sco.storage;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.InventoryManager;

public class SCOPlayerDataManager {
	
	private SCOPlayer s;
		public SCOPlayer getSCOPlayer() { return s; }
	
	File dataFile;
	private FileConfiguration c;
	
	private InventoryManager inventories;
		public InventoryManager getInventories() { return inventories; }
	
	public SCOPlayerDataManager(SCOPlayer s) {
		this.s = s;
		dataFile = new File(
			SwordCraftOnline.getPluginInstance().getDataFolder().getPath() + "/data/" + s.getUUID() + "/playerdata.yml"
		);
		
		inventories = new InventoryManager(this);
		c = new YamlConfiguration();
		
		initPlayerConfig();
		loadPlayerData();
	}
	
		/**
		 * Load the players' regular configuration file, if it exists
		 * If it does not exist, create it
		 */
		private void initPlayerConfig() {
	
			if(dataFile.exists()) {
				// If the file exists, load it
				try {
					c.load(dataFile);
				} catch (IOException | InvalidConfigurationException e) {
					e.printStackTrace();
				}
				
			}else {
				saveConfig();
			}
		}
		
	public void saveConfig() {
		try {
			c.save(dataFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Parse all the config values from the players' default playerdata.yml file
	 */
	public void loadPlayerData() {
		
	}
}
