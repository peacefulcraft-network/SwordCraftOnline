package net.peacefulcraft.sco.gamehandle.listeners;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.gamehandle.regions.Region;
import net.peacefulcraft.sco.gamehandle.regions.RegionManager;

public class RegionCheckListener implements Listener {
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRegionEnter(PlayerMoveEvent e) {
        //Has player moved
        if(e.getFrom().distanceSquared(e.getTo()) == 0) { return; }
        
        //Is player in the game
        Player p = e.getPlayer();
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) { return; }
        
        //Is players floor invalid
        if(s.getFloor() == 0) { return; }

        //Are there regions on the floor. Possible redundant check
        ArrayList<Region> regions = RegionManager.getFloorRegionsMap().get(s.getFloor());
        if(regions == null || regions.isEmpty()) { return; }

        Location locTo = e.getTo();
        for(Region r : regions){
            //If player has entered a new region
            if(s.getRegion() != r && r.isInRegion(locTo)) {
                
                // Fetching child if it exists
                Region child = r.getChild(locTo);
                if(child != null) {
                    s.setRegion(child, r.isSilentParentTransfer());
                } else {
                    s.setRegion(r, false);
                }
                return;
            }
        }

        //Player is not in any region
        s.setRegion(null, true);
    }

}