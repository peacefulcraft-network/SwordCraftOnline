package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;

public class BukkitCaveSpider extends MythicEntity {
    private static final int height = 1;
   
    public void instantiate(MythicConfig mc) {}
   
    public double getHealthbarOffset() {
        return -0.4D;
    }
   
    public Entity spawn(MythicMob mm, Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.CAVE_SPIDER);
        return e;
    }
   
    public Entity spawn(Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.CAVE_SPIDER);
        return e;
    }
   
    public Entity applyOptions(Entity entity) {
        return entity;
    }
   
    public boolean compare(Entity e) {
        return e instanceof org.bukkit.entity.CaveSpider;
    }
   
    public int getHeight() {
      return height;
    }
}