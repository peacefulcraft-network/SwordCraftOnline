package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Rabbit;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.properties.AgeableProperty;

public class BukkitRabbit extends MythicEntity {
    private static final int height = 1;
   
    private AgeableProperty ageableProperty;
    
    private boolean isAngry = false;
    
    private boolean isBaby = false;
    
    private Rabbit.Type type;
    
    public void instantiate(MythicConfig mc) {
        this.ageableProperty = new AgeableProperty(mc);
        this.isAngry = mc.getBoolean("Options.IsKillerBunny", false);
        this.isAngry = mc.getBoolean("Options.Angry", this.isAngry);
        this.isBaby = mc.getBoolean("Options.Baby", false);
        String t = mc.getString("Options.RabbitType", null);
        if (t != null)
        this.type = Rabbit.Type.valueOf(t.toUpperCase()); 
    }
    
    public Entity spawn(MythicMob mm, Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.RABBIT);
        e = applyOptions(e);
        return e;
    }
    
    public Entity spawn(Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.RABBIT);
        return e;
    }
    
    public Entity applyOptions(Entity entity) {
        Rabbit e = (Rabbit)entity;
        this.ageableProperty.applyProperties(entity);
        if (this.isBaby)
        e.setBaby(); 
        if (this.type != null)
        e.setRabbitType(this.type); 
        if (this.isAngry)
        e.setRabbitType(Rabbit.Type.THE_KILLER_BUNNY); 
        return (Entity)e;
    }
    
    public boolean compare(Entity e) {
        return e instanceof Rabbit;
    }
    
    public int getHeight() {
        return height;
    }
}