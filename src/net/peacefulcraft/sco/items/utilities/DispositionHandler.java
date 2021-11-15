package net.peacefulcraft.sco.items.utilities;

import net.peacefulcraft.sco.SwordCraftOnline;

/**
 * Class to handle rolling disposition on armor and weapons
 */
public class DispositionHandler {
    
    /**
     * Rolls disposition chance of reforge
     * 
     * @param disposition Disposition of item
     * @return boolean if can be rolled
     */
    public static boolean rollReforgeChance(int disposition) {
        return SwordCraftOnline.r.nextInt(9) + 1 <= disposition * 2;
    }

    /**
     * Rolls disposition for reforge modifier level
     * 
     * @param disposition Disposition of item
     * @return Int 
     */
    public static int rollReforgeLevel(int disposition) {
        int roll = (1 + disposition * 750) / (SwordCraftOnline.r.nextInt(99) + 1);
        roll = roll > 100 ? 100 : roll;
        return roll;
    }

}
