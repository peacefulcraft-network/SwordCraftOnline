package net.peacefulcraft.sco.mythicmobs.mobs;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class Swarm {
    
    private String name;

    private int size;

    private LivingEntity target;

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
    }

    public void spawn(Location loc, int level) {
        for(int i = 0; i < this.size; i++) {
            
        }
    }
}
