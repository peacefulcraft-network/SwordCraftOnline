package net.peacefulcraft.sco.swordskills.utilities;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntityType;

/**
 * Class for damage modifiers. Used in calculations of damage in an
 * EntityDamageEvent.
 */
public class Modifier {
    /**
     * Enum to signify how we calculate the modifier.
     * To subtract use ADD and set to negative
     * To divide use MULTIPLY and set to a fraction.
     */
    public enum MultiplierType{
        ADD, MULTIPLY;
        
        public static MultiplierType get(String s) {
            if(s == null) { return null; }
            try{
                return valueOf(s.toUpperCase());
            }catch(IllegalArgumentException e) {
                return null;
            }
        }
    }

    /**
     * Enum of effectively all mobs / entities in MC
     * Including extras: BOSS, PLAYER, etc.
     */
    public enum ModifierType{
        ARMOR_STAND, BABY_DROWNED, BABY_HUSK, BABY_PIG_ZOMBIE, BABY_PIG_ZOMBIE_VILLAGER, BABY_ZOMBIE, BABY_ZOMBIE_VILLAGER, BAT, BLAZE, CAT, CAVE_SPIDER, CHICKEN, COD, COW, CREEPER, CUSTOM, DOLPHIN, DONKEY, DROWNED, ELDER_GUARDIAN, ENDER_DRAGON, ENDERMAN, ENDERMITE, EVOKER, FALLING_BLOCK, FOX, GHAST, GIANT, GUARDIAN, HORSE, HUSK, ILLUSIONER, IRON_GOLEM, LLAMA, MAGMA_CUBE, MINECART, MINECART_CHEST, MINECART_COMMAND, MINECART_FURNACE, MINECART_HOPPER, MINECART_MOB_SPAWNER, MINECART_TNT, MULE, MUSHROOM_COW, OCELOT, PANDA, PARROT, PHANTOM, PIG, PIG_ZOMBIE, PIG_ZOMBIE_VILLAGER, PILLAGER, POLAR_BEAR, PRIMED_TNT, PUFFERFISH, RABBIT, RAVAGER, SALMON, SHEEP, SHULKER, SILVERFISH, SKELETON, SKELETON_HORSE, SLIME, SNOWMAN, SPIDER, STRAY, SQUID, TRADER_LLAMA, TROPICAL_FISH, TURTLE, VEX, VILLAGER, VINDICATOR, WANDERING_TRADER, WITCH, WITHER, WITHER_SKELETON, WOLF, ZOMBIE, ZOMBIE_HORSE, ZOMBIE_VILLAGER
        , PLAYER, BOSS
        , BLOCK_EXPLOSION, DRAGON_BREATH, DROWNING, ENTITY_EXPLOSION, FALL, FIRE, FIRE_TICK, HOT_FLOOR, LAVA, LIGHTNING, MAGIC, POISON, PROJECTILE, SUFFOCATION, THORNS;

        public static ModifierType get(String s) {
            if(s == null) { return null; }
            try{
                return valueOf(s.toUpperCase());
            } catch(IllegalArgumentException e) {
                try{
                    EntityType et = EntityType.valueOf(s.toUpperCase());
                    if(et == null) { return null; }
                    return CUSTOM;
                } catch(Exception ex) {
                    return null;
                }
            }
        }
    }

    /**The Type of mob it is effective against */
    ModifierType type;
    /**The amount to modify the damage*/
    double multiplier;
    /**The type of calculation we do. */
    MultiplierType mType;
    /**True if strength, false if weakness */
    Boolean strength;

    /**
     * Constructs baseline modifier.
     */
    public Modifier(ModifierType type, double multiplier, MultiplierType mType, boolean strength) {
        this.type = type;
        this.multiplier = multiplier;
        this.mType = mType;
        this.strength = strength;
    }

    /**
     * Constructs modifier from string.
     * String should be formatted: ZOMBIE-1.2-MULTIPLY
     * @param strength True if strength, False if weakness
     */
    public Modifier(String s, boolean strength) {
        String[] split = s.split("-");
        this.type = ModifierType.get(split[0]);
        this.multiplier = Double.valueOf(split[1]);
        this.mType = MultiplierType.get(split[2]);
    }

    /**
     * Calculations done specifically for EntityDamageEvents only
     * Example: Drowning, explosion, fire, magic(potions), lightning, etc.
     * Handles creeper explosions.
     * Only use for incoming damage.
     */
    public double calculate(EntityDamageEvent e) {
        double d = e.getDamage();
        Entity ent = e.getEntity();
        //Victim is mythic mob
        if(SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().containsKey(ent.getUniqueId())) {
            //Checking if modifier type matches damage type
            if(this.type.toString().equalsIgnoreCase(e.getCause().toString())) {
                d = internalCalc(e);
            }
        }
        //Victim is player
        if(ent instanceof Player && GameManager.findSCOPlayer((Player)ent) != null) {
            if(this.type.toString().equalsIgnoreCase(e.getCause().toString())) {
                d = internalCalc(e);
            }
        }
        return d;
    }

    /**
     * Calculations done specifically for EntityDamageEntity only
     */
    public double calculate(EntityDamageByEntityEvent e) {
        Entity ent;
        double d = e.getDamage();
        if(strength) {
            //Outgoing damage
            ent = e.getEntity();    
        } else {
            //Incoming damage
            ent = e.getDamager();
        }
        //Victim is mythic mob
        if(SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().containsKey(ent.getUniqueId())) {
            MythicMob mm = SwordCraftOnline.getPluginInstance().getMobManager().getMMDisplay().get(ent.getCustomName());
            //Mob is boss
            if(mm.usesBossBar() && this.type == ModifierType.BOSS) {
                d = internalCalc(e);
            }
            //Mob matches modifier.type
            if(mm.getMobType() == (MythicEntity.getMythicEntity(MythicEntityType.get(this.type.toString())))) {
                d = internalCalc(e);
            }
        }
        //Victim is player
        if(ent instanceof Player && GameManager.findSCOPlayer((Player) ent) != null) {
            if(this.type == ModifierType.PLAYER) {
                d = internalCalc(e);
            }
        } 
        return d;
    }

    /**
     * Holds internal calculation of modifier and multiplier type.
     */
    private double internalCalc(EntityDamageByEntityEvent e) {
        if(this.mType == MultiplierType.MULTIPLY) { return e.getDamage() * this.multiplier; }
        return e.getDamage() + this.multiplier;
    }

    /**
     * Holds internal calculation of modifier and multiplier type.
     */
    private double internalCalc(EntityDamageEvent e) {
        if(this.mType == MultiplierType.MULTIPLY) { return e.getDamage() * this.multiplier; }
        return e.getDamage() + this.multiplier;
    }

    /**
     * Static method. Checks list for matching modifier and returns damage.
     */
    public static double calculateList(List<Modifier> l, Event e) {
        double d = 0;
        try{
            EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent)e;
            for(Modifier m : l) {
                d = m.calculate(ev);
                if(d != 0) { break; }
            }
        } catch(Exception ex) {
            SwordCraftOnline.logSevere("Attempted to load modifier from illegal event argument.");
            return 0;
        }
        try{
            EntityDamageEvent ev = (EntityDamageEvent)e;
            for(Modifier m : l) {
                d = m.calculate(ev);
                if(d != 0) { break; }
            }
        } catch(Exception ex) {
            SwordCraftOnline.logSevere("Attempted to load modifier from illegal event argument.");
            return 0;
        }
        return d;
    }

    /**
     * Static method. Checks players damage modifiers directly.
     */
    public static double calculateList(SCOPlayer s, Event e) {
        return calculateList(s.getDamageModifiers(), e);
    }

    /**
     * Static method. Checks active mobs damage modifiers directly
     */
    public static double calculateList(ActiveMob am, Event e) {
        return calculateList(am.getDamageModifiers(), e);
    }
}
