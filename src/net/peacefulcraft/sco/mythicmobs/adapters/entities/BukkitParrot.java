package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Parrot;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.properties.AgeableProperty;

public class BukkitParrot extends MythicEntity {
    protected static final int height = 1;
   
    private AgeableProperty ageableProperty;
    
    private Parrot.Variant variant;
    
    public void instantiate(MythicConfig mc) {
        this.ageableProperty = new AgeableProperty(mc);
        String strcolor = mc.getString("Options.Color", "BLUE");
        strcolor = mc.getString("Options.Variant", strcolor);
        this.variant = Parrot.Variant.valueOf(strcolor);
    }
    
    public Entity spawn(MythicMob mm, Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.PARROT);
        e = applyOptions(e);
        return e;
    }
    
    public Entity spawn(Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.PARROT);
        return e;
    }
    
    public Entity applyOptions(Entity entity) {
        Parrot e = (Parrot)entity;
        this.ageableProperty.applyProperties(entity);
        e.setVariant(this.variant);
        return (Entity)e;
    }
    
    public boolean compare(Entity e) {
        return e instanceof Parrot;
    }
    
    public int getHeight() {
        return height;
    }
    
    public Parrot.Variant getVariant() {
        return this.variant;
   }
}