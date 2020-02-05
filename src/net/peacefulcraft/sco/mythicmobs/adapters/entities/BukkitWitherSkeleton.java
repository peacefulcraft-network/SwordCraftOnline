package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WitherSkeleton;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;

public class BukkitWitherSkeleton extends MythicEntity {
    private static final int height = 3;
    
    public void instantiate(MythicConfig mc) {}
    
    public Entity spawn(MythicMob mm, Location location) {
        WitherSkeleton witherSkeleton = (WitherSkeleton)location.getWorld().spawnEntity(location, EntityType.WITHER_SKELETON);
        return (Entity)witherSkeleton;
    }
    
    public Entity spawn(Location location) {
        WitherSkeleton witherSkeleton = (WitherSkeleton)location.getWorld().spawnEntity(location, EntityType.WITHER_SKELETON);
        return (Entity)witherSkeleton;
    }
    
    public Entity applyOptions(Entity e) {
      return e;
    }
    
    public boolean compare(Entity e) {
      return isInstanceOf(e);
    }
    
    public static boolean isInstanceOf(Entity e) {
        return e instanceof WitherSkeleton; 
    }
    
    public int getHeight() {
      return height;
    }
}