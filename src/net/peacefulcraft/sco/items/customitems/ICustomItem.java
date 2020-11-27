package net.peacefulcraft.sco.items.customitems;

import org.bukkit.inventory.ItemStack;

public interface ICustomItem {
    /**
     * @param amount # of items in itemstack
     * @param shop if item is in shop inventory. Changes items name to store shop price.
     * @param movable If true, item is movable in inventory. If false, it is immovable
     */
    public ItemStack create(Integer amount, Boolean shop, Boolean movable);
}