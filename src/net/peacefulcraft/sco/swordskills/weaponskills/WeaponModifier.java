package net.peacefulcraft.sco.swordskills.weaponskills;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser.CombatModifier;

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
     * Gets modifier incoming, outgoing
     * 
     * @return boolean
     */
    public abstract Boolean getModifierIncoming();

    /**
     * Gets weapons modifier level
     * 
     * @return level
     */
    public abstract String getLevel();

    /**
     * Gets weapons modifier type
     * 
     * @return modifier type
     */
    public abstract ModifierType getModifierType();

    /**
     * Gets combat modifier type
     * 
     * @return combat modifier
     */
    public abstract CombatModifier getCombatModifierType();

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
     * Calculates modifier power value against level/
     * Similar to how sharpness is calculated on weapons
     * 
     * @param level Level of weapon modifier
     */
    public static double calculateModifierAmount(int level) {
        return 0.5 * level + 0.5;
    }

    /**
     * Gets name displayed in item lore of weapons
     * 
     * @param modifier
     */
    public static String parseName(WeaponModifier modifier) {
        return modifier.getName() + " " + modifier.getLevel();
    }

}
