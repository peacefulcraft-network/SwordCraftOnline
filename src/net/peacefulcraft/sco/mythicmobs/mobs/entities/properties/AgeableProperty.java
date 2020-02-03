package net.peacefulcraft.sco.mythicmobs.mobs.entities.properties;

import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;

public class AgeableProperty implements EntityPropertySet {
    private boolean forceAdult;

    private boolean forceBaby;

    private int age = -1;

    private boolean lockAge;

    public AgeableProperty(MythicConfig mc) {
        if (mc.isSet("Options.Adult")) {
            boolean b = mc.getBoolean("Options.Adult");
            if (b == true) {
                this.forceAdult = true;
                this.forceBaby = false;
             } else {
                this.forceAdult = false;
                this.forceBaby = true;
            } 
        } else if (mc.isSet("Options.Baby")) {
            boolean b = mc.getBoolean("Options.Baby");
            if (b == true) {
                this.forceAdult = false;
                this.forceBaby = true;
            } else {
                this.forceAdult = true;
                this.forceBaby = false;
            } 
        } else {
            this.forceAdult = false;
            this.forceBaby = false;
            try {
                this.age = mc.getInteger("Options.Age", -1);
            } catch (Exception ex) {
                //TODO: Logger
            } 
        } 
        this.lockAge = mc.getBoolean("Options.AgeLock", false);
    }

    public Entity applyProperties(Entity entity) {
        Ageable ageable = (Ageable)entity;
        if (this.forceAdult) {
            ageable.setAdult();
        } else if (this.forceBaby) {
            ageable.setBaby();
        } else if (this.age > -1) {
            ageable.setAge(this.age);
        } 
        ageable.setAgeLock(this.lockAge);
        return (Entity)ageable;
    }
}