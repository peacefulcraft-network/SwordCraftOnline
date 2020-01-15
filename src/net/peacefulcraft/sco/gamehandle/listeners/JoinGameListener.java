package net.peacefulcraft.sco.gamehandle.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.gamehandle.tasks.LoadPlayerInventory;
import net.peacefulcraft.sco.inventory.InventoryType;

public class JoinGameListener implements Listener
{
	@EventHandler
	public void joinEvent(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		SwordCraftOnline.getGameManager().joinGame(p);
		
		@SuppressWarnings("static-access")
		SCOPlayer s = SwordCraftOnline.getGameManager().findSCOPlayer(p);
		(new LoadPlayerInventory(s, InventoryType.ACTIVE_SKILL)).runTaskAsynchronously(SwordCraftOnline.getPluginInstance());
		// (new LoadPlayerInventory(s, InventoryType.MAIN_INVENTORY)).runTaskAsynchronously(SwordCraftOnline.getPluginInstance());
	}
}
