package net.peacefulcraft.sco.utilities;

import org.bukkit.Location;

/**
 * Central class for teleportation utilities
 * 
 */
public class TeleportUtil {
    
    /**
     * Determines if location is safe place to teleport
     * @return True if safe. False otherwise
     */
    public static boolean safeTeleport(Location location) {
        
        if(location.getBlock().getType().isSolid()){
            if(location.add(0, 1, 0).getBlock().getType().isSolid()) {
                return false;
            }
        }
        Location head = location.clone().add(0, 1, 0);
        if(head.getBlock().getType().isSolid()) {
            return false;
        }
        Location ground = location.clone().subtract(0, 1, 0);
        if(!ground.getBlock().getType().isSolid()) {
            return false;
        }

        return true;

    }

    /**Iterates nearby locations to find safe location. */
    public static void findSafeTeleport(Location loc) {
        //TODO:
        return;
    }
}