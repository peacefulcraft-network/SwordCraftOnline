package net.peacefulcraft.sco.items.utilities;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Glow {
    
    public static ItemStack addGlow(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);

        item.addEnchantment(Enchantment.DEPTH_STRIDER, 1);
        return item;
    }
}
