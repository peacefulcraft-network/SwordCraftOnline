package net.peacefulcraft.sco.items.utilities;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Constant methods for handling item durability
 */
public class Durability {
    
    /**
     * Toggles item unbreakable
     * @param item Item stack we are modifying
     * @param unbreakable If true sets unbreakable
     * @return Modified item
     */
    public static ItemStack setUnbreakable(ItemStack item, boolean unbreakable) {
        ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(unbreakable);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Sets item durability if possible
     * If durability change is not possible we return same itemstack
     * @param item Item we are setting durability to
     * @param durability Desired durability
     * @return Modified itemstack or same itemstack if durability was not changed
     */
    public static ItemStack setDurability(ItemStack item, int durability) {
        ItemMeta meta = item.getItemMeta();
        if(meta instanceof Damageable) {
            Damageable dam = (Damageable)meta;
            dam.setDamage(durability);
            item.setItemMeta(meta);
        }

        return item;
    }

    /**
     * Sets weapon to unbreakable and sets durability
     * @param item Itemstack we are modifying
     * @param durability Desired durability
     * @return Modified weapon with durability
     */
    public static ItemStack setWeapon(ItemStack item, int durability) {
        item = setUnbreakable(item, true);
        item = setDurability(item, durability);
        return item;
    }

}
