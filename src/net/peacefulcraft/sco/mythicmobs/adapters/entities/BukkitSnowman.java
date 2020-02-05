package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Snowman;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;

public class BukkitSnowman extends MythicEntity {
    private static final int height = 2;
   
    private boolean preventSnowFormation = false;
    
    private boolean isDerp = false;
    
    public void instantiate(MythicConfig mc) {
        this.preventSnowFormation = mc.getBoolean("Options.PreventSnowFormation", false);
        this.isDerp = mc.getBoolean("Options.Derp", false);
    }
   
    public Entity spawn(MythicMob mm, Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.SNOWMAN);
        e = applyOptions(e); 
        return e;
    }
   
    public Entity spawn(Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.SNOWMAN);
        return e;
    }
   
    public Entity applyOptions(Entity entity) {
        Snowman e = (Snowman)entity;
        e.setDerp(this.isDerp);
        return (Entity)e;
    }
   
    public boolean compare(Entity e) {
        return e instanceof Snowman;
    }
   
    public int getHeight() {
        return height;
    }
   
    public boolean getPreventSnowFormation() {
        return this.preventSnowFormation;
    }
}