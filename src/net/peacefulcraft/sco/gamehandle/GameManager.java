package net.peacefulcraft.sco.gamehandle;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.gamehandle.player.Teleports;
import net.peacefulcraft.sco.items.utilities.SwordSkillTome;
import net.peacefulcraft.sco.items.utilities.TeleportCrystal;

public class GameManager {
	
	private static HashMap<UUID, SCOPlayer> players;
		public static HashMap<UUID, SCOPlayer> getPlayers(){ return players; }
		
	public GameManager() {
		players = new HashMap<UUID, SCOPlayer>();
		
	}
	
	public void joinGame(Player p) {
		
		if(findSCOPlayer(p) != null)
			throw new RuntimeException("Command executor is already in SCO");
		if(Teleports.getSpawn() == null)
			throw new RuntimeException("No teleport spawn set.");
		
		SCOPlayer s = new SCOPlayer(p);
		players.put(p.getUniqueId(), s);
		if(Teleports.getSpawn() == null) 
		p.teleport(Teleports.getSpawn());
		p.getInventory().setItem(8, (new SwordSkillTome().create()));
		p.getInventory().setItem(7, (new TeleportCrystal()).create());
		p.sendMessage("You have joined " + ChatColor.BLUE + "SwordCraftOnline");
	}
	
	public void leaveGame(Player p) {
		SCOPlayer s = findSCOPlayer(p.getPlayer());
		if(s == null) {
			throw new RuntimeException("Command executor is not playing SCO");
		}
		
		s.setLastInvite("");
		
		p.teleport(Teleports.getQuit());
		players.remove(p.getUniqueId());
		p.sendMessage("You have left " + ChatColor.BLUE + "SwordCraftOnline");
	}
	
	public static SCOPlayer findSCOPlayer(Player p) {
		return players.get(p.getUniqueId());
	}
	
	public static SCOPlayer findSCOPlayerByName(String name) {
		for(SCOPlayer p : players.values()) {
			if(p.getName().equalsIgnoreCase(name)) {
				return p;
			}
		}
		return null;
	}
}
