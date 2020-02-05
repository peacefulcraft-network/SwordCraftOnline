package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;

public class BukkitZombie extends MythicEntity {
    private static final int height = 2;
    
    private double reinforcementChance = -1.0D;
    
    public void instantiate(MythicConfig mc) {
      this.reinforcementChance = mc.getDouble("Options.ReinforcementsChance", -1.0D);
    }
    
    public Entity spawn(MythicMob mm, Location location) {
        Zombie e = (Zombie)location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
        e.setBaby(false);
        e = (Zombie)applyOptions((Entity)e);
        return (Entity)e;
    }
    
    public Entity spawn(Location location) {
        Zombie e = (Zombie)location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
        e.setBaby(false);
        return (Entity)e;
    }
    
    public Entity applyOptions(Entity entity) {
        Zombie e = (Zombie)entity;
        if (this.reinforcementChance >= 0.0D) {
            e.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS).setBaseValue(this.reinforcementChance); 
        }
        return (Entity)e;
    }
    
    public boolean compare(Entity e) {
        return isInstanceOf(e);
    }
    
    public static boolean isInstanceOf(Entity e) {
        if (e instanceof Zombie && e.getType().equals(EntityType.ZOMBIE)) {
            if (!((Zombie)e).isBaby()) {
                return true;  
            }
            return false;
        }
        return false;
    }
    
    public int getHeight() {
        return height;
    }
}