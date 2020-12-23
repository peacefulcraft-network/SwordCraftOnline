package net.peacefulcraft.sco.gamehandle.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.gamehandle.regions.Region;

public class RegionDamageListener implements Listener {
    
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent ev) {
        Entity vic = ev.getEntity();
        Entity damager = ev.getDamager();

        //If victim is player
        if(!(vic instanceof Player)) { return; }
        if(!(damager instanceof Player)) { return; }

        //If both players are in game
        SCOPlayer s1 = GameManager.findSCOPlayer((Player)vic);
        if(s1 == null) { return; }
        SCOPlayer s2 = GameManager.findSCOPlayer((Player)damager);
        if(s2 == null) { return; }

        Region r1 = s1.getRegion();

        //If victim is in region that prevents PVP
        if(r1 != null && r1.doesPreventPVP()) {
            ev.setCancelled(true);
        }
    }

    @EventHandler
    public void deathCancel(EntityDamageEvent e) {
        Entity vic = e.getEntity();

        //Is entity player
        if(!(vic instanceof Player)) { return; }
        Player p = (Player) vic;

        //Is player in the game
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) { return; }
        if(s.getRegion() == null) { return; }

        if(p.getHealth() - e.getFinalDamage() <= 0) {
            if(s.getRegion().doesPreventPlayerDeath()) {
                e.setCancelled(true);
            }
        }
    }
}