package net.peacefulcraft.sco.gamehandle;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.gamehandle.player.Teleports;
import net.peacefulcraft.sco.items.utilities.SwordSkillTome;
import net.peacefulcraft.sco.mythicmobs.adapters.BukkitAdapter;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractLocation;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractPlayer;

public class GameManager {
	private static HashMap<UUID, SCOPlayer> preProcessedPlayers;
	
	private static HashMap<UUID, SCOPlayer> players;
		public static HashMap<UUID, SCOPlayer> getPlayers(){ return players; }
		
	public GameManager() {
		preProcessedPlayers = new HashMap<UUID, SCOPlayer>();
		players = new HashMap<UUID, SCOPlayer>();
	}
	
	public void preProcessPlayerJoin(UUID uuid) {
		if(findSCOPlayerByUUID(uuid) != null)
			throw new RuntimeException("Command executor is already in SCO");
		
		SCOPlayer s = new SCOPlayer(uuid);
		preProcessedPlayers.put(uuid, s);
	}
	
	public void processPlayerJoin(Player p) {
		
		if(Teleports.getSpawn() == null)
			throw new RuntimeException("No teleport spawn set.");
		
		SCOPlayer s = preProcessedPlayers.remove(p.getUniqueId());
		if(s == null) { 
			p.kickPlayer("[SCO] Database error. Unable to load profile from registry");
			return;
		}
		s.linkPlayer(p);
		players.put(p.getUniqueId(), s);

		p.getInventory().setItem(8, (new SwordSkillTome().create()));
		p.sendMessage("You have joined " + ChatColor.BLUE + "SwordCraftOnline");

		if(Teleports.getSpawn() == null) {
			return; 
		}
		
		//Set to teleport player to floor.
		try {
			p.teleport(Teleports.getWaystone(s.getFloor()));
		} catch(Exception e) {
			SwordCraftOnline.logInfo("Failed to teleport player " + s.getName() + " to floor " + s.getFloor() + ". Sending to spawn.");
			p.teleport(Teleports.getSpawn());
		}
	}
	
	public void leaveGame(Player p) {
		SCOPlayer s = findSCOPlayer(p.getPlayer());
		if(s == null) {
			throw new RuntimeException("Command executor is not playing SCO");
		}
		
		p.teleport(Teleports.getQuit());
		players.remove(p.getUniqueId());

		p.getInventory().clear(p.getInventory().first((new SwordSkillTome().create())));

		p.sendMessage("You have left " + ChatColor.BLUE + "SwordCraftOnline");
	}
	
	public static SCOPlayer findSCOPlayer(Player p) {
		return findSCOPlayerByUUID(p.getUniqueId());
	}
	
	public static SCOPlayer findSCOPlayerByUUID(UUID uuid) {
		return players.get(uuid);
	}
	
	public static SCOPlayer findSCOPlayerByName(String name) {
		for(SCOPlayer p : players.values()) {
			if(p.getName().equalsIgnoreCase(name)) {
				return p;
			}
		}
		return null;
	}

	public Set<Player> getPlayersInRangeSq(Location location, int rangeSq) {
		Set<Player> inRange = new HashSet<>();
		try {
			for(SCOPlayer s : players.values()) {
				if(s.getPlayer().getLocation().distanceSquared(location) <= rangeSq) {
					inRange.add(s.getPlayer());
				}
			}
			return inRange;
		} catch(Exception ex) {
			return Collections.emptySet();
		}
	}
}
