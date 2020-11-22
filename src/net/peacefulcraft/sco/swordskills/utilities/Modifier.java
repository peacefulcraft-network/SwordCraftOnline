package net.peacefulcraft.sco.swordskills.utilities;

import org.bukkit.entity.EntityType;

/**
 * Class for damage modifiers. Used in calculations of damage in an
 * EntityDamageEvent.
 */
public class Modifier {

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
    /**True if damage is incoming, false if outgoing */
    Boolean incoming;

    /**
     * Constructs baseline modifier.
     */
    public Modifier(ModifierType type, double multiplier, boolean incoming) {
        this.type = type;
        this.multiplier = multiplier;
        this.incoming = incoming;
    }

    /**
     * Constructs modifier from string.
     * String should be formatted: ZOMBIE-1.2
     * @param strength True if strength, False if weakness
     */
    public Modifier(String s, boolean incoming) {
        String[] split = s.split("-");
        this.type = ModifierType.get(split[0]);
        this.multiplier = Double.valueOf(split[1]);
        this.incoming = incoming;
    }

    /**
     * Sets multiplier to given value
     * @param value Value to be set
     */
    public void setMultiplier(double value) {
        this.multiplier = value;
    }

    /**
     * @return Value of multiplier
     */
    public double getMultiplier() {
        return this.multiplier;
    }
    
    /**
     * @return Modifiers type
     */
    public ModifierType getType() {
        return this.type;
    }

    public Boolean isIncoming() { 
        return this.incoming;
    }

    /**
     * Clones modifier
     * @return Copied modifier
     */
    public Modifier clone() {
        return new Modifier(this.type, this.multiplier, this.incoming);
    }

    /**
     * Calculates damage after modifier
     * @param dam FinalDamage from damage event
     * @return Calculated damage
     */
    public double calculate(double dam) {
        return this.multiplier * dam;
    }

    private String getInfo() {
        String s = "[Modifier Info]: ";
        s += "[Modifier Type] " + this.type.toString() + " ";
        s += "[Multiplier] " + String.valueOf(this.multiplier) + " ";
        s += "[Incoming] " + String.valueOf(this.incoming);
        return s;
    }
}
