package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;

public class BukkitGuardian extends MythicEntity {
    private static final int height = 1;
    
    public void instantiate(MythicConfig mc) {}
    
    public Entity spawn(MythicMob mm, Location location) {
        Guardian e = (Guardian)location.getWorld().spawnEntity(location, EntityType.GUARDIAN);
        return (Entity)e;
    }
    
    public Entity spawn(Location location) {
        Guardian e = (Guardian)location.getWorld().spawnEntity(location, EntityType.GUARDIAN);
        return (Entity)e;
    }
    
    public Entity applyOptions(Entity e) {
        return e;
    }
    
    public boolean compare(Entity e) {
        return isInstanceOf(e);
    }
    
    public static boolean isInstanceOf(Entity e) {
        return e instanceof Guardian; 
    }
    
    public int getHeight() {
        return height;
    }
}