package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;

public class BukkitFallingBlock extends MythicEntity {
    private static final int height = 1;
   
    private Material type = Material.STONE;
    
    private boolean dropsItem = true;
    
    private boolean hurtsEntities = true;
   
    public void instantiate(MythicConfig mc) {
        String mat = mc.getString("Options.Block", "STONE");
        try {
            this.type = Material.getMaterial(mat.toUpperCase());
        } catch (Exception ex) {
            //Log: MythicMobs.error("Invalid material specified");
        } 
        this.dropsItem = mc.getBoolean("Options.DropsItem", true);
        this.hurtsEntities = mc.getBoolean("Options.HurtsEntities", true);
    }
   
    public Entity spawn(MythicMob mm, Location location) {
        Entity entity;
        FallingBlock fallingBlock = location.getWorld().spawnFallingBlock(location, this.type.createBlockData());
        entity = applyOptions((Entity)fallingBlock); 
        return entity;
    }
   
    public Entity spawn(Location location) {
        return (Entity)location.getWorld().spawnFallingBlock(location, this.type.createBlockData());
    }
   
    public Entity applyOptions(Entity entity) {
        FallingBlock e = (FallingBlock)entity;
        if (!this.dropsItem) {
            e.setDropItem(false); 
        }
        if (!this.hurtsEntities) {
            e.setHurtEntities(false); 
        }
        return (Entity)e;
    }
   
    public boolean compare(Entity e) {
        return e instanceof FallingBlock;
    }
   
    public int getHeight() {
        return height;
    }
}