package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.properties.AgeableProperty;

public class BukkitPig extends MythicEntity {
    private static final int height = 1;
   
    private AgeableProperty ageableProperty;
    
    private boolean saddled = false;
    
    public void instantiate(MythicConfig mc) {
        this.ageableProperty = new AgeableProperty(mc);
        this.saddled = mc.getBoolean("Options.Saddled", false);
    }
    
    public Entity spawn(MythicMob mm, Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.PIG);
        e = applyOptions(e);
        return e;
    }
    
    public Entity spawn(Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.PIG);
        return e;
    }
    
    public Entity applyOptions(Entity entity) {
        Pig e = (Pig)entity;
        this.ageableProperty.applyProperties(entity);
        e.setSaddle(this.saddled);
        return (Entity)e;
    }
    
    public boolean compare(Entity e) {
        return e instanceof Pig;
    }
    
    public int getHeight() {
        return height;
    }
}