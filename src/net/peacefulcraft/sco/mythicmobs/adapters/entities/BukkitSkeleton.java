package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;

public class BukkitSkeleton extends MythicEntity {
    private static final int height = 2;
   
    public void instantiate(MythicConfig mc) {}
   
    public Entity spawn(MythicMob mm, Location location) {
        Skeleton skeleton = (Skeleton)location.getWorld().spawnEntity(location, EntityType.SKELETON);
        return (Entity)skeleton;
    }
   
    public Entity spawn(Location location) {
       Skeleton skeleton = (Skeleton)location.getWorld().spawnEntity(location, EntityType.SKELETON);
       return (Entity)skeleton;
    }
   
    public Entity applyOptions(Entity e) {
        return e;
    }
   
    public boolean compare(Entity e) {
        return isInstanceOf(e);
    }
   
    public static boolean isInstanceOf(Entity e) {
       return e instanceof Skeleton; 
    }
   
    public int getHeight() {
        return height;
    }
}