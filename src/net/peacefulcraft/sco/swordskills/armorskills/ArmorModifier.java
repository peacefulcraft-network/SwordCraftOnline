package net.peacefulcraft.sco.swordskills.armorskills;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.google.gson.JsonObject;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

/**
 * Armor modifier interface for all
 * skills on armor
 */
public interface ArmorModifier {
    
    /**
     * Gets name of this armor modifier
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
     * Gets modifier level as roman numeral
     * 
     * @return level
     */
    public abstract String getLevel();

    /**
     * Applies this armor modifiers effects onto modifier user
     * 
     * @param user User we modify
     */
    public abstract void applyEffects(ModifierUser user);

    /**
     * Removes this armor modifiers effects from modifier user
     *  
     * @param user User we modify
     */
    public abstract void removeEffects(ModifierUser user);

    /**
     * Determines if armor modifier can be reforged
     * 
     * @return boolean
     */
    public abstract Boolean canReforge();

    /**
     * Gets max level of modifier for player armor
     * 
     * @return int level
     */
    public abstract Integer getMaxPlayerLevel();

    /**
     * List of modified stats as json object
     * 
     * @return Json object of stats
     */
    public abstract JsonObject getModifiedStats();

    /**
     * Checks if a given armor skill exists
     * 
     * @param name Name of armor skill
     * @return True if armor skill exists
     */
    public static Boolean modifierExists(String name) {
        try {
            Class.forName("net.peacefulcraft.sco.swordskills.armorskills." + name);
            return true;
        } catch (ClassNotFoundException ex) {
            try {
                Class.forName("net.peacefulcraft.sco.swordskills.armorskills." + name + "Skill");
                return true;
            } catch (ClassNotFoundException exx) {
                return false;
            }
        }
    }

    /**
     * Gets armor skills max player level
     * 
     * @param name Name of skill
     * @return Max level of instance
     */
    public static int getArmorMaxLevel(String name) {
        ArmorModifier am = ArmorModifier.generateArmorSkill(name);
        return am.getMaxPlayerLevel();
    }

    /**
     * Generates an armor modifier instance from name
     * 
     * @param name Name of armor modifier
     * @return Armor modifier instance
     */
    public static ArmorModifier generateArmorSkill(String name) {
        return ArmorModifier.generateArmorSkill(name, "I");
    }

    /**
     * Generates an armor modifier instance from name and level
     * 
     * @param name Name of armor modifier
     * @param level Level of armor modifier as roman numeral
     * @return Armor modifier instance
     */
    public static ArmorModifier generateArmorSkill(String name, String level) {
        try {
            name = name.replaceAll(" ", "");

            Class<?> clas = Class.forName("net.peacefulcraft.sco.swordskills.armorskills." + name);
            Class<?> params[] = new Class[] { String.class };
            Constructor<?> constructor = clas.getConstructor(params);

            ArmorModifier am = ((ArmorModifier) constructor.newInstance(level));

            return am;
        } catch (NoSuchMethodException e) {
            SwordCraftOnline.logSevere("net.peacefulcraft.sco.swordskills.armorskills." + name
                    + " must have a constuctor with arguments (String)");
        } catch (ClassNotFoundException e) {
            SwordCraftOnline.logSevere("Attempted to generate skill " + name + ", but no corresponding class exists");
        } catch (InstantiationException | InvocationTargetException e) {
            SwordCraftOnline.logSevere("net.peacefulcraft.sco.swordskills.armorskills." + name + "generated exception during reflective instantation.");
        } catch (IllegalAccessException e) {
            SwordCraftOnline.logSevere("net.peacefulcraft.sco.swordskills.armorskills." + name + " is an abstract class and cannot be instatiated.");
        } catch (IllegalArgumentException e) {
            SwordCraftOnline.logSevere("net.peacefulcraft.sco.swordskills.armorskills." + name + " recieved an invalid argument type during instation. Arguments must be of type (String).");
        } 

        throw new RuntimeException("Failed to generate ArmorModifier with requested scope " + name);
    }

    /**
     * Gets name displayed in item lore of armor
     * 
     * @param modifier Armor modifier instance
     * @return Formatted name
     */
    public static String parseName(ArmorModifier modifier) {
        return modifier.getName() + " " + modifier.getLevel();
    }
}
