package net.peacefulcraft.sao;

import org.bukkit.plugin.java.JavaPlugin;

public class SwordArtOnline extends JavaPlugin{

	public static SwordArtOnline sao;
		public static SwordArtOnline getPluginInstance() { return sao; }
	
	public static SAOConfig cfg;
		public static SAOConfig getPluginConfig() { return cfg; }
		
	public SwordArtOnline() {

		sao = this;
		cfg = new SAOConfig(getConfig());
		
	}
	
}
