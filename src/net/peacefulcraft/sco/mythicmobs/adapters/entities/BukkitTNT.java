package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;

public class BukkitTNT extends MythicEntity {
    private static final int height = 1;
    
    private int explosionFuseTicks = -1;
    
    private int explosionYield = -1;
    
    private boolean explosionIncendiary = false;
    
    public void instantiate(MythicConfig mc) {
        this.explosionFuseTicks = mc.getInteger("Options.FuseTicks", -1);
        this.explosionYield = mc.getInteger("Options.ExplosionYield", -1);
        this.explosionIncendiary = mc.getBoolean("Options.Incendiary", false);
    }
    
    public Entity spawn(MythicMob mm, Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.PRIMED_TNT);
        e = applyOptions(e);
        return e;
    }
    
    public Entity spawn(Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.PRIMED_TNT);
        return e;
    }
    
    public Entity applyOptions(Entity entity) {
        TNTPrimed tnt = (TNTPrimed)entity;
        if (this.explosionFuseTicks > -1) {
            tnt.setFuseTicks(this.explosionFuseTicks); 
        }
        if (this.explosionYield > -1) {
            tnt.setYield(this.explosionYield); 
        }
        tnt.setIsIncendiary(this.explosionIncendiary);
        return (Entity)tnt;
    }
    
    public boolean compare(Entity e) {
        return e instanceof TNTPrimed;
    }
    
    public int getHeight() {
        return height;
    }
}