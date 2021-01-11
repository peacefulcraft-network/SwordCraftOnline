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
        weaponModifiers.add("Light Material");
        weaponModifiers.add("Enhanced Guard");
    }

    /**
     * Gets random modifier name from list
     * 
     * @return Name of modifier
     */
    public static String getRandomModifier() {
        return weaponModifiers.get(SwordCraftOnline.r.nextInt(weaponModifiers.size() - 1));
    }

    /**
     * Selects random modifier name that is capable of reforging
     * 
     * @param reforge
     * @return
     */
    public static String getRandomModifier(boolean reforge) {
        if(!reforge) { return getRandomModifier(); }
        WeaponModifier wm = WeaponModifier.generateWeaponSkill(
            getRandomModifier()
        );
        while(!wm.canReforge()) {
            wm = WeaponModifier.generateWeaponSkill(
                getRandomModifier()
            );
        }
        return wm.getName();
    }

}
