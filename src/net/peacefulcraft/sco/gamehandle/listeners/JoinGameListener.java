package net.peacefulcraft.sco.gamehandle.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.exceptions.SCOSQLRuntimeException;
import net.peacefulcraft.sco.storage.tasks.PlayerRegistryJoinGameSyncTask;

public class JoinGameListener implements Listener
{
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void preJoinEvent(AsyncPlayerPreLoginEvent e) {
		try {
			PlayerRegistryJoinGameSyncTask registryTask = new PlayerRegistryJoinGameSyncTask(e.getUniqueId(), e.getName());
			registryTask.run();
			
			SwordCraftOnline.getGameManager().preProcessPlayerJoin(e.getUniqueId());
		} catch(SCOSQLRuntimeException ex) {
			ex.printStackTrace();
		}
	}
	
	@EventHandler
	public void joinEvent(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		SwordCraftOnline.getGameManager().processPlayerJoin(p);
	}
}
