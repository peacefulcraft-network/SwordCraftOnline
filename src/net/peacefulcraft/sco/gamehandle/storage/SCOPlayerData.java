package net.peacefulcraft.sco.gamehandle.storage;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

import net.peacefulcraft.sco.SwordCraftOnline;

public class SCOPlayerData
{
	private SwordCraftOnline plugin;
	private File playerFile;
	private FileConfiguration playerConfig;
	
	public SCOPlayerData() {
		
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
