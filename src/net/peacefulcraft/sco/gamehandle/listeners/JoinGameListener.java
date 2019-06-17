package net.peacefulcraft.sco.gamehandle.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.items.utilities.SwordSkillTome;

public class JoinGameListener implements Listener
{
	@EventHandler
	public void joinEvent(PlayerJoinEvent e) {
		Inventory inv = e.getPlayer().getInventory();
		ItemStack tome = (new SwordSkillTome()).create();
		if(!(inv.contains(tome))) {
			inv.setItem(8, tome);
		}
	}
}
