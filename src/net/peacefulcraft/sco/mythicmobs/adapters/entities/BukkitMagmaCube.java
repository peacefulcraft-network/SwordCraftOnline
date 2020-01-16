package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MagmaCube;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;

public class BukkitMagmaCube extends MythicEntity {
    private static final int height = 2;
   
    private int size = -1;
    
    private boolean preventSplit = false;
    
    public void instantiate(MythicConfig mc) {
        this.size = mc.getInteger("Options.Size", -1);
        this.preventSplit = mc.getBoolean(".Options.PreventSplit", false);
        this.preventSplit = mc.getBoolean(".Options.PreventSlimeSplit", this.preventSplit);
    }
    
    public Entity spawn(MythicMob mm, Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.MAGMA_CUBE);
        e = applyOptions(e);
        return e;
    }
    
    public Entity spawn(Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.MAGMA_CUBE);
        return e;
    }
    
    public Entity applyOptions(Entity entity) {
        MagmaCube e = (MagmaCube)entity;
        if (this.size > 0)
            e.setSize(this.size); 
         return (Entity)e;
    }
    
    public boolean compare(Entity e) {
        return e instanceof MagmaCube;
    }
    
    public int getHeight() {
        return height;
    }
    
    public boolean canSplit() {
        return !this.preventSplit;
    }
}