package net.peacefulcraft.sco.mythicmobs.adapters.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;

public class BukkitCustom extends MythicEntity {
    private String strMobType = null;
    
    public void instantiate(MythicConfig mc) {
        this.strMobType = mc.getString("Type");
        this.strMobType = mc.getString("MobType", this.strMobType);
        this.strMobType = mc.getString("MobType", this.strMobType);
    }

    public Entity spawn(MythicMob mm, Location loc) {
        Entity e = loc.getWorld().spawnEntity(loc, MythicEntity.fromName(this.strMobType));
        if(e == null) {
            try {
                e = loc.getWorld().spawnEntity(loc, MythicEntity.fromId(Integer.parseInt(this.strMobType)));
            } catch (Exception ex) {
                //TODO:Logger error
                return null;
            }
        }
        return e;
    }

    public Entity spawn(Location loc) {
        return spawn(null, loc);
    }

    public Entity applyOptions(Entity e) {
        return e;
    }

    public boolean compare(Entity e) {
        return false;
    }
}