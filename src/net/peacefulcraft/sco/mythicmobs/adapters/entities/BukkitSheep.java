package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Sheep;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.properties.AgeableProperty;

public class BukkitSheep extends MythicEntity {
    protected static final int height = 2;
   
    private AgeableProperty ageableProperty;
    
    protected DyeColor color;
    
    private boolean isSheared = false;
    
    public void instantiate(MythicConfig mc) {
        this.ageableProperty = new AgeableProperty(mc);
        String strcolor = mc.getString("Options.Color", "WHITE");
        this.color = DyeColor.valueOf(strcolor);
        this.isSheared = mc.getBoolean("Options.Sheared", false);
    }
    
    public Entity spawn(MythicMob mm, Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.SHEEP);
        e = applyOptions(e);
        return e;
    }
    
    public Entity spawn(Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.SHEEP);
        return e;
    }
    
    public Entity applyOptions(Entity entity) {
        Sheep e = (Sheep)entity;
        this.ageableProperty.applyProperties(entity);
        e.setColor(this.color);
        e.setSheared(this.isSheared);
        return (Entity)e;
    }
    
    public boolean compare(Entity e) {
        return e instanceof Sheep;
    }
    
    public int getHeight() {
        return height;
    }
    
    public DyeColor getColor() {
        return this.color;
    }
}