package net.peacefulcraft.sco.items;

import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.items.swordskills.CriticalStrikeItem;
import net.peacefulcraft.sco.items.utilities.TeleportCrystal;

public class Item {
    public enum Items {
        TELEPORT_CRYSTAL,

        CRITICAL_STRIKE;
    }

    public enum Tiers {
        COMMON, UNCOMMON, RARE, LEGENDARY, MASTERY, ETHEREAL;
    }

    public static boolean itemExists(String str) {
        String up = str.toUpperCase();

        System.out.println("[TEST ERROR] Item name: " + up);
        for(Items i : Items.values()) {
            if(up.equals(i.toString())) {
                return true;
            }
        }
        System.out.println("[TEST ERROR] Result: FALSE");
        return false;
    }

    /**
     * Creates item
     * @param str name of the item to be created
     * @param tier Tier of sword skill item to be created
     */
    public static ItemStack giveItem(String str, String tier) {
        
        if(tier == null) { tier = "COMMON"; }

        String up = str.toUpperCase();
        String tierUp = tier.toUpperCase();

        switch(Items.valueOf(up)) {
        case TELEPORT_CRYSTAL:
            return (new TeleportCrystal().create());
        case CRITICAL_STRIKE:
            return (new CriticalStrikeItem().create(tierUp));
        default:
            return null;
        }
    }

    public static boolean tierExists(String str) {
        String up = str.toUpperCase();

        for (Tiers t : Tiers.values()) {
            if(up.equals(t.toString())) {
                return true;
            }
        }
        return false;
    }
}