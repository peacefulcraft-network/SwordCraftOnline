package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MushroomCow;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.properties.AgeableProperty;

public class BukkitMushroomCow extends MythicEntity {
    private static final int height = 2;
   
    private AgeableProperty ageableProperty;
    
    public void instantiate(MythicConfig mc) {
        this.ageableProperty = new AgeableProperty(mc);
    }
   
    public Entity spawn(MythicMob mm, Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.MUSHROOM_COW);
        e = applyOptions(e);
        return e;
    }
    
    public Entity spawn(Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.MUSHROOM_COW);
        return e;
    }
    
    public Entity applyOptions(Entity entity) {
        MushroomCow e = (MushroomCow)entity;
        this.ageableProperty.applyProperties(entity);
        return (Entity)e;
    }
    
    public boolean compare(Entity e) {
        return e instanceof MushroomCow;
    }
    
    public int getHeight() {
        return height;
    }
}