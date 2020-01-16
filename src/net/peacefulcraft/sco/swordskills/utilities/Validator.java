package net.peacefulcraft.sco.swordskills.utilities;

import net.peacefulcraft.sco.item.ItemTier;

public class Validator {
	public static boolean teirExists(String str) {
		String up = str.toUpperCase();

        for (ItemTier t : ItemTier.values()) {
            if(up.equals(t.toString())) {
                return true;
            }
        }
        return false;
	}
}
