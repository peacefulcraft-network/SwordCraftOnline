package net.peacefulcraft.sco.swordskills.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.gson.JsonObject;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.announcer.Announcer;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.CustomDataHolder;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.WeaponAttributeHolder;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
import net.peacefulcraft.sco.swordskills.armorskills.ArmorModifier;
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
     * Holds armor modifiers of user
     * Armor name -> List of armor modifiers
     */
    protected HashMap<String, ArrayList<ArmorModifier>> armorModifiers = new HashMap<>();

    /**
     * Holds any elemental types of user
     * Should be set in MU loading
     */
    protected ArrayList<ModifierType> types = new ArrayList<>();
    
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
        SwordCraftOnline.logDebug("[Modifier User] Modifier " + modifier.toString() + ". Set to " + getModifier(modifier, incoming));

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
        
        // Stripping weapon modifiers on various cases
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
                    }
                }
                if(activeList != null && !activeList.isEmpty()) {
                    for(WeaponModifier wm : activeList) {
                        wm.removeEffects(this);
                    }
                }
                weaponModifiers.clear();
            } else if(wModifiers.isEmpty()) {
                // Case 2: Check passed with no TYPE modifiers in hotbar / hand

                ArrayList<WeaponModifier> wmList = entry.getValue().get(processType);
                if(wmList != null && !wmList.isEmpty()) {
                    for(WeaponModifier wm : wmList) {
                        wm.removeEffects(this);
                    }
                    entry.getValue().remove(processType);
                }
            } else {
                // Case 3: Hotbar passed through. Removing passive and active modifiers not in hotbar

                // If weapon name not in passed data
                if(!wModifiers.keySet().contains(entry.getKey())) {
                    ArrayList<WeaponModifier> passiveList = entry.getValue().get(WeaponModifierType.PASSIVE);
                    ArrayList<WeaponModifier> activeList = entry.getValue().get(WeaponModifierType.ACTIVE);
                    if(passiveList != null && !passiveList.isEmpty()) {
                        for(WeaponModifier wm : passiveList) {
                            wm.removeEffects(this);
                        }
                    }
                    if(activeList != null && !activeList.isEmpty()) {
                        for(WeaponModifier wm : activeList) {
                            wm.removeEffects(this);
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
     * Applies armor modifiers effects onto modifier user
     * Removes old modifiers
     * 
     * @param aModifiers A COMPLETE list of armor modifiers equipped currently
     */
    public void applyArmorModifiers(HashMap<String, ArrayList<ArmorModifier>> aModifiers) {

        // Remove armor modifiers on various cases
        // 1. aModifiers is null, no submitted changes
        // 2. aModifiers is empty.
        // 3. aModifiers passes with data
        Iterator<Entry<String, ArrayList<ArmorModifier>>> iter = this.armorModifiers.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, ArrayList<ArmorModifier>> entry = iter.next();

            if (aModifiers == null || aModifiers.isEmpty()) {
                // Case 1 & 2
                ArrayList<ArmorModifier> lis = entry.getValue();
                if (lis != null && !lis.isEmpty()) {
                    for (ArmorModifier am : lis) {
                        am.removeEffects(this);
                    }
                }
                this.armorModifiers.clear();
            } else {
                // Case 3

                // If armor name not in passed data. Remove effects
                if(!aModifiers.keySet().contains(entry.getKey())) {
                    ArrayList<ArmorModifier> lis = entry.getValue();
                    if (lis != null && !lis.isEmpty()) {
                        for (ArmorModifier am : lis) {
                            am.removeEffects(this);
                        }
                    }
                    iter.remove();
                }
            }
        }

        // Applying any armor effects that are passed through
        if(aModifiers == null || aModifiers.entrySet() == null || aModifiers.entrySet().isEmpty()) { return; }
        Iterator<Entry<String, ArrayList<ArmorModifier>>> iterr = aModifiers.entrySet().iterator();
        while (iterr.hasNext()) {
            Entry<String, ArrayList<ArmorModifier>> entry = iterr.next();

            ArrayList<ArmorModifier> compareLis = this.armorModifiers.get(entry.getKey());
            if (compareLis != null && !compareLis.isEmpty()) { continue; }

            for (ArmorModifier am : entry.getValue()) {
                am.applyEffects(this);
            }
            this.armorModifiers.put(entry.getKey(), entry.getValue());
        }

    }

    /**
     * Handles all damage calculations and inflicting
     * @param damager Opposing modifier user
     */
    private void handleDamageByEvent(EntityDamageByEntityEvent ev, ModifierUser damager) {
        // 1. Calculate parry chance
        // 2. Split damage into typing from damager weapon
        // 2a. Calculate damage of split typings
        // 3. Crit multiplier
        
        double damage = ev.getDamage();

        // 1. Parry.
        // TODO: Effect of "Spiritual, physical, mental"
        if(Parry.parryCalc(damager, this, 0)) {
            ev.setCancelled(true);
            return;
        }

        // 3. Critical Hit on all damage
        double multiplier = CriticalHit.calculateMultiplier(damager, 0, 0);
        if (multiplier != 1.0 && damager instanceof SCOPlayer) {
            Announcer.messagePlayer((SCOPlayer)damager, "Critical Hit!", 0);
        }

        // 2. Splitting outgoing damages
        HashMap<ModifierType, Double> damages = new HashMap<>();
        WeaponAttributeHolder mainHand = damager.getWeaponInHand();
        if (mainHand != null) {
            for (ModifierType type : mainHand.getModifierTypes()) {
                double calcDamage = this.calculateDamage(type, damage, false) * multiplier;
                // 4. Calculate against incoming damage
                calcDamage = this.calculateDamage(type, calcDamage, true);

                damages.put(type, calcDamage);
            }
        }

        // TODO: Add support for off hand

        // 5. Inflict the damage onto the user.
        // We sum the damage values and deal 1
        // total damage packet
        double total = 0.0;
        for (double i : damages.values()) {
            total += i;
        }
        this.convertHealth(total, true);
    }

    /**
     * Handles all damage events on ModifierUser
     * 
     * @param ev Damage event
     */
    public void handleDamageByEvent(EntityDamageEvent ev) {
        if (ev instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent evv = (EntityDamageByEntityEvent)ev;
            ModifierUser damager = ModifierUser.getModifierUser(evv.getDamager());
            if (damager == null) { return; }

            this.handleDamageByEvent(evv, damager);
            return;
        }

        // Non-entity events are converted to elemental modifiers
        // and totaled to player.
        double damage = 0.0;
        for (ModifierType type : ModifierType.get(ev.getCause())) {
            damage += this.calculateDamage(type, ev.getDamage(), true);
        }
        this.convertHealth(damage, true);
    } 

    /**
     * Checks users modifiers for match
     * @param type Type of damage
     * @param damage FinalDamage from event
     * @return modified damage
     */
    public double calculateDamage(ModifierType type, double damage, boolean incoming) {
        ModifierType[] primaryComp = ModifierType.getPrimaryModifierTypes(type);
        if (primaryComp != null) {
            double dam1 = this.calculateDamage(primaryComp[0], damage, incoming);
            double dam2 = this.calculateDamage(primaryComp[1], damage, incoming);
            return ((dam1 + dam2) / 2) * 1.66;
        }
        
        Modifier m = getModifier(type, incoming);
        if(m == null) { return damage; }

        return m.calculate(damage);
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
        Modifier m = getModifier(type, incoming);
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
	public Modifier getModifier(ModifierType type, boolean incoming) {
        try {
            return this.damageModifiers.get(type).get(incoming);
        } catch(NullPointerException ex) {
            return null;
        }
    }
    
    /**
     * Fetches the users multiplier of this type
     * @param type Type of modifier
     * @param incoming Is incoming
     * @return Multiplier value or 1.0
     */
    public double getMultiplier(ModifierType type, boolean incoming) {
        Modifier m = getModifier(type, incoming);
        return m == null ? 1.0 : m.getMultiplier();
    }

    /**
     * Fetches the users multiplier of average of multiple types
     * @param types Types we want average of
     * @param incoming Is incoming
     * @param bonus Bonus multiplier multiplied against average
     * @return
     */
    public double getMultiplier(ModifierType[] types, boolean incoming) {
        double sum = 0.0;
        int i = 0;
        for (ModifierType type : types) {
            sum += getMultiplier(type, incoming);
            i++;
        }
        return sum / i;
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
            getLivingEntity().setHealth(health);
        }

        return health;
    }

    /*
     *
     * ===============================================================================================
     * MODIFIER USER HELPER METHODS
     * ===============================================================================================
     * 
     */

    /**
     * Fetches the weapon attribute holder in the main hand
     * @return Weapon attribute holder if exists, null otherwise
     */
    protected WeaponAttributeHolder getWeaponInHand() {
        ItemIdentifier ident = ItemIdentifier.resolveItemIdentifier(
            getLivingEntity().getEquipment().getItemInMainHand()
        );

        if (ident.getMaterial().equals(Material.AIR)) { return null; }
        if (ident instanceof CustomDataHolder) {
            JsonObject obj = ((CustomDataHolder)ident).getCustomData();
            if (obj.get("weapon") != null) {
                return (WeaponAttributeHolder)ident;
            }
        }
        return null;
    }

    /**
     * Fetches the weapon attribute holder in the off hand
     * @return Weapon attribute holder if exists, null otherwise
     */
    protected WeaponAttributeHolder getWeaponInOffHand() {
        ItemIdentifier ident = ItemIdentifier.resolveItemIdentifier(
            getLivingEntity().getEquipment().getItemInOffHand()
        );

        if (ident.getMaterial().equals(Material.AIR)) { return null; }
        if (ident instanceof CustomDataHolder) {
            JsonObject obj = ((CustomDataHolder)ident).getCustomData();
            if (obj.get("weapon") != null) {
                return (WeaponAttributeHolder)ident;
            }
        }
        return null;
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
