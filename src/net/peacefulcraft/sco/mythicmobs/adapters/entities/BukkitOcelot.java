package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.properties.AgeableProperty;

public class BukkitOcelot extends MythicEntity {
    private static final int height = 1;
   
    private AgeableProperty ageableProperty;
    
    private boolean tameable = true;
    
    private String catType = "WILD_OCELOT";
    
    public void instantiate(MythicConfig mc) {
        this.ageableProperty = new AgeableProperty(mc);
        this.catType = mc.getString("Options.Ocelot", "WILD_OCELOT");
        this.catType = mc.getString("Options.CatType", this.catType);
        this.catType = mc.getString("Options.OcelotType", this.catType);
        this.tameable = mc.getBoolean("Options.Tameable", false);
    }
   
    public Entity spawn(MythicMob mm, Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.OCELOT);
        e = applyOptions(e);
        return e;
    }
    
    public Entity spawn(Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.OCELOT);
        return e;
    }
    
    public Entity applyOptions(Entity entity) {
        Ocelot e = (Ocelot)entity;
        this.ageableProperty.applyProperties(entity);
        return (Entity)e;
    }
    
    public boolean compare(Entity e) {
        return e instanceof org.bukkit.entity.Cow;
    }
    
    public int getHeight() {
        return height;
    }
    
    public boolean isTameable() {
        return this.tameable;
    }
}