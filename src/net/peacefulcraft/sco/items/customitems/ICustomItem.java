package net.peacefulcraft.sco.items.customitems;

import org.bukkit.inventory.ItemStack;

public interface ICustomItem {
    /**
     * @param amount # of items in itemstack
     */
    public ItemStack create(Integer amount);

    /**
     * @param amount # of items in itemstack
     * @param price Price of items in shop
     */
    public ItemStack create(Integer amount, Integer price);
}