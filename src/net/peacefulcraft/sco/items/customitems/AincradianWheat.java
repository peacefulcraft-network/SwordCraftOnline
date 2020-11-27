package net.peacefulcraft.sco.items.customitems;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.items.utilities.Movable;

public class AincradianWheat implements ICustomItem {

    @Override
    public ItemStack create(Integer amount, Boolean shop, Boolean movable) {
        
        ItemStack wheat = new ItemStack(Material.WHEAT, amount);
        ItemMeta meta = wheat.getItemMeta();

        ChatColor color = ItemTier.getTierColor(ItemTier.COMMON);

        ArrayList<String> desc = ItemTier.addDesc(ItemTier.COMMON);
        desc.add(color + "Natural wheat found across Aincrad");
        meta.setLore(desc);
        meta.setDisplayName(color + "Aincradian Wheat");

        wheat.setItemMeta(meta);

        return Movable.setMovable(wheat, movable);
    }
    
}
