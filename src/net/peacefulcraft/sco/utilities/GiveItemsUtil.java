package net.peacefulcraft.sco.utilities;

import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Static class. Safely gives items to player via filling inventory then
 * dropping on ground
 */
public class GiveItemsUtil {
    
    /**
     * Safely gives items to player then drops excess on ground around player
     * @param p Player to be given items
     * @param lis List of itemstacks to be given to player
     */
    public static void giveItems(Player p, List<ItemStack> lis) {
        for(ItemStack i : lis) {
            HashMap<Integer, ItemStack> ret = p.getInventory().addItem(i);
            if(ret != null) {
                for(ItemStack i2 : ret.values()) {
                    p.getLocation().getWorld().dropItemNaturally(p.getLocation(), i2);
                }
            }
        }
    }

}