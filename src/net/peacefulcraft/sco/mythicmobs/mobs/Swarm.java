package net.peacefulcraft.sco.mythicmobs.mobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.utilities.LocationUtil;

/**
 * Swarm class
 * 
 * Allows for spawning multiple instances of a group of mobs. 
 * All targeting one player.
 * 
 * Meant to be used once then deleted or removed from scope.
 */
public class Swarm {
    
    private List<String> mobNames;

    private int size;
        public void setSize(int i) { this.size = i; }

    private LivingEntity target;

    private HashMap<UUID, ActiveMob> mobs;

    private boolean spawned = false;

    /**
     * Constructs swarm with target and mythic mob type
     * @param type
     * @param size
     * @param target
     */
    public Swarm(String mobName, int size, LivingEntity target) {
        this.mobNames = new ArrayList<String>();
        mobNames.add(mobName);
        this.size = size;
        this.target = target;
        this.mobs = new HashMap<UUID, ActiveMob>();
    }

    public Swarm(List<String> mobNames, int size, LivingEntity target) {
        this.mobNames = mobNames;
        this.size = size;
        this.target = target;
        this.mobs = new HashMap<UUID, ActiveMob>();
    }

    /**
     * Spawns swarm in area around given location
     * @param loc Location to be spawned at
     * @param level Level of mobs wanted
     */
    public void spawn(Location loc, int level) {
        //Swarm was spawned so we drop
        if(this.spawned) { return; }
        
        List<Location> locations = LocationUtil.getRandomLocations(loc, 5, size);

        for(int i = 0; i < this.size; i++) {
            ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().spawnMob(getMob(), locations.get(i), level);
            if(am == null) { continue; }

            // Setting the mobs target
            am.setTarget(this.target);       
            
            // Tracking active mob in list
            mobs.put(am.getUUID(), am);    
        }

        //Locking swarm
        this.spawned = true;
    }

    /**
     * Despawns all current mobs from world and
     * unregisters the swarm from MobManager
     */
    public void clearMobs() {
        SwordCraftOnline.getPluginInstance().getMobManager().despawnMobs(this.mobs.values());
        this.mobs.clear();

    }

    /**
     * Removes ActiveMob from map
     * @param am Mob to be removed
     */
    public void removeMob(ActiveMob am) {
        this.mobs.remove(am.getUUID());
    }

    /**
     * Helper function to get random mob from list
     * @return String name of mob
     */
    private String getMob() {
        return this.mobNames.get(SwordCraftOnline.r.nextInt(this.mobNames.size()));
    }
}
