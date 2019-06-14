package net.peacefulcraft.sco.gamehandle.player;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

public class DungeonManager {
	
	private static HashMap<UUID, SCOPlayer> players;
		public static Set<UUID> getPlayers() {return players.keySet();}
		
	public DungeonManager() {
		players = new HashMap<UUID, SCOPlayer>();
		
	}
	
	public SCOPlayer joinGame(Player p) {
		
		if(findSCOPlayer(p) != null)
			throw new RuntimeException("Command executor is already in SCO");
		
		SCOPlayer s = new SCOPlayer(p);
		players.put(p.getUniqueId(), s);
		return s;
	}
	
	public void leaveGame(Player p) {
		SCOPlayer s = findSCOPlayer(p.getPlayer());
		if(s == null) {
			throw new RuntimeException("Command executor is not playing SCO");
		}
		
		players.remove(p.getUniqueId());
	}
	
	public static SCOPlayer findSCOPlayer(Player p) {
		return players.get(p.getUniqueId());
	}
}
