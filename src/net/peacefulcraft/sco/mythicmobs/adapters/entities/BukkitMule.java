package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mule;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.properties.AgeableProperty;

public class BukkitMule extends MythicEntity {
    private static final int height = 2;
   
    private AgeableProperty ageableProperty;
    
    private boolean horseChest;
    
    private boolean horseSaddled;
    
    private boolean horseTamed;
    
    public void instantiate(MythicConfig mc) {
        this.ageableProperty = new AgeableProperty(mc);
        this.horseChest = mc.getBoolean("Options.HorseCarryingChest", false);
        this.horseChest = mc.getBoolean("Options.CarryingChest", this.horseChest);
        this.horseSaddled = mc.getBoolean("Options.HorseSaddled", false);
        this.horseSaddled = mc.getBoolean("Options.Saddled", this.horseSaddled);
        this.horseTamed = mc.getBoolean("Options.HorseTamed", false);
        this.horseTamed = mc.getBoolean("Options.Tamed", this.horseTamed);
    }
   
    public Entity spawn(MythicMob mm, Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.MULE);
        e = applyOptions(e);
        return e;
    }
    
    public Entity spawn(Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.MULE);
        return e;
    }
    
    public Entity applyOptions(Entity entity) {
        Mule e = (Mule)entity;
        this.ageableProperty.applyProperties(entity);
        AbstractHorseInventory hi = e.getInventory();
        if (this.horseTamed) {
            e.setTamed(true); 
        }
        if (this.horseSaddled) {
            hi.setSaddle(new ItemStack(Material.SADDLE, 1)); 
        }
        if (this.horseChest) {
            e.setCarryingChest(true); 
        }
        return (Entity)e;
    }
    
    public boolean compare(Entity e) {
        return e instanceof Mule;
    }
    
    public int getHeight() {
        return height;
    }
}