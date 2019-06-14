package net.peacefulcraft.sco;

import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

public class SCOConfig {

	private FileConfiguration c;
	
	private Map<String, Object> waystone_floor_1;
	private Map<String, Object> waystone_floor_2;
	private Map<String, Object> waystone_floor_3;
	
	private String db_ip = "";
	private String db_name = "";
	private String db_user = "";
	private String db_password = "";
	
	public SCOConfig(FileConfiguration c) {
		
		this.c = c;
		db_ip = (String) c.getString("database.ip");
		db_name = (String) c.getString("database.name");
		db_user = (String) c.getString("database.username");
		db_password = (String) c.getString("database.password");
		
		waystone_floor_1 = c.getConfigurationSection("waystones.1").getValues(false);
		waystone_floor_2 = c.getConfigurationSection("waystones.2").getValues(false);
		waystone_floor_3 = c.getConfigurationSection("waystones.3").getValues(false);
	}
	
	public void setWaystone(Map<String, Object> waystone, int index) {
		
		switch(index) {
		case 1:
			this.waystone_floor_1 = waystone;
			c.set("waystones.1", waystone);
		case 2:
			this.waystone_floor_2 = waystone;
			c.set("waystones.2", waystone);
		case 3:
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

}
