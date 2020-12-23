package net.peacefulcraft.sco.gamehandle.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.duel.Duel;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

public class DuelMoveListener implements Listener {
    
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        //Has player moved
        if(e.getFrom().distanceSquared(e.getTo()) == 0) { return; }

        Player p = e.getPlayer();
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) { return; }

        if(s.isInDuel()) {
            Duel d = s.getDuel();
            if(d.isInSetup()) {
                e.setCancelled(true);
            }
        }
    }

}