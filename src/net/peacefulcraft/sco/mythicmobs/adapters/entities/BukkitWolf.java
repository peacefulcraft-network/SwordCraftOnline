package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractPlayer;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.properties.AgeableProperty;

public class BukkitWolf extends MythicEntity {
    private static final int height = 1;
    
    private AgeableProperty ageableProperty;
    
    private boolean angry = false;
    
    private DyeColor color;
    
    private boolean tameable = false;
    
    public void instantiate(MythicConfig mc) {
        this.ageableProperty = new AgeableProperty(mc);
        this.angry = mc.getBoolean("Options.Angry", false);
        this.tameable = mc.getBoolean("Options.Tameable", false);
        String strColor = mc.getString("Options.Color");
        if (strColor != null) {
            try {
                this.color = DyeColor.valueOf(strColor.toUpperCase());
            } catch (Exception ex) {
                //Log error MythicLogger.errorEntityConfig(this, mc, "Invalid Color specified");
            } 
        }  
    }
    
    public Entity spawn(MythicMob mm, Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.WOLF);
        e = applyOptions(e);
        return e;
    }
    
    public Entity spawn(Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.WOLF);
        return e;
    }
    
    public Entity applyOptions(Entity entity) {
        Wolf e = (Wolf)entity;
        e = (Wolf)this.ageableProperty.applyProperties((Entity)e);
        e.setAngry(this.angry);
        if (this.color != null) {
            e.setCollarColor(this.color); 
        }
        return (Entity)e;
    }
    
    public boolean compare(Entity e) {
        return e instanceof Wolf;
    }
    
    public int getHeight() {
        return height;
    }
    
    public boolean isTameable() {
      return this.tameable;
    }
    
    public Player getOwner(ActiveMob am) {
      Entity e = am.getEntity();
      if (e instanceof Wolf) {
        Wolf w = (Wolf)e;
        if (w.getOwner() instanceof Player)
          return ((Player)w.getOwner()); 
      } 
      return null;
    }
}