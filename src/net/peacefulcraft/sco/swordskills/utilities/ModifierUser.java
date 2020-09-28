package net.peacefulcraft.sco.swordskills.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;

/**
 * Holds necessary information for SCOPlayers and AciveMobs
 * Attribute changing methods and damage modifier methods
 */
public class ModifierUser {

    private List<Modifier> damageModifiers;

    private LivingEntity entity;
    
    public LivingEntity getLivingEntity() { return this.entity; }

    public List<Modifier> getDamageModifiers() { return Collections.unmodifiableList(this.damageModifiers); }

    /**
     * Temporarily set value of multiplier to value.
     * Also used to permanently set value via duration value.
     * @param type ModifierType to be searched for
     * @param multiplier Value to be set
     * @param duration Duration of set, if -1 it is permanent.
     */
    public void setMultiplier(ModifierType type, double multiplier, int duration) {
        Double initial = null;
        for(Modifier m : this.damageModifiers) {
			if(m.getType().equals(type)) {
                // Copying and setting multiplier
                initial = m.getMultiplier();
				m.setMultiplier(multiplier);
				break;
			}
        }
        
        // If not perm change and we found the modifier
        if(duration != -1 && initial != null) {
            double copy = initial;
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), new Runnable() {
                public void run() {
                    setMultiplier(type, copy, -1);
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
		removeDamageModifier(m);
		this.damageModifiers.add(m);
	}

	/**
	 * Safely removes modifier of same type from list
	 * @param m Modifier to be removed
	 */
	public void removeDamageModifier(Modifier m) {
		removeDamageModifier(m.getType());
	}

	/**
	 * Safely removes modifier of same type from list
	 * @param type Modifier to be removed
	 */
	public void removeDamageModifier(ModifierType type) {
		// If modifier of same type exists we remove
		Iterator<Modifier> iter = this.damageModifiers.iterator();
		while(iter.hasNext()) {
			Modifier mod = iter.next();
			if(mod.getType().equals(type)) {
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
	public Modifier getDamageModifier(ModifierType type) {
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
	public Modifier getDamageModifier(Modifier m) {
		return getDamageModifier(m.getType());
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
    public void disableModifier(ModifierType type, int duration) {
        // Copying modifier we want and removing it
        Modifier m = getDamageModifier(type);
        removeDamageModifier(type);

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
}
