package net.peacefulcraft.sco.swordskills.weaponskills;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.google.gson.JsonObject;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public interface WeaponModifier {

    /**
     * Gets name of this weapon modifier
     * 
     * @return name
     */
    public abstract String getName();

    /**
     * Gets modifier changed amount
     * 
     * @return double value
     */
    public abstract Double getModifierAmount();

    /**
     * Gets weapons modifier level
     * 
     * @return level
     */
    public abstract String getLevel();

    /**
     * Applies this modifiers effects onto modifier users
     * 
     * @param user User we are modifying
     */
    public abstract void applyEffects(ModifierUser user);

    /**
     * Removes this modifiers effects from modifier user
     * 
     * @param user User we are modifying
     */
    public abstract void removeEffects(ModifierUser user);

    /**
     * Determines if WeaponModifier can be reforged in inventory
     * 
     * @return boolean
     */
    public abstract Boolean canReforge();

    /**
     * Gets max level of modifier for players
     * 
     * @return
     */
    public abstract Integer getMaxPlayerLevel();

    /**
     * List of modified stats
     * 
     * @return
     */
    public abstract JsonObject getModifiedStats();

    /**
     * Check if a weapon skill exists
     * 
     * @param name Name of weapon skill
     * @return True if weapon skill exists
     */
    public static boolean modifierExists(String name) {
        try {
            Class.forName("net.peacefulcraft.sco.swordskills.weaponskills." + name);
            return true;
        } catch (ClassNotFoundException ex) {
            try {
                Class.forName("net.peacefulcraft.sco.swordskills.weaponskills." + name + "Skill");
                return true;
            } catch (ClassNotFoundException exx) {
                return false;
            }
        }
    }

    /**
     * Gets weapon skills max player level
     * 
     * @param name Name of skill
     * @return max level
     */
    public static int getWeaponMaxLevel(String name) {
        WeaponModifier wm = WeaponModifier.generateWeaponSkill(name);
        return wm.getMaxPlayerLevel();
    }

    /**
     * Generates the requested weapon modifier with base level
     * 
     * @param name Name of skill
     */
    public static WeaponModifier generateWeaponSkill(String name) {
        return WeaponModifier.generateWeaponSkill(name, "I");
    }

    /**
     * Generates the requested weapon modifier
     * 
     * @param name   Name of weapon skill
     * @param amount modifier amount
     */
    public static WeaponModifier generateWeaponSkill(String name, String level) {
        try {
            name = name.replaceAll(" ", "");

            Class<?> clas = Class.forName("net.peacefulcraft.sco.swordskills.weaponskills." + name);
            Class<?> params[] = new Class[] { String.class };
            Constructor<?> constructor = clas.getConstructor(params);

            WeaponModifier wm = ((WeaponModifier) constructor.newInstance(level));

            return wm;
        } catch (NoSuchMethodException e) {
            SwordCraftOnline.logSevere("net.peacefulcraft.sco.swordskills.weaponskills." + name
                    + " must have a constuctor with arguments (String)");
        } catch (ClassNotFoundException e) {
            SwordCraftOnline.logSevere("Attempted to generate skill " + name + ", but no corresponding class exists");
        } catch (InstantiationException | InvocationTargetException e) {
            SwordCraftOnline.logSevere("net.peacefulcraft.sco.swordskills.weaponskills." + name + "generated exception during reflective instantation.");
        } catch (IllegalAccessException e) {
            SwordCraftOnline.logSevere("net.peacefulcraft.sco.swordskills.weaponskills." + name + " is an abstract class and cannot be instatiated.");
        } catch (IllegalArgumentException e) {
            SwordCraftOnline.logSevere("net.peacefulcraft.sco.swordskills.weaponskills." + name + " recieved an invalid argument type during instation. Arguments must be of type (String).");
        } 

        throw new RuntimeException("Failed to generate WeaponModifier with requested scope " + name);
    }

    /**
     * Gets name displayed in item lore of weapons
     * 
     * @param modifier
     */
    public static String parseName(WeaponModifier modifier) {
        return modifier.getName() + " " + modifier.getLevel();
    }

    /**
     * Types of weapon modifiers when applied to weapon
     */
    public enum WeaponModifierType {
        /**Effects applied while in hotbar */
        PASSIVE, 
        /**Effects applied while in hand */
        ACTIVE;
    }

}
