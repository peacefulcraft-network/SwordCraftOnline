package net.peacefulcraft.sco.swordskills.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

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
    private List<Modifier> damageModifiers = new ArrayList<>();

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

    /**Holds weapon modifiers of user */
    protected HashMap<String, HashMap<WeaponModifierType, ArrayList<WeaponModifier>>> weaponModifiers = new HashMap<>();
    
    /**
     * Fetches implementing sub class living entity
     * @return LivingEntity instance
     */
    public LivingEntity getLivingEntity() { return this.entity; }

    /**
     * Fetches copy of damage modifiers
     * @return Unmodifiable copy of DamageModifiers list
     */
    public List<Modifier> getDamageModifiers() { return Collections.unmodifiableList(this.damageModifiers); }

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
        if(wModifiers.entrySet().isEmpty()) { return; }
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
     * Checks users modifiers for match
     * @param type Type of damage
     * @param damage FinalDamage from event
     * @return modified damage
     */
    public double calculateDamage(ModifierType type, double damage, boolean incoming) {
        Modifier m = getDamageModifier(type, incoming);
        if(m == null) { return damage; }

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
            SwordCraftOnline.logDebug("[ModifierUser] IllegalArgumentException thrown in checkModifier method.");
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
    public void addToMultiplier(ModifierType type, boolean incoming, double value, int duration) {
        Modifier m = getDamageModifier(type, incoming);
        if(m != null) {
            setMultiplier(type, incoming, m.getMultiplier() + value, duration);
        } else {
            setMultiplier(type, incoming, value, duration);
        }
    }

    /**
     * Temporarily set value of multiplier to value.
     * Also used to permanently set value via duration value.
     * @param type ModifierType to be searched for
     * @param multiplier Value to be set
     * @param duration Duration of set, if -1 it is permanent.
     */
    public void setMultiplier(ModifierType type, boolean incoming, double multiplier, int duration) {
        Double initial = null;
        for(Modifier m : this.damageModifiers) {
			if(m.getType().equals(type) && m.isIncoming() == incoming) {
                initial = m.getMultiplier();
				m.setMultiplier(multiplier);
				break;
			}
        }
        if(initial == null && type != null) {
            addDamageModifier(new Modifier(type, multiplier, incoming));
        }
        
        // If not perm change and we found the modifier
        if(duration != -1 && initial != null) {
            double copy = initial;
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), new Runnable() {
                public void run() {
                    setMultiplier(type, incoming, copy, -1);
                }
            }, duration * 20);
        }
    }

    /**
     * Temporarily sets multiplier value of all modifiers
     * @param multipler Value to be set
     * @param duration Duration of change, if -1 change is permanent
     */
    public void setAllMultipliers(double multipler, int duration) {
        List<Modifier> copy = new ArrayList<Modifier>(this.damageModifiers);
        for(Modifier m : this.damageModifiers) {
            m.setMultiplier(multipler);
        }

        if(duration != -1) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), new Runnable() {
                public void run() {
                    addDamageModifiers(copy);
                }
            }, duration * 20);
        }
    }
    
    /**
     * Adds list of modifiers to list
     * @param lis List of modifiers to be added
     */
    public void addDamageModifiers(List<Modifier> lis) {
        for(Modifier m : lis) {
            addDamageModifier(m);
        }
    }

	/**
	 * Safely adds modifier to list. 
	 * If modifier already contains modifier we replace it
	 * @param m Modifier to be added.
	 */
	public void addDamageModifier(Modifier m) { 
		removeDamageModifier(m, m.isIncoming());
		this.damageModifiers.add(m);
	}

	/**
	 * Safely removes modifier of same type from list
	 * @param m Modifier to be removed
	 */
	public void removeDamageModifier(Modifier m, boolean incoming) {
		removeDamageModifier(m.getType(), incoming);
	}

	/**
	 * Safely removes modifier of same type from list
	 * @param type Modifier to be removed
	 */
	public void removeDamageModifier(ModifierType type, boolean incoming) {
		// If modifier of same type exists we remove
		Iterator<Modifier> iter = this.damageModifiers.iterator();
		while(iter.hasNext()) {
			Modifier mod = iter.next();
			if(mod.getType().equals(type) && mod.isIncoming() == incoming) {
				iter.remove();
			}
		}
	}

	/**
	 * Searches for modifier of same type
     * Note: returns a clone wanted modifier
	 * @param type Type to be searched for
	 * @return Clone of Modifier if found. Null otherwise
	 */
	public Modifier getDamageModifier(ModifierType type, boolean incoming) {
		// If type exists we return
		for(Modifier m : this.damageModifiers) {
			if(m.getType().equals(type)) {
				return m.clone();
			}
		}
		return null;
	}

	/**
	 * Searches for modifier of same type
	 * @param m Modifier to search for same type
	 * @return Modifier of type if found. Null otherwise
	 */
	public Modifier getDamageModifier(Modifier m, boolean incoming) {
		return getDamageModifier(m.getType(), incoming);
    }
    
    /**
     * Clears the modifier list
     */
    public void clearList() {
        this.damageModifiers.clear();
    }

    /**
     * Temporarily removes modifier from list
     * @param type Type of modifier to be removed
     * @param duration Time in seconds for modifier to be removed
     */
    public void disableModifier(ModifierType type, int duration, boolean incoming) {
        // Copying modifier we want and removing it
        Modifier m = getDamageModifier(type, incoming);
        removeDamageModifier(type, incoming);

        // Adding modifier back after duration in ticks
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), new Runnable() {
            public void run() {
                addDamageModifier(m);
            }
        }, duration * 20);
    }

    /**
     * Temporarily removes all modifiers
     * @param duration Time in seconds for list to be cleared
     */
    public void disableAllModifiers(int duration) {
        // Cloning list and clearing
        List<Modifier> clone = new ArrayList<Modifier>(this.damageModifiers);
        clearList();

        // Adding modifiers back after duration in ticks
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), new Runnable() {
            public void run() {
                addDamageModifiers(clone);
            }
        }, duration * 20);
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
     * Sets given attribute to value
     * @param attribute Attribute to be set
     * @param amount Value to be set
     * @param duration Resets value after this time in seconds. If -1 it does not
     */
    public void setAttribute(Attribute attribute, double amount, int duration) {
        double d = getAttribute(attribute);

        getLivingEntity().getAttribute(attribute).setBaseValue(amount);
        if(duration != -1) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), new Runnable() {
                public void run() {
                    setAttribute(attribute, d, -1);
                }
            }, duration * 20);
        }
    }

    /**
     * Multiplies given attribute by amount
     * @param attribute Attribute to be set
     * @param amount Value to be set
     * @param duration Resets value after this time in seconds. If -1 it does not
     */
    public void multiplyAttribute(Attribute attribute, double amount, int duration) {
        double d = getAttribute(attribute);

        getLivingEntity().getAttribute(attribute).setBaseValue(amount * getAttribute(attribute));
        if(duration != -1) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), new Runnable() {
                public void run() {
                    setAttribute(attribute, d, -1);
                }
            }, duration * 20);
        }
    }

    /**
     * Adds a double value to a set attribute
     * @param attribute Attribute to be set
     * @param amount Value to be added. I.e. 0.2, 0.4, 1, etc.
     * @param duration Resets value after this time in seconds. If -1 it does not
     */
    public void addToAttribute(Attribute attribute, double amount, int duration) {
        double d = getAttribute(attribute);

        getLivingEntity().getAttribute(attribute).setBaseValue(d + amount);
        if(duration != -1) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), new Runnable() {
                public void run() {
                    setAttribute(attribute, d, -1);
                }
            }, duration * 20);
        }
    }

    /**
     * Checks if user is dead
     * @return True if user is dead, false otherwise
     */
    public boolean isDead() {
        return getLivingEntity().isDead();
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
     * Helper method for delayed resetting of attributes to a value
     * @param mod Modifier we want to set
     * @param amount Amount to be set
     * @param duration Time in seconds for value to be set
     */
    protected void _setCombatModifier(CombatModifier mod, double amount, int duration) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), new Runnable() {
            public void run() {
                setCombatModifier(mod, amount, -1);
            }
        }, duration * 20);
    }

    /**
     * Sets CombatModifier to value for duration
     * @param mod Modifier we want to set
     * @param amount Amount to be set
     * @param duration Resets value after this time in seconds. If -1 it does not.
     */
    public void setCombatModifier(CombatModifier mod, double amount, int duration) {
        double d = getCombatModifier(mod);

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
            _setCombatModifier(mod, d, duration);
        }
    }

    /**
     * Multiplies given combat modifier by amount.
     * @param mod CombatModifier we want to adjust
     * @param amount Amount to be multiplied by
     * @param duration Resets value after this time in seconds. If -1 it does not.
     */
    public void multiplyCombatModifier(CombatModifier mod, double amount, int duration) {
        double d = getCombatModifier(mod);

        setCombatModifier(mod, d * amount, -1);
        if(duration != -1) {
            _setCombatModifier(mod, d, duration);
        }
    }

    /**
     * Adds a double value to given combat modifier
     * @param mod CombatModifier we want to adjust
     * @param amount Amount to be multiplied by
     * @param duration Resets value after this time in seconds. If -1 it does not.
     */
    public void addToCombatModifier(CombatModifier mod, double amount, int duration) {
        double d = getCombatModifier(mod);
        setCombatModifier(mod, d + amount, -1);
        if(duration != -1) {
            _setCombatModifier(mod, d, duration);
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

    /**
     * Sets current health of user
     * @param h Amount to be set
     */
    public void setHealth(Integer h) {
        if(h <= 0) {
            if(this instanceof SCOPlayer) {
                Player p = ((SCOPlayer)this).getPlayer();
                p.setHealth(0);
            } else if(this instanceof ActiveMob) {
                LivingEntity e = ((ActiveMob)this).getLivingEntity();
                e.setHealth(0);
            }
        } else {
            this.currHealth = h;
        }
    }

    /**
     * Sets max health of user.
     * @param amount Amount we set max health to
     * @param duration Duration in seconds of change, -1 if no timer
     */
    public void setMaxHealth(Integer amount, int duration) {
        int copy = this.maxHealth;

        this.maxHealth = amount;
        if(this.currHealth > this.maxHealth) {
            this.currHealth = this.maxHealth;
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
        double ratio = (this.currHealth - damage) / this.maxHealth;
        double health = getAttribute(Attribute.GENERIC_MAX_HEALTH) * ratio;

        if(set) {
            this.currHealth -= (int)damage;
            getLivingEntity().setHealth(health);
        }

        return health;
    }
}
