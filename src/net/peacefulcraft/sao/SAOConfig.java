package net.peacefulcraft.sao;

import org.bukkit.configuration.file.FileConfiguration;

public class SAOConfig {

	private FileConfiguration c;
	
	private String db_ip = "";
	private String db_name = "";
	private String db_user = "";
	private String db_password = "";
	
	public SAOConfig(FileConfiguration c) {
		
		this.c = c;
		db_ip = (String) c.getString("database.ip");
		db_name = (String) c.getString("database.name");
		db_user = (String) c.getString("database.username");
		db_password = (String) c.getString("database.password");
		
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
