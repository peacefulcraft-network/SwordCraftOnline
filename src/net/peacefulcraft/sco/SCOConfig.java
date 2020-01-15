package net.peacefulcraft.sco;

import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

public class SCOConfig {

	private FileConfiguration c;
	
	private Map<String, Object> waystone_floor_1;
	private Map<String, Object> waystone_floor_2;
	private Map<String, Object> waystone_floor_3;
	
	private Map<String, Object> spawn;
	private Map<String, Object> quit;

	private Map<String, Object> dungeon_floor_1_entrance;
	private Map<String, Object> dungeon_floor_1_arena;
	
	private Map<String, Object> parties;
	
	private String db_ip = "";
	private String db_name = "";
	private String db_user = "";
	private String db_password = "";

	private boolean autoTarget = true;
	
	public SCOConfig(FileConfiguration c) {
		
		this.c = c;
		db_ip = (String) c.getString("database.ip");
		db_name = (String) c.getString("database.name");
		db_user = (String) c.getString("database.username");
		db_password = (String) c.getString("database.password");
		
		waystone_floor_1 = c.getConfigurationSection("waystones.1").getValues(false);
		waystone_floor_2 = c.getConfigurationSection("waystones.2").getValues(false);
		waystone_floor_3 = c.getConfigurationSection("waystones.3").getValues(false);
		spawn = c.getConfigurationSection("teleport.spawn").getValues(false);
		quit = c.getConfigurationSection("teleport.quit").getValues(false);

		dungeon_floor_1_entrance = c.getConfigurationSection("dungeon.1.entrance").getValues(false);
		dungeon_floor_1_arena = c.getConfigurationSection("dungeon.1.arena").getValues(false);

		parties = c.getConfigurationSection("parties").getValues(false);
	}
	
	public void setWaystone(Map<String, Object> waystone, int index) {
		
		switch(index) {
		case 1:
			this.waystone_floor_1 = waystone;
			c.set("waystones.1", waystone);
		break;case 2:
			this.waystone_floor_2 = waystone;
			c.set("waystones.2", waystone);
		break;case 3:
			this.waystone_floor_3 = waystone;
			c.set("waystones.3", waystone);
		}
	}
	
	public Map<String, Object> getWaystone(int index) {
		
		switch(index) {
		case 1:
			return waystone_floor_1;
		case 2:
			return waystone_floor_2;
		case 3:
			return waystone_floor_3;
		default:
			return waystone_floor_1;//Fix this
		}
	}
	
	/**Updates dungeon entrance config to Map object with index */
	public void setDungeonEntrance(Map<String, Object> entrance, int index) {
		
		switch(index) {
			case 1:
				this.dungeon_floor_1_entrance = entrance;
				c.set("dungeon.1.entrance", entrance);
			break;
		}
	}

	/**Returns dungeon entrance map object from config */
	public Map<String, Object> getDungeonEntrance(int index) {
		switch(index) {
		case 1:
			return dungeon_floor_1_entrance;
		default:
			return dungeon_floor_1_entrance;
		}
	}

	/**Updates dungeon arena config to Map object with index */
	public void setDungeonArena(Map<String, Object> arena, int index) {
		switch(index) {
			case 1:
				this.dungeon_floor_1_arena = arena;
				c.set("dungeon.1.arena", arena);
			break;
		}
	}

	/**Returns dungeon arena map object from config */
	public Map<String, Object> getDungeonArena(int index) {
		switch(index) {
		case 1:
			return dungeon_floor_1_arena;
		default:
			return dungeon_floor_1_arena;
		}
	}

	public void setSpawn(Map<String, Object> spawn) {
		this.spawn = spawn;
		c.set("teleport.spawn", spawn);
	}
	
	public Map<String, Object> getSpawn() {
		return spawn;
	}
	
	public void setQuit(Map<String, Object> quit) {
		this.quit = quit;
		c.set("teleport.quit", quit);
	}
	
	public Map<String, Object> getQuit() {
		return quit;
	}
	
	public void setParties(Map<String, Object> parties) {
		this.parties = parties;
		c.set("parties", this.parties);
	}
	
	public Map<String, Object> getParties() {
		return parties;
	}
	
	public void setParty(String name, Object party) {
		this.parties.put(name, party);
		c.set("parties", this.parties);
	}
	public Object getParty(String name) {
		return parties.get(name);
	}
	
	public void removeParty(String name) {
		this.parties.remove(name);
	}
	
	public String getDb_ip() {
		return db_ip;
	}
	
	public String getDb_name() {
		return db_name;
	}
	
	public String getDb_user() {
		return db_user;
	}
	
	public String getDb_password() {
		return db_password;
	}

	public void setAutoTarget(boolean b) {
		autoTarget = b;
	}

	public boolean autoTargetEnabled() {
		return autoTarget;
	}
}
