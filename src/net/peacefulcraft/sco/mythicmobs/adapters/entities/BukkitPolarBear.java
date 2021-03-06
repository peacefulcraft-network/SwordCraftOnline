package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PolarBear;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.properties.AgeableProperty;

public class BukkitPolarBear extends MythicEntity {
    private static final int height = 1;
   
    private AgeableProperty ageableProperty;
    
    public void instantiate(MythicConfig mc) {
        this.ageableProperty = new AgeableProperty(mc);
    }
    
    public Entity spawn(MythicMob mm, Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.POLAR_BEAR);
        e = applyOptions(e);
        return e;
    }
    
    public Entity spawn(Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.POLAR_BEAR);
        return e;
    }
    
    public Entity applyOptions(Entity entity) {
        PolarBear e = (PolarBear)entity;
        this.ageableProperty.applyProperties(entity);
        return (Entity)e;
    }
    
    public boolean compare(Entity e) {
        return e instanceof PolarBear;
    }
    
    public int getHeight() {
        return height;
    }
}