package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.properties.AgeableProperty;

public class BukkitChicken extends MythicEntity {
    private static final int height = 1;
   
    private AgeableProperty ageableProperty;
   
    private boolean jockey = false;
   
    public void instantiate(MythicConfig mc) {
        this.ageableProperty = new AgeableProperty(mc);
        this.jockey = mc.getBoolean("Options.Jockey", false);
    }
   
    public Entity spawn(MythicMob mm, Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.CHICKEN);
        e = applyOptions(e);
        return e;
    }
   
    public Entity spawn(Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.CHICKEN);
        return e;
    }
   
    public Entity applyOptions(Entity entity) {
        Chicken e = (Chicken)entity;
        this.ageableProperty.applyProperties(entity);
        return (Entity)e;
   }
   
    public boolean compare(Entity e) {
        return e instanceof Chicken;
    }
   
    public int getHeight() {
        return height;
    }

    public boolean getJockey() {
        return this.jockey;
    }
}