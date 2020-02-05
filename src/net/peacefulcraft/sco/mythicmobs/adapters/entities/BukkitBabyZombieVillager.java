package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.entity.ZombieVillager;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;

public class BukkitBabyZombieVillager extends MythicEntity {
    private static final int height = 1;
   
    private double reinforcementChance = -1.0D;
   
     private Villager.Profession villagerProfession;
   
    public void instantiate(MythicConfig mc) {
        this.reinforcementChance = mc.getDouble("Options.ReinforcementsChance", -1.0D);
        String prof = mc.getString("Options.Profession", "FARMER");
        try {
            this.villagerProfession = Villager.Profession.valueOf(prof.toUpperCase());
        } catch (Exception ex) {
            this.villagerProfession = Villager.Profession.FARMER;
        } 
    }

    public Entity spawn(MythicMob mm, Location location) {
        ZombieVillager zombieVillager = (ZombieVillager)location.getWorld().spawnEntity(location, EntityType.ZOMBIE_VILLAGER);
        zombieVillager.setBaby(true);
        zombieVillager.setVillagerProfession(this.villagerProfession);
        zombieVillager = (ZombieVillager)applyOptions((Entity)zombieVillager);
        return (Entity)zombieVillager;
    }

    public Entity spawn(Location location) {
        ZombieVillager zombieVillager = (ZombieVillager)location.getWorld().spawnEntity(location, EntityType.ZOMBIE_VILLAGER);
        zombieVillager.setBaby(true);
        zombieVillager.setVillagerProfession(this.villagerProfession);
        zombieVillager = (ZombieVillager)applyOptions((Entity)zombieVillager);
        return (Entity)zombieVillager;
    }

    public Entity applyOptions(Entity entity) {
        ZombieVillager e = (ZombieVillager)entity;
        e.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS).setBaseValue(this.reinforcementChance);
        return entity;
    }

    public boolean compare(Entity e) {
        return isInstanceOf(e);
    }

    public static boolean isInstanceOf(Entity e) {
        if (e instanceof ZombieVillager) {
            if (((ZombieVillager)e).isBaby()) {
                return true; 
            }
        }
        return false;
    }

    public int getHeight() {
        return height;
    }
}