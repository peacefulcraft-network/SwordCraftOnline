package net.peacefulcraft.sco.gamehandle;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.gamehandle.player.Teleports;
import net.peacefulcraft.sco.items.customitems.TeleportCrystal;
import net.peacefulcraft.sco.items.utilities.SwordSkillTome;
import net.peacefulcraft.sco.mythicmobs.adapters.BukkitAdapter;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractLocation;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractPlayer;

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
		if(Teleports.getSpawn() != null) 
		
		//Set to teleport player to floor.
		p.teleport(Teleports.getWaystone(s.getFloor()));
		p.getInventory().setItem(8, (new SwordSkillTome().create()));
		p.getInventory().setItem(7, (new TeleportCrystal()).create(1));
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

		p.getInventory().clear(p.getInventory().first((new SwordSkillTome().create())));

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

	public Set<AbstractPlayer> getPlayersInRangeSq(AbstractLocation location, int rangeSq) {
		Set<AbstractPlayer> inRange = new HashSet<>();
		try {
			for(SCOPlayer s : players.values()) {
				if(s.getPlayer().getLocation().distanceSquared(BukkitAdapter.adapt(location)) <= rangeSq) {
					inRange.add(BukkitAdapter.adapt(s.getPlayer()));
				}
			}
			return inRange;
		} catch(Exception ex) {
			return Collections.emptySet();
		}
	}
}
