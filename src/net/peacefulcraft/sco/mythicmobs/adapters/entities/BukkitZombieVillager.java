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

public class BukkitZombieVillager extends MythicEntity {
    private static final int height = 2;
   
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
        zombieVillager.setBaby(false);
        zombieVillager.setVillagerProfession(this.villagerProfession);
        zombieVillager = (ZombieVillager)applyOptions((Entity)zombieVillager);
        return (Entity)zombieVillager;
    }
   
    public Entity spawn(Location location) {
        ZombieVillager zombieVillager = (ZombieVillager)location.getWorld().spawnEntity(location, EntityType.ZOMBIE_VILLAGER);
        zombieVillager.setBaby(false);
        zombieVillager.setVillagerProfession(this.villagerProfession);
        zombieVillager = (ZombieVillager)applyOptions((Entity)zombieVillager);
        return (Entity)zombieVillager;
    }
   
    public Entity applyOptions(Entity entity) {
        ZombieVillager zombieVillager = (ZombieVillager)entity;
        if (this.reinforcementChance >= 0.0D) {
            zombieVillager.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS).setBaseValue(this.reinforcementChance);
        }
        return (Entity)zombieVillager;
    }
   
    public boolean compare(Entity e) {
        return isInstanceOf(e);
    }
   
    public static boolean isInstanceOf(Entity e) {
        return (e instanceof ZombieVillager && !((ZombieVillager)e).isBaby()); 
    }
   
   public int getHeight() {
     return height;
   }
}