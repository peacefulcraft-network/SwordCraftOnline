package net.peacefulcraft.sco.swordskills.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.gson.JsonObject;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;
import net.peacefulcraft.sco.swordskills.weaponskills.WeaponModifier;
import net.peacefulcraft.sco.swordskills.weaponskills.WeaponModifier.WeaponModifierType;

/**
 * Holds necessary information for SCOPlayers and AciveMobs
 * Attribute changing methods and damage modifier methods
 */
public class ModifierUser {

    /**List of Modifiers user has */
    //private List<Modifier> damageModifiers = new ArrayList<>();
    protected HashMap<ModifierType, HashMap<Boolean, Modifier>> damageModifiers = new HashMap<>();

    /**Instance of living entity using this class */
    private LivingEntity entity;

    /**
     * Health attribute of user. 
     * This is modified instead of max health attribute
     * TODO: Remove 20 base value and set from data load
     */
    private Integer maxHealth = 20;

    /**
     * Current health of user
     * TODO: Remove 20 base value
     */
    private Integer currHealth = 20;

    /**CombatModifier: Chance user lands critical hit */
    protected int criticalChance = 2;
    
    /**CombatModifier: Additional damage dealt on critical hit */
    protected double criticalMultiplier = 1.2;

    /**CombatModifier: Chance user lowers incoming damage */
    protected int parryChance = 2;

    /**CombatModifier: Damage dampener on incoming damage */
    protected double parryMultiplier = 1.2;

    /**
     * Holds weapon modifiers of user 
     * Weapon name -> Map of modifier by passve / active
     */
    protected HashMap<String, HashMap<WeaponModifierType, ArrayList<WeaponModifier>>> weaponModifiers = new HashMap<>();
    
    /**
     * Fetches implementing sub class living entity
     * @return LivingEntity instance
     */
    public LivingEntity getLivingEntity() { return this.entity; }

    /**
     * Checks if user is dead
     * @return True if user is dead, false otherwise
     */
    public boolean isDead() {
        return getLivingEntity().isDead();
    }

    /**
     * Queued up stat changes for user
     */
    private HashMap<UUID, JsonObject> queuedChanges = new HashMap<>();

    /**
     * Queue an attribute data field change
     * @param amount we are changing the field by
     * @param duration in seconds we are doing the change for. -1 for permanent change.
     * @return UUID of change in queue
     */
    public UUID queueChange(Attribute attribute, Double amount, int duration) {
        UUID id = UUID.randomUUID();
        JsonObject obj = _queueChange_(
            attribute.toString(), 
            "ATTRIBUTE", 
            amount, 
            null
        );
        this.queuedChanges.put(id, obj);
        addToAttribute(attribute, amount, duration, id);
        SwordCraftOnline.logDebug("[Modifier User] Attribute " + attribute.toString() + ". Set to " + getAttribute(attribute));

        return id;
    }

    /**
     * Queue an attribute data field change
     * @param amount we are changing the field by
     * @param duration in seconds we are doing the change for. -1 for permanent change.
     * @return UUID of change in queue
     */
    public UUID queueChange(Attribute attribute, Integer amount, int duration) {
        return queueChange(attribute, Double.valueOf(amount), duration);
    }

    /**
     * Queue an damage modifier data field change
     * @param amount we are changing the field by
     * @param duration in seconds we are doing the change for. -1 for permanent change.
     * @return UUID of change in queue
     */
    public UUID queueChange(ModifierType modifier, Double amount, boolean incoming, int duration) {
        UUID id = UUID.randomUUID();
        JsonObject obj = _queueChange_(
            modifier.toString(), 
            "DAMAGE_MODIFIER", 
            amount, 
            null
        );
        obj.addProperty("incoming", incoming);
        this.queuedChanges.put(id, obj);
        addToMultiplier(modifier, incoming, amount, duration, id);
        SwordCraftOnline.logDebug("[Modifier User] Modifier " + modifier.toString() + ". Set to " + getDamageModifier(modifier, incoming));

        return id;
    }

    /**
     * Queue n combat modifier data field change
     * @param amount we are changing the field by
     * @param duration in seconds we are doing the change for. -1 for permanent change.
     * @return UUID of change in queue
     */
    public UUID queueChange(CombatModifier modifier, Double amount, int duration) {
        UUID id = UUID.randomUUID();
        JsonObject obj = _queueChange_(
            modifier.toString(), 
            "COMBAT_MODIFIER", 
            amount, 
            null
        );
        this.queuedChanges.put(id, obj);
        addToCombatModifier(modifier, amount, duration, id);
        SwordCraftOnline.logDebug("[Modifier User] Combat Modifier " + modifier.toString() + ". Set to " + getCombatModifier(modifier));

        return id;
    }

    /**
     * Queue n combat modifier data field change
     * @param amount we are changing the field by
     * @param duration in seconds we are doing the change for. -1 for permanent change.
     * @return UUID of change in queue
     */
    public UUID queueChange(CombatModifier modifier, Integer amount, int duration) {
        return queueChange(modifier, Double.valueOf(amount), duration);
    }

    /**
     * Queue a max health data field change
     * @param amount we are changing the field by
     * @param duration in seconds we are doing the change for. -1 for permanent change.
     * @return UUID of change in queue
     */
    public UUID queueChange(Integer healthAmount, int duration) {
        UUID id = UUID.randomUUID();
        JsonObject obj = _queueChange_(
            "MAX_HEALTH", 
            "MAX_HEALTH", 
            healthAmount, 
            getMaxHealth()
        );
        this.queuedChanges.put(id, obj);
        addToMaxHealth(healthAmount, duration, id);
        SwordCraftOnline.logDebug("[Modifier User] Max Health set to:" + getMaxHealth());

        return id;
    }

    private JsonObject _queueChange_(String changedField, String fieldType, Integer value, Integer baseValue) {
        return _queueChange_(changedField, fieldType, (double)value, baseValue);
    }

    private JsonObject _queueChange_(String changedField, String fieldType, Double value, Integer baseValue) {
        JsonObject obj = new JsonObject();
        obj.addProperty("changed_field", changedField);
        obj.addProperty("changed_field_type", fieldType);
        obj.addProperty("changed_value", value);
        if(baseValue != null) {
            obj.addProperty("base_value", baseValue);
        }
        return obj;
    }

    /**
     * Dequeues and reverts data field change
     * @param id of change in queue
     */
    public void dequeueChange(UUID id) {
        JsonObject obj = this.queuedChanges.get(id);
        if(obj == null) { 
            SwordCraftOnline.logDebug("No matching UUID in Modifier User Queued Changes.");
            return;
        }

        String changedField = obj.get("changed_field").getAsString();
        String changedFieldType = obj.get("changed_field_type").getAsString();
        double changedValue = obj.get("changed_value").getAsDouble();

        if(changedFieldType.equalsIgnoreCase("ATTRIBUTE")) {
            addToAttribute(Attribute.valueOf(changedField), -changedValue, -1, id);
        } else if(changedFieldType.equalsIgnoreCase("COMBAT_MODIFIER")) {
            addToCombatModifier(CombatModifier.valueOf(changedField), -changedValue, -1, id);
        } else if(changedField.equalsIgnoreCase("DAMAGE_MODIFIER")) {
            boolean incoming = obj.get("incoming").getAsBoolean();
            addToMultiplier(ModifierType.valueOf(changedField), incoming, -changedValue, -1, id);
        } else if(changedField.equalsIgnoreCase("MAX_HEALTH")) {
            addToMaxHealth(-((int) changedValue), -1, id);
        }else {
            SwordCraftOnline.logDebug("No matching field change for obj in Modifier User.");
            return;
        }
        SwordCraftOnline.logDebug("[ModifierUser] Change dequeued.");

        this.queuedChanges.remove(id);
    }

    /**
     * Applies weapon modifiers to user. 
     * Removes old modifiers.
     * 
     * @param wModifiers Map by Weapon name -> List of weapon modifier
     * if passed value is empty or null all weapon modifiers are removed and cleared
     */
    public void applyWeaponModifiers(HashMap<String, ArrayList<WeaponModifier>> wModifiers, WeaponModifierType processType) {
        //SwordCraftOnline.logDebug("wModifiers value: " + wModifiers + ", processType: " + processType);

        Iterator<Entry<String, HashMap<WeaponModifierType, ArrayList<WeaponModifier>>>> iter = weaponModifiers.entrySet().iterator();
        while(iter.hasNext()) {
            Entry<String, HashMap<WeaponModifierType, ArrayList<WeaponModifier>>> entry = iter.next();

            if(wModifiers == null) {
                // Case 1: Hot bar weapon empty, wModifiers null

                ArrayList<WeaponModifier> passiveList = entry.getValue().get(WeaponModifierType.PASSIVE);
                ArrayList<WeaponModifier> activeList = entry.getValue().get(WeaponModifierType.ACTIVE);
                if(passiveList != null && !passiveList.isEmpty()) {
                    for(WeaponModifier wm : passiveList) {
                        wm.removeEffects(this);
                        //SwordCraftOnline.logDebug("Case 1, cleared passive weapon modifier: " + WeaponModifier.parseName(wm));
                    }
                }
                if(activeList != null && !activeList.isEmpty()) {
                    for(WeaponModifier wm : activeList) {
                        wm.removeEffects(this);
                        //SwordCraftOnline.logDebug("Case 1, cleared active weapon modifier: " + WeaponModifier.parseName(wm));
                    }
                }
                weaponModifiers.clear();
            } else if(wModifiers.isEmpty()) {
                // Case 2: Check passed with no TYPE modifiers in hotbar / hand

                ArrayList<WeaponModifier> wmList = entry.getValue().get(processType);
                if(wmList != null && !wmList.isEmpty()) {
                    for(WeaponModifier wm : wmList) {
                        wm.removeEffects(this);
                        //SwordCraftOnline.logDebug("Case 2, cleared " + processType.toString() + " weapon modifier: "+ WeaponModifier.parseName(wm));
                    }
                    entry.getValue().remove(processType);
                }
            } else {
                // Case 3: Hotbar passed through. Removing passive and active modifiers not in hotbar

                if(!wModifiers.keySet().contains(entry.getKey())) {
                    ArrayList<WeaponModifier> passiveList = entry.getValue().get(WeaponModifierType.PASSIVE);
                    ArrayList<WeaponModifier> activeList = entry.getValue().get(WeaponModifierType.ACTIVE);
                    if(passiveList != null && !passiveList.isEmpty()) {
                        for(WeaponModifier wm : passiveList) {
                            wm.removeEffects(this);
                            //SwordCraftOnline.logDebug("Case 3, removed passive effects of: " + WeaponModifier.parseName(wm));
                        }
                    }
                    if(activeList != null && !activeList.isEmpty()) {
                        for(WeaponModifier wm : activeList) {
                            wm.removeEffects(this);
                            //SwordCraftOnline.logDebug("Case 3, active removed effects of: " + WeaponModifier.parseName(wm));
                        }
                    }
                    iter.remove();
                }
            }
        }

        // Applying non-applied weapon modifiers to player
        if(wModifiers == null || wModifiers.entrySet() == null || wModifiers.entrySet().isEmpty()) { return; }
        Iterator<Entry<String, ArrayList<WeaponModifier>>> iterr = wModifiers.entrySet().iterator();
        while(iterr.hasNext()) {
            Entry<String, ArrayList<WeaponModifier>> entryy = iterr.next();

            HashMap<WeaponModifierType, ArrayList<WeaponModifier>> compareMap = weaponModifiers.get(entryy.getKey());
            if(compareMap != null && compareMap.get(processType) != null) {
               continue;
            }

            for(WeaponModifier wm : entryy.getValue()) {
                wm.applyEffects(this);
            }
            if(!weaponModifiers.containsKey(entryy.getKey())) {
                weaponModifiers.put(entryy.getKey(), new HashMap<>());
            }
            weaponModifiers.get(entryy.getKey()).put(processType, entryy.getValue());
        }
    }

    /**
     * Calculates potential damage on modifier user
     * @param type
     * @param damage
     * @param incoming
     * @return
     */
    public double calculateDamage(String type, double damage, boolean incoming) {
        if (ModifierType.get(type) == null) { return damage; }

        return calculateDamage(ModifierType.get(type), damage, incoming);
    }

    /**
     * Calculates potential damage on modifier user
     * @param type Type of damage
     * @param damage FinalDamage from event
     * @return modified damage
     */
    public double calculateDamage(ModifierType type, double damage, boolean incoming) {
        Modifier m = getDamageModifier(type, incoming);
        if(m == null) { return damage; }

        SwordCraftOnline.logDebug("[ModifierUser] Calculating damage on: " + type.toString());
        double dam = m.calculate(damage);
        return dam;
    }

    /**
     * Checks users modifiers for match
     * @param type Type of damage in string
     * @param damage FinalDamage from event
     * @return modified damage
     */
    public double checkModifier(String type, double damage, boolean incoming) {
        try{
            return calculateDamage(ModifierType.valueOf(type), damage, incoming);
        } catch(IllegalArgumentException ex) {
            SwordCraftOnline.logDebug("[ModifierUser] IllegalArgumentException thrown in checkModifier method. Input: " + type);
            return damage;
        }
    }

    /**
     * Temporarily adds to value of desired multiplier
     * Permenantly sets value with duration -1
     * 
     * @param type
     * @param incoming
     * @param value
     * @param duration
     */
    private void addToMultiplier(ModifierType type, boolean incoming, double value, int duration, UUID id) {
        Modifier m = getDamageModifier(type, incoming);
        value = m != null ? m.getMultiplier() + value : 1 + value;

        if(m == null) {
            HashMap<Boolean, Modifier> inputMap = new HashMap<>();
            inputMap.put(incoming, new Modifier(type, value, incoming));
            this.damageModifiers.put(type, inputMap);
        } else {
            m.setMultiplier(value);
        }

        if(duration != -1) {
            double copy = m.getMultiplier();
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), new Runnable() {
                public void run() {
                    addToMultiplier(type, incoming, copy, -1, id);
                    dequeueChange(id);
                }
            }, duration * 20);
        }
    }

	/**
	 * Searches for modifier of same type
     * Note: returns a clone wanted modifier
	 * @param type Type to be searched for
	 * @return Clone of Modifier if found. Null otherwise
	 */
	public Modifier getDamageModifier(ModifierType type, boolean incoming) {
        try {
            return this.damageModifiers.get(type).get(incoming);
        } catch(NullPointerException ex) {
            return null;
        }
	}

    /*
     * 
     * ===============================================================================================
     * MODIFIER USER ATTRIBUTE HANDLING
     * ===============================================================================================
     * 
     */

    /**
     * Fetches mobs attribute base value
     * @param attribute Attribute to be searched for
     * @return Double value of attribute
     */
    public double getAttribute(Attribute attribute) {
        return getLivingEntity().getAttribute(attribute).getBaseValue();
    }

    /**
     * Adds a double value to a set attribute
     * @param attribute Attribute to be set
     * @param amount Value to be added. I.e. 0.2, 0.4, 1, etc.
     * @param duration Resets value after this time in seconds. If -1 it does not
     */
    private void addToAttribute(Attribute attribute, double amount, int duration, UUID id) {
        double d = getAttribute(attribute);

        if(d + amount <= 0.0) {
            getLivingEntity().getAttribute(attribute).setBaseValue(0.0);
        } else {
            getLivingEntity().getAttribute(attribute).setBaseValue(d + amount);
        }
        if(duration != -1) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), new Runnable() {
                public void run() {
                    dequeueChange(id);
                }
            }, duration * 20);
        }
    }

    /*
     * 
     * ===============================================================================================
     * MODIFIER USER COMBAT MODIFIER HANDLING
     * ===============================================================================================
     * 
     */

    /**
     * Fetches combat modifier value
     * @param mod CombatModifier we want
     * @return value of modifier, -1 default.
     */
    public double getCombatModifier(CombatModifier mod) {
        switch(mod) {
            case CRITICAL_CHANCE:
                return criticalChance;
            case CRITICAL_MULTIPLIER:
                return criticalMultiplier;
            case PARRY_CHANCE:
                return parryChance;
            case PARRY_MULTIPLIER:
                return parryMultiplier;
            default:
                return -1;
        }
    }

    /**
     * Sets CombatModifier to value for duration
     * @param mod Modifier we want to set
     * @param amount Amount to be set
     * @param duration Resets value after this time in seconds. If -1 it does not.
     */
    protected void setCombatModifier(CombatModifier mod, double amount, int duration, UUID id) {
        switch(mod) {
            case CRITICAL_CHANCE:
                criticalChance = (int)amount;
            case CRITICAL_MULTIPLIER:
                criticalMultiplier = amount;
            case PARRY_CHANCE:
                parryChance = (int)amount;
            case PARRY_MULTIPLIER:
                parryMultiplier = amount;
            default:
                SwordCraftOnline.logInfo("[Modifier User] Attempted to set player specific combat modifier on super class. " + mod.toString());
        }

        if(duration != -1) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), new Runnable() {
                public void run() {
                    dequeueChange(id);
                }
            }, duration * 20);
        }
    }

    /**
     * Adds a double value to given combat modifier
     * @param mod CombatModifier we want to adjust
     * @param amount Amount to be multiplied by
     * @param duration Resets value after this time in seconds. If -1 it does not.
     */
    protected void addToCombatModifier(CombatModifier mod, double amount, int duration, UUID id) {
        switch(mod) {
            case CRITICAL_CHANCE:
                criticalChance += (int)amount;
            case CRITICAL_MULTIPLIER:
                criticalMultiplier += amount;
            case PARRY_CHANCE:
                parryChance += (int)amount;
            case PARRY_MULTIPLIER:
                parryMultiplier += amount;
            default:
                SwordCraftOnline.logInfo("[Modifier User] Attempted to set player specific combat modifier on super class. " + mod.toString());
        }

        if(duration != -1) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), new Runnable() {
                public void run() {
                    dequeueChange(id);
                }
            }, duration * 20);
        }
    }

    /**
     * Combat modifiers used by both Active Mobs and Players
     */
    public enum CombatModifier {
        /**Critical hit chance of user */
        CRITICAL_CHANCE, 
        /**Critical damage multiplier of user */
        CRITICAL_MULTIPLIER, 
        /**Parry chance of user */
        PARRY_CHANCE,
        /**Parry damage dampener multiplier of user */ 
        PARRY_MULTIPLIER,
        /**Additional chance to increase item level on drop */
        ITEM_LEVEL,
        /**Additional chance to get more items on drop */ 
        BONUS_DROP, 
        /**Additional chance to get more exp on mob kill */
        BONUS_EXP;
    }

    /*
     * 
     * ===============================================================================================
     * MODIFIER USER HEALTH HANDLING
     * ===============================================================================================
     * 
     */

    /**
     * Gets max health of this user 
     * @return Integer of max health value
     */
    public Integer getMaxHealth() {
        return this.maxHealth;
    }

    /**
     * Gets current health of user
     * @return Integer of health value
     */
    public Integer getHealth() {
        return this.currHealth;
    }

    public void setHealth(Integer amount) {
        this.currHealth = amount >= this.maxHealth ? this.maxHealth : amount;
        double ratio = this.currHealth / this.maxHealth;
        double health = getAttribute(Attribute.GENERIC_MAX_HEALTH) * ratio;

        getLivingEntity().setHealth(health);
    }

    private void addToMaxHealth(Integer amount, int duration, UUID id) {
        this.maxHealth += amount;
        if(this.currHealth > this.maxHealth) {
            this.currHealth = this.maxHealth;
            getLivingEntity().setHealth(20);
        }
        if(duration != -1) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), new Runnable() {
                public void run() {
                    dequeueChange(id);
                }
            }, duration * 20);
        }
    }

    /**
     * Sets max health of user.
     * @param amount Amount we set max health to
     * @param duration Duration in seconds of change, -1 if no timer
     */
    protected void setMaxHealth(Integer amount, int duration) {
        int copy = this.maxHealth;

        this.maxHealth = amount;
        if(this.currHealth > this.maxHealth) {
            this.currHealth = this.maxHealth;
            getLivingEntity().setHealth(20);
        }
        if(duration != -1) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), new Runnable() {
                public void run() {
                    setMaxHealth(copy, -1);
                }
            }, duration * 20);
        }
    }

    /**
     * Converts users custom health values to
     * their Attribute.MAX_HEALTH scale
     * @param damage Incoming damage from event
     * @param set If true, sets users health and living entity health
     * @return Amount of correct damage to be set on health bar
     */
    public Double convertHealth(double damage, boolean set) {
        return convertHealth(damage, set, false);
    }

    /**
     * Converts users custom health values to
     * their Attribute.MAX_HEALTH scale
     * @param damage Incoming damage from event
     * @param set If true, sets users health and living entity health
     * @param isRegain Determines if health regain event
     * @return Amount of correct damage to be set on health bar
     */
    public Double convertHealth(double damage, boolean set, boolean isRegain) {
        // Ratio of current health to max health
        // Ex: (100-2) / 100 = 0.98
        // DisplayedHealth = 20 * ratio
        // DisplayedHealth = 20 * ( (currHealth - damage) / maxhealth )
        // currHealth = (displayed * max) / 20 + damage
        
        //double displayed = this.entity.getHealth();
        int damagee = (int)damage;
        double ratio = (this.currHealth - damage) / this.maxHealth;
        double health = ratio > 1.0 ? 20.0 : getAttribute(Attribute.GENERIC_MAX_HEALTH) * ratio;
        health = health < 0 ? 0 : health;
        if(set) {
            this.currHealth = this.currHealth - damagee < 0 ? 0 : this.currHealth - damagee;
            this.currHealth = this.currHealth > this.maxHealth ? this.maxHealth : this.currHealth;
            SwordCraftOnline.logDebug("[ModifierUser] Calc: " + health + ", Curr: " + getLivingEntity().getHealth());
            if ((isRegain && health >= getLivingEntity().getHealth()) || !isRegain) {
                getLivingEntity().setHealth(health);
                SwordCraftOnline.logDebug("[ModifierUser] Set health: " + getLivingEntity().getHealth());
            } 
        }

        return health;
    }

    /*
     * 
     * ===============================================================================================
     * MODIFIER USER STATIC METHODS
     * ===============================================================================================
     * 
     */

    /**
     * Given entity we return the Modifier User
     * or null if not valid.
     * 
     * @param e
     * @return
     */
    public static ModifierUser getModifierUser(Entity e) {
        if(e instanceof LivingEntity) {
            if(e instanceof Player) {
                SCOPlayer s = GameManager.findSCOPlayer((Player)e);
                if(s == null) { return null; }
                return (ModifierUser) s;
            } else {
                ActiveMob am = SwordCraftOnline.getPluginInstance()
                    .getMobManager()
                    .getMobRegistry()
                    .get(e.getUniqueId());
                if(am == null) { return null; }
                return (ModifierUser) am;
            }
        }
        return null;
    }

    /**
     * Gets constant base damage attribute
     * for all mobs
     */
    public static Double getBaseGenericAttack() {
        return 2.0;
    }

    /**
     * Gets constant base movement attribute
     * for all used mobs
     */
    public static Double getBaseGenericMovement(ModifierUser mu) {
        /**
         * This code is gross but is a workaround for
         * having to temporarily spawn in a new entity
         * every time we do a check.
         * 
         * Solution is to hard code these values to check against
         */

        if(mu instanceof SCOPlayer) {
            return 0.1;
        }

        ActiveMob am = (ActiveMob)mu;
        switch(am.getEntity().getBukkitEntity().getType()) {
            case PANDA:
                return 0.15;
            case DONKEY:
            case LLAMA:
            case MULE:
            case STRIDER:
                return 0.175;
            case SLIME:
                int size = ((Slime)am.getEntity().getBukkitEntity()).getSize();
                return 0.2 + (0.1 * size);
            case COW:
            case MAGMA_CUBE:
            case MUSHROOM_COW:
            case PARROT:
            case SKELETON_HORSE:
            case SNOWMAN:
            case ZOMBIE_HORSE:
                return 0.2;
            case BLAZE:
            case DROWNED:
            case HUSK:
            case SHEEP:
            case ZOMBIE:
            case ZOMBIE_VILLAGER:
            case ZOMBIFIED_PIGLIN:
                return 0.23;
            case CHICKEN:
            case CREEPER:
            case ENDERMITE:
            case IRON_GOLEM:
            case PIG:
            case POLAR_BEAR:
            case SILVERFISH:
            case SKELETON:
            case STRAY:
            case TURTLE:
            case WITCH:
            case WITHER_SKELETON:
                return 0.25;
            case BEE:
            case CAT:
            case CAVE_SPIDER:
            case ELDER_GUARDIAN:
            case ENDERMAN:
            case FOX:
            case OCELOT:
            case RABBIT:
            case RAVAGER:
            case SPIDER:
            case WOLF:
                return 0.3;
            case PILLAGER:
            case VINDICATOR:
                return 0.35;
            case HOGLIN:
                return 0.4;
            case EVOKER:
            case GIANT:
            case GUARDIAN:
            case ILLUSIONER:
            case PIGLIN:
            case VILLAGER:
            case WANDERING_TRADER:
                return 0.5;
            case WITHER:
                return 0.6;
            case BAT:
            case COD:
            case ENDER_DRAGON:
            case GHAST:
            case PUFFERFISH:
            case SALMON:
            case SHULKER:
            case SQUID:
            case TROPICAL_FISH:
            case VEX:
                return 0.7;
            case DOLPHIN:
                return 1.2;
            default:
                return 0.0;
        }
    }
}
