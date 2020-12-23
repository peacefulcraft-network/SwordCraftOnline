package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;

public class BukkitBabyPigZombie extends MythicEntity {
    private static final int height = 1;

    private boolean angry = false;

    public void instantiate(MythicConfig mc) {
        this.angry = mc.getBoolean("Options.Angry", false);
    }

    public Entity spawn(MythicMob mm, Location location) {
        PigZombie e = (PigZombie)location.getWorld().spawnEntity(location, EntityType.PIGLIN_BRUTE);
        e.setBaby(true);
        e = (PigZombie)applyOptions((Entity)e);
        return (Entity)e;
    }

    public Entity spawn(Location location) {
        PigZombie e = (PigZombie)location.getWorld().spawnEntity(location, EntityType.PIGLIN_BRUTE);
        e.setBaby(true);
        return (Entity)e;
    }

    public Entity applyOptions(Entity entity) {
        PigZombie e = (PigZombie)entity;
        e.setAngry(this.angry);
        return (Entity)e;
    }

    public boolean compare(Entity e) {
        return isInstanceOf(e);
    }

    public static boolean isInstanceOf(Entity e) {
        if (e instanceof PigZombie) { return ((PigZombie)e).isBaby(); } 
        return false;
    }

    public int getHeight() {
        return height;
    }
}