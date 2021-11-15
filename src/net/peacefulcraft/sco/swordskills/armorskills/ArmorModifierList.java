package net.peacefulcraft.sco.swordskills.armorskills;

import java.util.ArrayList;
import java.util.List;

import net.peacefulcraft.sco.SwordCraftOnline;

public class ArmorModifierList {
    
    private static List<String> armorModifiers = new ArrayList<>();

    static {

    }

    /**
     * Gets random modifier name from list
     * 
     * @return Modifier name
     */
    public static String getRandomModifier() {
        return armorModifiers.get(SwordCraftOnline.r.nextInt(armorModifiers.size() - 1));
    }

    /**
     * Gets random modifier name from list
     * that is capable of reforge
     * 
     * @param reforge
     * @return name of reforge capable modifier
     */
    public static String getRandomModifier(boolean reforge) {
        if (!reforge) { return getRandomModifier(); }
        ArmorModifier am = ArmorModifier.generateArmorSkill(
            getRandomModifier()
        );
        while(!am.canReforge()) {
            am = ArmorModifier.generateArmorSkill(
                getRandomModifier()
            );
        }
        return am.getName();
    }

}
