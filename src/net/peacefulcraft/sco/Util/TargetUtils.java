package net.peacefulcraft.sco.Util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

public class TargetUtils {
   
    /**Max range within which to search for targets */
    private static final int MAX_DISTANCE = 256; 
    /**Max distance squared; comparing target distances */
    private static final double MAX_DISTANCE_SQ = MAX_DISTANCE * MAX_DISTANCE;

    /**
     * Returns player's reach distance based on gamemode.
     */
    public static double getReachDistanceSq(Player p) {
        return (p.getGameMode() == GameMode.CREATIVE) ? 36.0D : 12.0D;
    }

    public static double getDistanceSq(Entity p, Entity e) {
        double x = Math.pow((p.getLocation().getX() - e.getLocation().getX()), 2);
        double y = Math.pow((p.getLocation().getY() - e.getLocation().getY()), 2);
        double z = Math.pow((p.getLocation().getZ() - e.getLocation().getZ()), 2);
        return Math.sqrt(x + y + z);
    }

    public static double getDistanceSq(Player p, double x, double y,  double z) {
        double tempx = Math.pow((p.getLocation().getX() - x), 2);
        double tempy = Math.pow((p.getLocation().getY() - y), 2);
        double tempz = Math.pow((p.getLocation().getZ() - z), 2);
        return Math.sqrt(tempx + tempy + tempz);
    }

    public static boolean canReachTarget(Player p, Entity target) {
        return getDistanceSq(p, target) < getReachDistanceSq(p);
    }

    public static final boolean isTargetValid(Entity target, Entity seeker) {
        if(target == seeker) {
            return false;
        } else if(target.getVehicle() == seeker || seeker.getVehicle() == target) {
            return false;
        }
        return true;
    }

    public static final Entity acquireLookTarget(Entity seeker, int distance, double radius, boolean closestToSeeker) {
        double currentDistance = MAX_DISTANCE_SQ;
        Entity currentTarget = null;

        double targetX = seeker.getLocation().getX();
        double targetY = seeker.getLocation().getY();
        double targetZ = seeker.getLocation().getZ();
        
        List<Entity> list = seeker.getNearbyEntities(distance, distance, distance);
        for(Entity target : list) {
            //TODO:Add check isTargetInSight
            if(isTargetValid(target, seeker)) {
                double newDistance = getDistanceSq(seeker, target); 
                if(newDistance < currentDistance) {
                    currentTarget = target;
                    currentDistance = newDistance;
                }
            }
        }
        return currentTarget;
    }

    public static Entity getMouseOver(Player p) {
        List<Entity> nearby = p.getNearbyEntities(15, 15, 15);
        ArrayList<LivingEntity> livingE = new ArrayList<LivingEntity>();

        for(Entity e : nearby) {
            if(e instanceof LivingEntity) {
                livingE.add((LivingEntity) e);
            }
        }
        LivingEntity target = null;
        BlockIterator itr = new BlockIterator(p, 15);
        Block block;
        Location loc;
        int bx, by, bz;
        double ex, ey, ez;

        while(itr.hasNext()) {
            block = itr.next();
            bx = block.getX();
            by = block.getY();
            bz = block.getZ();
            for(LivingEntity e : livingE) {
                loc = e.getLocation();
                ex = loc.getX();
                ey = loc.getY();
                ez = loc.getZ();
                if((bx-0.75 <= ex && ex <= bx+1.75) && (bz-0.75 <= ez && ez <= bz+1.75) && (by-1 <= ey && ey <= by+2.5)) {
                    target = e;
                    break;
                }
            }
        }
        return (Entity) target;
    }
}