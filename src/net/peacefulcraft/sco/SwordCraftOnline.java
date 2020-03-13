package net.peacefulcraft.sco;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import net.peacefulcraft.sco.commands.SCOAdmin;
import net.peacefulcraft.sco.commands.partyCommands;
import net.peacefulcraft.sco.commands.setTeleport;
import net.peacefulcraft.sco.commands.setWaystone;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.PartyManager;
import net.peacefulcraft.sco.gamehandle.dungeon.DungeonManager;
import net.peacefulcraft.sco.gamehandle.listeners.EnterDungeon;
import net.peacefulcraft.sco.gamehandle.listeners.ItemDropOnDeath;
import net.peacefulcraft.sco.gamehandle.listeners.JoinGameListener;
import net.peacefulcraft.sco.gamehandle.listeners.MobTarget;
import net.peacefulcraft.sco.gamehandle.listeners.MythicMobDeathEvent;
import net.peacefulcraft.sco.gamehandle.listeners.QuitGameListener;
import net.peacefulcraft.sco.gamehandle.listeners.TestDamageEvent;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.listeners.InventoryActions;
import net.peacefulcraft.sco.mythicmobs.adapters.BukkitServer;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.ServerInterface;
import net.peacefulcraft.sco.mythicmobs.drops.DropManager;
import net.peacefulcraft.sco.mythicmobs.mobs.MobManager;
import net.peacefulcraft.sco.storage.HikariManager;
import net.peacefulcraft.sco.storage.SwordSkillRegistery;
import net.peacefulcraft.sco.swordskills.utilities.DirectionalUtil;

public class SwordCraftOnline extends JavaPlugin{

	public static SwordCraftOnline sco;
		public static SwordCraftOnline getPluginInstance() { return sco; }
	
	public static SCOConfig cfg;
		public static SCOConfig getSCOConfig() { return cfg; }
		public static Boolean showDebug() { return cfg.getDebug(); }
		
	public static HikariManager hikari;
		public static HikariManager getHikariPool() { return hikari; }
		
	public static GameManager gameManager;
		public static GameManager getGameManager() {return gameManager;}
		
	public static PartyManager partyManager;
		public static PartyManager getPartyManager() {return partyManager;}

	public static DungeonManager dungeonManager;
		public static DungeonManager getDungeonManager() { return dungeonManager; }

	public static Random r;

	private ServerInterface server;
		public ServerInterface server() { return this.server; }

	private DropManager dropManager;
		public DropManager getDropManager() { return this.dropManager; }
		
	private MobManager mobManager;
		public MobManager getMobManager() { return this.mobManager; }
	
	public SwordCraftOnline() {

		sco = this;
		cfg = new SCOConfig(getConfig());

		r = new Random();
		
		
	}
	
	public void onEnable() {
		this.saveDefaultConfig();
		
		this.loadCommands();
		this.loadEventListeners();
		
		hikari = new HikariManager(cfg);
		new SwordSkillRegistery();
		gameManager = new GameManager();
		partyManager = new PartyManager();
		

		dungeonManager = new DungeonManager();

		this.server = (ServerInterface)new BukkitServer();
		this.dropManager = new DropManager(this);
		this.mobManager = new MobManager(this);
		
		this.getLogger().info("Sword Craft Online has been enabled!");
	}
	
	public void onDisable() {
		this.getLogger().info("Disabling player inventories...");
		HashMap<UUID, SCOPlayer> players = gameManager.getPlayers();
		for(UUID u : players.keySet()) {
			players.get(u).playerDisconnect();
		}
		this.getLogger().info("Player inventories disabled.");
		
		hikari.close();
		this.getLogger().info("Database connection closed.");
		
		this.saveConfig();
		this.getLogger().info("Sword Craft Online has been disabled!");
	}
	
	private void loadCommands() {
		this.getCommand("setWaystone").setExecutor(new setWaystone());
		this.getCommand("setTeleport").setExecutor(new setTeleport());
		this.getCommand("party").setExecutor(new partyCommands());
		this.getCommand("scoadmin").setExecutor(new SCOAdmin());
		
	}
	
	private void loadEventListeners() {

		//Game Handle Listeners
		getServer().getPluginManager().registerEvents(new JoinGameListener(), this);
		getServer().getPluginManager().registerEvents(new QuitGameListener(), this);
		getServer().getPluginManager().registerEvents(new ItemDropOnDeath(), this);
		getServer().getPluginManager().registerEvents(new EnterDungeon(), this);
		getServer().getPluginManager().registerEvents(new MythicMobDeathEvent(), this);
		getServer().getPluginManager().registerEvents(new MobTarget(), this);
		
		//Register Menu Opener
		getServer().getPluginManager().registerEvents(new InventoryActions(), this);

		//SwordSkill Util Listeners
		getServer().getPluginManager().registerEvents(new DirectionalUtil(), this);

		/**
		 * The Inventories manage their own event listeners
		 */
	}
	
	public static void logDebug(String debug) {
		if(showDebug()) {
			sco.getLogger().log(Level.INFO, debug);
		}
	}
	
	public static void logInfo(String info) {
		sco.getLogger().log(Level.INFO, info);
	}
	
	public static void logWarning(String warning) {
		sco.getLogger().log(Level.WARNING, warning);
	}
	
	public static void logSevere(String severe) {
		sco.getLogger().log(Level.SEVERE, severe);
	}
	
}
