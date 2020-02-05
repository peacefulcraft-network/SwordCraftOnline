package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;

public class BukkitElderGuardian extends MythicEntity {
    private static final int height = 1;
   
    public void instantiate(MythicConfig mc) {}
   
    public Entity spawn(MythicMob mm, Location location) {
        ElderGuardian elderGuardian = (ElderGuardian)location.getWorld().spawnEntity(location, EntityType.ELDER_GUARDIAN);
        return (Entity)elderGuardian;
    }
   
    public Entity spawn(Location location) {
        ElderGuardian elderGuardian = (ElderGuardian)location.getWorld().spawnEntity(location, EntityType.ELDER_GUARDIAN);
        return (Entity)elderGuardian;
    }
   
    public Entity applyOptions(Entity e) {
        return e;
    }
   
    public boolean compare(Entity e) {
        return isInstanceOf(e);
    }
   
    public static boolean isInstanceOf(Entity e) {
        return e instanceof ElderGuardian;
    }
   
    public int getHeight() {
        return height;
    }
}