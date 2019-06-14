package net.peacefulcraft.sco;

import org.bukkit.plugin.java.JavaPlugin;

import net.peacefulcraft.sco.commands.setWaystone;

public class SwordCraftOnline extends JavaPlugin{

	public static SwordCraftOnline sao;
		public static SwordCraftOnline getPluginInstance() { return sao; }
	
	public static SCOConfig cfg;
		public static SCOConfig getSCOConfig() { return cfg; }
		
	public SwordCraftOnline() {

		sao = this;
		cfg = new SCOConfig(getConfig());
		
	}
	
	public void onEnable() {
		this.saveDefaultConfig();
		
		this.loadCommands();
		this.loadEventListeners();
		
		this.getLogger().info("Sword Craft Online has been enabled!");
		
	}
	
	public void onDisable() {
		this.saveConfig();
		this.getLogger().info("Sword Craft Online has been disabled!");
	}
	
	private void loadCommands() {
		this.getCommand("setWaystone").setExecutor(new setWaystone());
	}
	
	private void loadEventListeners() {
		
	}
	
}
