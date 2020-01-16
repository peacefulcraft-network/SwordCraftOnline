package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TropicalFish;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;

public class BukkitTropicalFish extends MythicEntity {
    private static final int height = 1;
    
    private TropicalFish.Pattern pattern = null;
    
    private DyeColor bodyColor = null;
    
    private DyeColor patternColor = null;
    
    public void instantiate(MythicConfig mc) {
        String strPattern = mc.getString("Options.Pattern", null);
        String strBodyColor = mc.getString("Options.BodyColor", null);
        String strPatternColor = mc.getString("Options.PatternColor", null);
        if (strPattern != null)
            try {
                this.pattern = TropicalFish.Pattern.valueOf(strPattern.toUpperCase());
            } catch (Exception ex) {
                //Log error MythicLogger.errorEntityConfig(this, mc, "Invalid Pattern specified");
            }  
        if (strBodyColor != null)
            try {
                this.bodyColor = DyeColor.valueOf(strBodyColor.toUpperCase());
            } catch (Exception ex) {
                //Log error MythicLogger.errorEntityConfig(this, mc, "Invalid BodyColor specified");
            }  
        if (strPatternColor != null)
            try {
                this.patternColor = DyeColor.valueOf(strPatternColor.toUpperCase());
            } catch (Exception ex) {
                //Log error MythicLogger.errorEntityConfig(this, mc, "Invalid PatternColor specified");
            }  
    }
    
    public Entity spawn(MythicMob mm, Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.TROPICAL_FISH);
        e = applyOptions(e);
        return e;
    }
    
    public Entity spawn(Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.TROPICAL_FISH);
        return e;
    }
    
    public Entity applyOptions(Entity entity) {
        TropicalFish e = (TropicalFish)entity;
        if (this.pattern != null) {
            e.setPattern(this.pattern);
        } 
        if (this.bodyColor != null) {
            e.setBodyColor(this.bodyColor); 
        }
        if (this.patternColor != null) {
            e.setPatternColor(this.patternColor);
        } 
        return (Entity)e;
    }
    
    public boolean compare(Entity e) {
      return e instanceof TropicalFish;
    }
    
    public int getHeight() {
      return height;
    }
}