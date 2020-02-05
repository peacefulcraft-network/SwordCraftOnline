package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fox;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.properties.AgeableProperty;

public class BukkitFox extends MythicEntity {
    private static final int height = 1;
   
    private AgeableProperty ageableProperty;
    
    public void instantiate(MythicConfig mc) {
        this.ageableProperty = new AgeableProperty(mc);
    }
   
    public Entity spawn(MythicMob mm, Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.FOX);
        e = applyOptions(e);
        return e;
    }
   
    public Entity spawn(Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.FOX);
        return e;
    }
   
    public Entity applyOptions(Entity entity) {
        Fox e = (Fox)entity;
        this.ageableProperty.applyProperties(entity);
        return (Entity)e;
    }
   
    public boolean compare(Entity e) {
        return e instanceof Fox;
    }
   
    public int getHeight() {
        return height;
    }
}