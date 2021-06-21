package net.peacefulcraft.sco.gamehandle.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.storage.tasks.PlayerRegistryJoinGameTask;

public class JoinGameListener implements Listener
{
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void preJoinEvent(AsyncPlayerPreLoginEvent e) {
		try {
			PlayerRegistryJoinGameTask registryTask = new PlayerRegistryJoinGameTask(e.getUniqueId(), e.getName());
			Long playerRegistryId = registryTask.fetchPlayerRegistryId().get();
			
			SCOPlayer s = SwordCraftOnline.getGameManager().preProcessPlayerJoin(e.getUniqueId(), playerRegistryId);
			if (s != null) {
				s.getQuestBookManager().processCompletedQuests(registryTask.getPlayerCompletedQuests());
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			SwordCraftOnline.logSevere(ex.getMessage());
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
