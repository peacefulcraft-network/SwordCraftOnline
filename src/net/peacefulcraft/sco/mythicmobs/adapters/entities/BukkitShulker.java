package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Shulker;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;

public class BukkitShulker extends MythicEntity {
    private static final int height = 2;
   
    private DyeColor color;
    
    public void instantiate(MythicConfig mc) {
        String strcolor = mc.getString("Options.Color", "WHITE");
        this.color = DyeColor.valueOf(strcolor);
    }
    
    public Entity spawn(MythicMob mm, Location location) {
        Shulker e = (Shulker)location.getWorld().spawnEntity(location, EntityType.SHULKER);
        e = (Shulker)applyOptions((Entity)e);
        return (Entity)e;
    }
    
    public Entity spawn(Location location) {
        Shulker e = (Shulker)location.getWorld().spawnEntity(location, EntityType.SHULKER);
        return (Entity)e;
    }
    
    public Entity applyOptions(Entity entity) {
        Shulker e = (Shulker)entity;
        e.setColor(this.color);
        return (Entity)e;
    }
    
    public boolean compare(Entity e) {
        return isInstanceOf(e);
    }
    
    public static boolean isInstanceOf(Entity e) {
        return e instanceof Shulker;
    }
    
    public int getHeight() {
        return height;
    }
}