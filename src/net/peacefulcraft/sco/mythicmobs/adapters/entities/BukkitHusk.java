package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Husk;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;

public class BukkitHusk extends MythicEntity {
    private static final int height = 2;
   
    private double reinforcementChance = -1.0D;
    
    public void instantiate(MythicConfig mc) {
        this.reinforcementChance = mc.getDouble("Options.ReinforcementsChance", -1.0D);
    }
    
    public Entity spawn(MythicMob mm, Location location) {
        Husk husk = (Husk)location.getWorld().spawnEntity(location, EntityType.HUSK);
        husk.setBaby(false);
        return (Entity)husk;
    }
    
    public Entity spawn(Location location) {
        Husk husk = (Husk)location.getWorld().spawnEntity(location, EntityType.HUSK);
        husk.setBaby(false);
        return (Entity)husk;
    }
    
    public Entity applyOptions(Entity entity) {
        Husk husk = (Husk)entity;
        if (this.reinforcementChance >= 0.0D)
        husk.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS).setBaseValue(this.reinforcementChance);
        return (Entity)husk;
    }
    
    public boolean compare(Entity e) {
      return isInstanceOf(e);
    }
    
    public static boolean isInstanceOf(Entity e) {
        return (e instanceof Husk && !((Husk)e).isBaby()); 
    }
    
    public int getHeight() {
      return height;
    }
}