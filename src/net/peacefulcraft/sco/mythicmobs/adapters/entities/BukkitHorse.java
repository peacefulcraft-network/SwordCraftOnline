package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.properties.AgeableProperty;

public class BukkitHorse extends MythicEntity {
    private static final int height = 2;
    
    private AgeableProperty ageableProperty;
    
    private String horseStyle;
    
    
    private String horseColor;
    
    private String horseArmor;
    
    private boolean horseSaddled;
    
    private boolean horseTamed;
    
    public void instantiate(MythicConfig mc) {
      this.ageableProperty = new AgeableProperty(mc);
      this.horseArmor = mc.getString("Options.HorseArmor");
      //this.horseChest = mc.getBoolean("Options.HorseCarryingChest", false);
      this.horseStyle = mc.getString("Options.HorseStyle");
      //this.horseType = mc.getString("Options.HorseType");
      this.horseColor = mc.getString("Options.HorseColor");
      this.horseColor = mc.getString("Options.Color", this.horseColor);
      this.horseSaddled = mc.getBoolean("Options.HorseSaddled", false);
      this.horseSaddled = mc.getBoolean("Options.Saddled", this.horseSaddled);
      this.horseTamed = mc.getBoolean("Options.HorseTamed", false);
      this.horseTamed = mc.getBoolean("Options.Tamed", this.horseTamed);
    }
    
    public Entity spawn(MythicMob mm, Location location) {
      Entity e = location.getWorld().spawnEntity(location, EntityType.HORSE);
      e = applyOptions(e);
      return e;
    }
    
    public Entity spawn(Location location) {
      Entity e = location.getWorld().spawnEntity(location, EntityType.HORSE);
      return e;
    }
    
    public Entity applyOptions(Entity entity) {
      Horse e = (Horse)entity;
      this.ageableProperty.applyProperties(entity);
      HorseInventory hi = e.getInventory();
      if (this.horseStyle != null)
        e.setStyle(Horse.Style.valueOf(this.horseStyle.toUpperCase())); 
      if (this.horseColor != null)
        try {
          e.setColor(Horse.Color.valueOf(this.horseColor.toUpperCase()));
        } catch (Exception ex) {
          SwordCraftOnline.logInfo("Attempted to load invalid horse color: " + this.horseColor);
        }  
      if (this.horseTamed)
        e.setTamed(true); 
      if (this.horseSaddled)
        hi.setSaddle(new ItemStack(Material.SADDLE, 1)); 
      if (this.horseArmor != null) {
        switch (this.horseArmor) {
          case "diamond":
            hi.setArmor(new ItemStack(Material.DIAMOND_HORSE_ARMOR, 1));
            return (Entity)e;
          case "gold":
          case "golden":
            hi.setArmor(new ItemStack(Material.GOLDEN_HORSE_ARMOR, 1));
            return (Entity)e;
        } 
        hi.setArmor(new ItemStack(Material.IRON_HORSE_ARMOR, 1));
      } 
      return (Entity)e;
    }
    
    public boolean compare(Entity e) {
      return e instanceof Horse;
    }
    
    public int getHeight() {
      return height;
    }
  }