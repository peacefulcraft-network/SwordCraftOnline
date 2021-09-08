package net.peacefulcraft.sco.gamehandle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.gamehandle.player.Teleports;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;

public class GameManager {
	private static HashMap<UUID, SCOPlayer> preProcessedPlayers;

	private static HashMap<UUID, SCOPlayer> players;

	public static HashMap<UUID, SCOPlayer> getPlayers() {
		return players;
	}

	public GameManager() {
		preProcessedPlayers = new HashMap<UUID, SCOPlayer>();
		players = new HashMap<UUID, SCOPlayer>();
	}

	/**
	 * WARNING: This method should be invoked asynchronously. It performs serveral
	 * blocking MySQL queries that will cause server hangs if invoked on the main
	 * thread.
	 * @param uuid
	 * @param playerRegistryId
	 */
	public void preProcessPlayerJoin(UUID uuid, long playerRegistryId) throws RuntimeException {
		if (players.containsKey(uuid))
			throw new RuntimeException("Command executor is already in SCO");

		try {
			SCOPlayer s = new SCOPlayer(uuid, playerRegistryId);
			preProcessedPlayers.put(uuid, s);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
			if (e.getCause() != null) {
				e.getCause().printStackTrace();
			}
			throw new RuntimeException("An error occured while processing player " + uuid + " | " + playerRegistryId + "'s SCOPlayer initialization sequence.");
		}
	}
	
	public void processPlayerJoin(Player p) {
		
		if(Teleports.getSpawn() == null)
			throw new RuntimeException("No teleport spawn set.");
		
		SCOPlayer s = preProcessedPlayers.remove(p.getUniqueId());
		if(s == null) { 
			p.kickPlayer("[SCO] Database error. Unable to load profile from registry");
			return;
		}
		try {
			s.linkPlayer(p);
			// Register the Player's personal inventory with the InventoryEvent routing system
			SwordCraftOnline.getInventoryListeners().registerPlayerInventoryView(s, p.getOpenInventory());
		} catch (RuntimeException ex) {
			p.kickPlayer("[SCO] Database error occured while loading inventories.");
			ex.printStackTrace();
			return;
		} 

		players.put(p.getUniqueId(), s);

		p.getInventory().setItem(8, ItemIdentifier.generateItem("SwordSkillTome", ItemTier.COMMON, 1));
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
		
		// Unregister the Player's personal inventory from the InventoryEvent routing system
		SwordCraftOnline.getInventoryListeners().unregisterPlayerInventoryView(s, p.getOpenInventory());

		s.processLeave();

		p.teleport(Teleports.getQuit());
		players.remove(p.getUniqueId());

		p.sendMessage("You have left " + ChatColor.BLUE + "SwordCraftOnline");
	}
	
	public static SCOPlayer findSCOPlayer(Player p) {
		return players.get(p.getUniqueId());
	}
	
	public static SCOPlayer findSCOPlayer(String name) {
		for(SCOPlayer p : players.values()) {
			if(p.getName().equalsIgnoreCase(name)) {
				return p;
			}
		}
		return null;
	}

	public static List<SCOPlayer> findSCOPlayers(List<Player> lis) {
        List<SCOPlayer> output = new ArrayList<>();
        for(Player p : lis) {
            SCOPlayer s = GameManager.findSCOPlayer(p);
			if(s == null) { continue; }
			output.add(s);
		}
		return output;
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

	public static boolean isDay() {
		long time = SwordCraftOnline.getPluginInstance().getServer().getWorld("SwordCraftOnline").getTime();
		return time < 12300 || time > 23850;
	}
}
