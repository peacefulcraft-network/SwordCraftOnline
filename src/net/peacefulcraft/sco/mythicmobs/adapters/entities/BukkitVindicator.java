package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Vindicator;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;

public class BukkitVindicator extends MythicEntity {
    private static final int height = 2;
    
    public void instantiate(MythicConfig mc) {}
    
    public Entity spawn(MythicMob mm, Location location) {
        Vindicator e = (Vindicator)location.getWorld().spawnEntity(location, EntityType.VINDICATOR);
        return (Entity)e;
    }
    
    public Entity spawn(Location location) {
        Vindicator e = (Vindicator)location.getWorld().spawnEntity(location, EntityType.VINDICATOR);
        return (Entity)e;
    }
    
    public Entity applyOptions(Entity entity) {
        return entity;
    }
    
    public boolean compare(Entity e) {
        return isInstanceOf(e);
    }
    
    public static boolean isInstanceOf(Entity e) {
        return e instanceof Vindicator;
    }
    
    public int getHeight() {
        return height;
    }
}