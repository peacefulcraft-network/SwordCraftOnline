package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Phantom;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;

public class BukkitPhantom extends MythicEntity {
    private static final int height = 2;
   
    private int size = -1;
    
    public void instantiate(MythicConfig mc) {
        this.size = mc.getInteger("Options.Size", -1);
    }
    
    public Entity spawn(MythicMob mm, Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.PHANTOM);
        e = applyOptions(e);
        return e;
    }
    
    public Entity spawn(Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.PHANTOM);
        return e;
    }
    
    public Entity applyOptions(Entity entity) {
        Phantom e = (Phantom)entity;
        if (this.size > 0)
        e.setSize(this.size); 
        return (Entity)e;
    }
    
    public boolean compare(Entity e) {
        return e instanceof Phantom;
    }
    
    public int getHeight() {
        return height;
    }
}