package net.peacefulcraft.sco.gamehandle.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.peacefulcraft.sco.SwordCraftOnline;

public class QuitGameListener implements Listener
{
	@EventHandler
	public void quitGame(PlayerQuitEvent e) {
		SwordCraftOnline.getGameManager().leaveGame(e.getPlayer());
	}
}
