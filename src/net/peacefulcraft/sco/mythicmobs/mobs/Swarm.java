package net.peacefulcraft.sco.mythicmobs.mobs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.utilities.LocationUtil;

public class Swarm {
    
    private String name;

    private int size;
        public void setSize(int i) { this.size = i; }

    private LivingEntity target;

    private List<ActiveMob> mobs;

    /**
     * Constructs swarm with target and mythic mob type
     * @param type
     * @param size
     * @param target
     */
    public Swarm(String name, int size, LivingEntity target) {
        this.name = name;
        this.size = size;
        this.target = target;
        this.mobs = new ArrayList<ActiveMob>();
    }

    /**
     * Spawns swarm in area around given location
     * @param loc Location to be spawned at
     * @param level Level of mobs wanted
     */
    public void spawn(Location loc, int level) {
        List<Location> locations = LocationUtil.getRandomLocations(loc, 5, size);

        for(int i = 0; i < this.size; i++) {
            ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().spawnMob(this.name, locations.get(i), level);
            if(am == null) { continue; }

            // Setting the mobs target
            am.setTarget(this.target);       
            
            // Tracking active mob in list
            mobs.add(am);      
        }
    }

    /**
     * Despawns all current mobs from world and unregisters
     */
    public void clearMobs() {
        SwordCraftOnline.getPluginInstance().getMobManager().despawnMobs(this.mobs);
        this.mobs.clear();
    }
}
