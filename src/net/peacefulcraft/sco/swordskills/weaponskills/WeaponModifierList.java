package net.peacefulcraft.sco.swordskills.weaponskills;

import java.util.ArrayList;
import java.util.List;

import net.peacefulcraft.sco.SwordCraftOnline;

/**
 * Supporter class for getting weapon modifiers by name.
 */
public class WeaponModifierList {
    
    private static List<String> weaponModifiers = new ArrayList<>();

    static {
        weaponModifiers.add("Refined Power");
        weaponModifiers.add("Refined Technique");
    }

    /**
     * Gets random modifier name from list
     * 
     * @return Name of modifier
     */
    public static String getRandomModifier() {
        return weaponModifiers.get(SwordCraftOnline.r.nextInt(weaponModifiers.size() - 1));
    }

}
