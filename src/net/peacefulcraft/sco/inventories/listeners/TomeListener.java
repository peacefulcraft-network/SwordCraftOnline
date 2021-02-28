package net.peacefulcraft.sco.inventories.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.ItemIdentifier;

public class TomeListener implements Listener {
    
    @EventHandler
    public void OpenTomeListener(PlayerInteractEvent ev) {
        if(!(ev.getAction().equals(Action.RIGHT_CLICK_AIR) || ev.getAction().equals(Action.RIGHT_CLICK_BLOCK))) { return; }
        SCOPlayer s = GameManager.findSCOPlayer(ev.getPlayer());
        if(s == null) { return; }

        ItemIdentifier identifier = ItemIdentifier.resolveItemIdentifier(ev.getItem());
        if(identifier == null || identifier.getMaterial().equals(Material.AIR)) { return; }

        if(!identifier.getName().equalsIgnoreCase("Sword Skill Tome")) { return; }

        s.getSwordSkillInventory().openInventory(s);
    }

}
