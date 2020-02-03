package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;

public class BukkitVillager extends MythicEntity {
    private static final int height = 2;
    
    private int age = 0;
    
    private boolean ageLock = false;
    
    private Villager.Profession villagerType;
    
    private boolean hasTrades = true;
    
    public void instantiate(MythicConfig mc) {
        this.age = mc.getInteger("Options.Age", 0);
        this.ageLock = mc.getBoolean("Options.AgeLock", false);
        String strVillagerType = mc.getString("Options.VillagerType", "NITWIT");
        strVillagerType = mc.getString("Options.Profession", strVillagerType);
        try {
            this.villagerType = Villager.Profession.valueOf(strVillagerType);
        } catch (Exception ex) {
            //Log Error MythicLogger.errorEntityConfig(this, mc, "Invalid VillagerType specified");
        } 
        this.hasTrades = mc.getBoolean("Options.HasTrades", true);
    }
    
    public Entity spawn(MythicMob mm, Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        e = applyOptions(e);
        return e;
    }
    
    public Entity spawn(Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        return e;
    }
    
    public Entity applyOptions(Entity entity) {
        Villager e = (Villager)entity;
        e.setAge(this.age);
        e.setAgeLock(this.ageLock);
        if (this.villagerType != null) {
            e.setProfession(this.villagerType); 
        }
        if (!this.hasTrades) {
            e.setRecipes(null);
        } 
        return (Entity)e;
    }
    
    public boolean compare(Entity e) {
      return e instanceof Villager;
    }
    
    public int getHeight() {
      return height;
    }
}