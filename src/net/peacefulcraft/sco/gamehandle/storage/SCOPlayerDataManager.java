package net.peacefulcraft.sco.gamehandle.storage;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.InventoryManager;

public class SCOPlayerDataManager {
	
	private SCOPlayer s;
	
	private SwordCraftOnline plugin;
	private File playerFile;
	private FileConfiguration playerConfig;
	
	private InventoryManager inventories;
		public InventoryManager getInventories() { return inventories; }
	
	public SCOPlayerDataManager(SCOPlayer s) {
		this.s = s;
		inventories = new InventoryManager(this);
	}
	
	public void createDefaultSections() {
		
	}
	
	public File getPlayerFile() {
		return playerFile;
	}
	
	public FileConfiguration getPlayerConfig() {
		return playerConfig;
	}
	
}
