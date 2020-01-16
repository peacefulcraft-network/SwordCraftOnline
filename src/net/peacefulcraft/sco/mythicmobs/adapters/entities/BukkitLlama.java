package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Llama;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.properties.AgeableProperty;

public class BukkitLlama extends MythicEntity {
    private static final int height = 2;
   
    private AgeableProperty ageableProperty;
    
    private String horseColor;
    
    private boolean horseChest;
    
    private boolean horseSaddled;
    
    private boolean horseTamed;
    
    public void instantiate(MythicConfig mc) {
        this.ageableProperty = new AgeableProperty(mc);
        this.horseChest = mc.getBoolean("Options.CarryingChest", false);
        this.horseColor = mc.getString("Options.Color", "WHITE");
        this.horseTamed = mc.getBoolean("Options.Tamed", false);
    }
        
    public Entity spawn(MythicMob mm, Location location) {
        Llama e = (Llama)location.getWorld().spawnEntity(location, EntityType.LLAMA);
        e = (Llama)applyOptions((Entity)e);
        return (Entity)e;
    }
        
    public Entity spawn(Location location) {
        Llama e = (Llama)location.getWorld().spawnEntity(location, EntityType.LLAMA);
        return (Entity)e;
    }
        
    public Entity applyOptions(Entity entity) {
        Llama e = (Llama)entity;
        this.ageableProperty.applyProperties(entity);
        try {
            e.setColor(Llama.Color.valueOf(this.horseColor));
        } catch (Exception ex) {
            //MythicMobs.error("Invalid llama color specified" + this.horseColor);
        } 
        if (this.horseChest)
            e.setCarryingChest(true); 
        if (this.horseTamed)
            e.setTamed(true); 
        return (Entity)e;
        }
        
        public boolean compare(Entity e) {
        return e instanceof Llama;
        }
        
        public int getHeight() {
        return height;
    }
}