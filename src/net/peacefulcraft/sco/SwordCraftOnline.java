package net.peacefulcraft.sco;

import org.bukkit.plugin.java.JavaPlugin;

import net.peacefulcraft.sco.commands.setTeleport;
import net.peacefulcraft.sco.commands.setWaystone;
import net.peacefulcraft.sco.gamehandle.listeners.JoinGameListener;
import net.peacefulcraft.sco.gamehandle.player.GameManager;
import net.peacefulcraft.sco.inventories.listeners.SwordSkillInventory;

public class SwordCraftOnline extends JavaPlugin{

	public static SwordCraftOnline sao;
		public static SwordCraftOnline getPluginInstance() { return sao; }
	
	public static SCOConfig cfg;
		public static SCOConfig getSCOConfig() { return cfg; }
		
	public static GameManager gameManager;
		public static GameManager getGameManager() {return gameManager;}
		
	public SwordCraftOnline() {

		sao = this;
		cfg = new SCOConfig(getConfig());
		
	}
	
	public void onEnable() {
		this.saveDefaultConfig();
		
		this.loadCommands();
		this.loadEventListeners();
		
		gameManager = new GameManager();
		
		this.getLogger().info("Sword Craft Online has been enabled!");
		
	}
	
	public void onDisable() {
		this.saveConfig();
		this.getLogger().info("Sword Craft Online has been disabled!");
	}
	
	private void loadCommands() {
		this.getCommand("setWaystone").setExecutor(new setWaystone());
		this.getCommand("setTeleport").setExecutor(new setTeleport());
		
	}
	
	private void loadEventListeners() {
		//Sword Skill Listeners
		
		//Game Handle Listeners
		getServer().getPluginManager().registerEvents(new JoinGameListener(), this);
		
		//Menu Listeners
		getServer().getPluginManager().registerEvents(new SwordSkillInventory(), this);
	}
	
}
