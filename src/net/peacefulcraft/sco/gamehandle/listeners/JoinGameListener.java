package net.peacefulcraft.sco.gamehandle.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

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
			
			SwordCraftOnline.getGameManager().preProcessPlayerJoin(e.getUniqueId(), registryTask.getPlayerRegistryId());
		} catch(SCOSQLRuntimeException ex) {
			ex.printStackTrace();
			e.setLoginResult(Result.KICK_OTHER);
			e.setKickMessage("A database error occured while loading your user profile. Please try again. If the issue persists, contact an administrator.");
		}
	}
	
	@EventHandler
	public void joinEvent(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		SwordCraftOnline.getGameManager().processPlayerJoin(p);
	}
}
