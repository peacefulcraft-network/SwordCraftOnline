package net.peacefulcraft.sco;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import net.peacefulcraft.sco.commands.QuestMessager;
import net.peacefulcraft.sco.commands.SCOAdmin;
import net.peacefulcraft.sco.commands.partyCommands;
import net.peacefulcraft.sco.commands.setTeleport;
import net.peacefulcraft.sco.commands.setWaystone;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.PartyManager;
import net.peacefulcraft.sco.gamehandle.dungeon.DungeonManager;
import net.peacefulcraft.sco.gamehandle.listeners.DuelMoveListener;
import net.peacefulcraft.sco.gamehandle.listeners.EnterDungeon;
import net.peacefulcraft.sco.gamehandle.listeners.JoinGameListener;
import net.peacefulcraft.sco.gamehandle.listeners.ModifierUserDamageListener;
import net.peacefulcraft.sco.gamehandle.listeners.QuitGameListener;
import net.peacefulcraft.sco.gamehandle.listeners.RegionCheckListener;
import net.peacefulcraft.sco.gamehandle.listeners.RegionDamageListener;
import net.peacefulcraft.sco.gamehandle.listeners.SCOPlayerDamageListener;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.gamehandle.regions.RegionManager;
import net.peacefulcraft.sco.inventories.listeners.InventoryActions;
import net.peacefulcraft.sco.inventories.listeners.MerchantListeners;
import net.peacefulcraft.sco.mythicmobs.adapters.BukkitServer;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.ServerInterface;
import net.peacefulcraft.sco.mythicmobs.drops.DropManager;
import net.peacefulcraft.sco.mythicmobs.listeners.CentipedeDamage;
import net.peacefulcraft.sco.mythicmobs.listeners.HealthBarUpdate;
import net.peacefulcraft.sco.mythicmobs.listeners.MobOptions;
import net.peacefulcraft.sco.mythicmobs.listeners.MobSpawnHandler;
import net.peacefulcraft.sco.mythicmobs.listeners.MythicMobDeathEvent;
import net.peacefulcraft.sco.mythicmobs.mobs.MobManager;
import net.peacefulcraft.sco.mythicmobs.spawners.SpawnerManager;
import net.peacefulcraft.sco.particles.EffectManager;
import net.peacefulcraft.sco.quests.QuestManager;
import net.peacefulcraft.sco.quests.listeners.NPCActivateListener;
import net.peacefulcraft.sco.quests.listeners.QuestEntityDamageListener;
import net.peacefulcraft.sco.quests.listeners.QuestOpenInventoryListener;
import net.peacefulcraft.sco.quests.listeners.QuestPlayerInteractEntityListener;
import net.peacefulcraft.sco.quests.listeners.QuestPlayerMoveListener;
import net.peacefulcraft.sco.storage.HikariManager;
import net.peacefulcraft.sco.storage.SwordSkillRegistery;
import net.peacefulcraft.sco.swordskills.utilities.DirectionalUtil;
import net.peacefulcraft.sco.swordskills.listeners.AbilityAsyncPlayerChatListener;
import net.peacefulcraft.sco.swordskills.listeners.AbilityClickListener;
import net.peacefulcraft.sco.swordskills.listeners.AbilityEntityDamageEntityListener;
import net.peacefulcraft.sco.swordskills.listeners.AbilityEntityTeleportListener;
import net.peacefulcraft.sco.swordskills.listeners.AbilityPlayerDeathListener;
import net.peacefulcraft.sco.swordskills.listeners.AbilityPlayerMoveListener;
import net.peacefulcraft.sco.swordskills.listeners.AbilityPlayerRespawnListener;

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

	private static EffectManager effectManager;
		public static EffectManager getEffectManager() { return effectManager; }

	private SpawnerManager spawnerManager;
		public SpawnerManager getSpawnerManager() { return this.spawnerManager; }

	private QuestManager questManager;
		public QuestManager getQuestManager() { return this.questManager; }

	private RegionManager regionManager;
		public RegionManager getRegionManager() { return this.regionManager; }
	
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
		this.spawnerManager = new SpawnerManager();

		this.regionManager = new RegionManager();
		this.questManager = new QuestManager();

		effectManager = new EffectManager(this);
		
		this.getLogger().info("Sword Craft Online has been enabled!");
	}
	
	public void onDisable() {
		this.getLogger().info("Disabling player inventories...");
		HashMap<UUID, SCOPlayer> players = GameManager.getPlayers();
		for(UUID u : players.keySet()) {
			players.get(u).playerDisconnect();
		}
		this.getLogger().info("Player inventories disabled.");
		
		hikari.close();
		this.getLogger().info("Database connection closed.");

		effectManager.dispose();
		this.getLogger().info("Effect Manager disposed.");

		this.spawnerManager.save();
		this.mobManager.save();
		
		this.saveConfig();
		this.getLogger().info("Sword Craft Online has been disabled!");
	}
	
	private void loadCommands() {
		this.getCommand("setWaystone").setExecutor(new setWaystone());
		this.getCommand("setTeleport").setExecutor(new setTeleport());
		this.getCommand("party").setExecutor(new partyCommands());
		this.getCommand("scoadmin").setExecutor(new SCOAdmin());
		this.getCommand("questmessager").setExecutor(new QuestMessager());
		
	}
	
	private void loadEventListeners() {

		// Game Handle Listeners
		getServer().getPluginManager().registerEvents(new JoinGameListener(), this);
		getServer().getPluginManager().registerEvents(new QuitGameListener(), this);
		getServer().getPluginManager().registerEvents(new RegionCheckListener(), this);
		getServer().getPluginManager().registerEvents(new RegionDamageListener(), this);
		getServer().getPluginManager().registerEvents(new SCOPlayerDamageListener(), this);
		getServer().getPluginManager().registerEvents(new EnterDungeon(), this);
		getServer().getPluginManager().registerEvents(new DuelMoveListener(), this);
		getServer().getPluginManager().registerEvents(new ModifierUserDamageListener(), this);
		
		//Mythicmob listeners
		getServer().getPluginManager().registerEvents(new MythicMobDeathEvent(), this);
		getServer().getPluginManager().registerEvents(new MobOptions(), this);
		getServer().getPluginManager().registerEvents(new MobSpawnHandler(), this);
		getServer().getPluginManager().registerEvents(new CentipedeDamage(), this);
		getServer().getPluginManager().registerEvents(new HealthBarUpdate(), this);
		
		// Register Menu Opener
		getServer().getPluginManager().registerEvents(new InventoryActions(), this);
		getServer().getPluginManager().registerEvents(new MerchantListeners(), this);

		//SwordSkill Util Listeners
		getServer().getPluginManager().registerEvents(new DirectionalUtil(), this);

		/**
		 * The Inventories manage their own event listeners
		 */
	
		// Register ability listeners
		getServer().getPluginManager().registerEvents(new AbilityAsyncPlayerChatListener(), this);
		getServer().getPluginManager().registerEvents(new AbilityClickListener(), this);
		getServer().getPluginManager().registerEvents(new AbilityEntityDamageEntityListener(), this);
		getServer().getPluginManager().registerEvents(new AbilityEntityTeleportListener() ,this);
		getServer().getPluginManager().registerEvents(new AbilityPlayerDeathListener(), this);
		getServer().getPluginManager().registerEvents(new AbilityPlayerMoveListener(),this);
		getServer().getPluginManager().registerEvents(new AbilityPlayerRespawnListener(), this);

		//Register Quest listeners
		getServer().getPluginManager().registerEvents(new NPCActivateListener(), this);
		getServer().getPluginManager().registerEvents(new QuestEntityDamageListener(), this);
		getServer().getPluginManager().registerEvents(new QuestOpenInventoryListener(), this);
		getServer().getPluginManager().registerEvents(new QuestPlayerInteractEntityListener(), this);
		getServer().getPluginManager().registerEvents(new QuestPlayerMoveListener(), this);

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
