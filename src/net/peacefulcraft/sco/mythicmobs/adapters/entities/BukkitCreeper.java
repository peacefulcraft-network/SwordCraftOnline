package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;

public class BukkitCreeper extends MythicEntity {
    private static final int height = 2;
   
    private boolean powered = false;
    
    private boolean preventSuicide = false;
    
    private int explosionFuseTicks = -1;
    
    private int explosionRadius = -1;
    
    public void instantiate(MythicConfig mc) {
        this.powered = mc.getBoolean("Options.SuperCharged", false);
        this.preventSuicide = mc.getBoolean("Options.PreventSuicide", false);
        this.explosionFuseTicks = mc.getInteger("Options.FuseTicks", -1);
        this.explosionRadius = mc.getInteger("Options.ExplosionRadius", -1);
    }

    public Entity spawn(MythicMob mm, Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.CREEPER);
        e = applyOptions(e);
        return e;
    }
      
    public Entity spawn(Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.CREEPER);
        return e;
    }

    public Entity applyOptions(Entity entity) {
        Creeper e = (Creeper)entity;
        if (this.powered) {
          e.setPowered(true);
        } 
        if (this.explosionFuseTicks >= 0) {
            e.setMaxFuseTicks(this.explosionFuseTicks);
        }
        if (this.explosionRadius >= 0) {
            e.setExplosionRadius(this.explosionRadius);
        }   
        return (Entity)e;
    }

    public boolean compare(Entity e) {
        return e instanceof Creeper;
    }
      
    public int getHeight() {
        return height;
    }
      
    public boolean preventSuicide() {
        return this.preventSuicide;
    }
}