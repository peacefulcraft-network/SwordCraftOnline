package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;

public class BukkitIronGolem extends MythicEntity {
    private boolean isPlayerCreated = false;
   
    private static final int height = 1;
    
    public void instantiate(MythicConfig mc) {
        this.isPlayerCreated = mc.getBoolean("Options.PlayerCreated", false);
    }
    
    public Entity spawn(MythicMob mm, Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.IRON_GOLEM);
        e = applyOptions(e);
        return e;
    }
    
    public Entity spawn(Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.IRON_GOLEM);
        return e;
    }
    
    public Entity applyOptions(Entity entity) {
        IronGolem e = (IronGolem)entity;
        if (this.isPlayerCreated) {
            e.setPlayerCreated(true); 
        }
        return (Entity)e;
    }
    
    public boolean compare(Entity e) {
        return e instanceof IronGolem;
    }
    
    public int getHeight() {
        return height;
    }
}