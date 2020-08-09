package net.peacefulcraft.sco.gamehandle.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

/**
 * Main death logic for SCOPlayers
 */
public class SCOPlayerDeathListener implements Listener {
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerDeath(EntityDamageEvent e) {
        Entity ent = e.getEntity();
        if(!(ent instanceof Player)) { return; }

        Player p = (Player)ent;
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) { return; }

        //If player died
        if(p.getHealth() - e.getFinalDamage() <= 0) {
            if(s.isInDuel()) {
                e.setCancelled(true);
            }
        }
    }

}