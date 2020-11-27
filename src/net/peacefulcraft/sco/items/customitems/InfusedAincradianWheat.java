package net.peacefulcraft.sco.items.customitems;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.items.utilities.Glow;
import net.peacefulcraft.sco.items.utilities.Movable;

public class InfusedAincradianWheat implements ICustomItem {

    @Override
    public ItemStack create(Integer amount, Boolean shop, Boolean movable) {
        ItemStack wheat = new ItemStack(Material.WHEAT, amount);
        ItemMeta meta = wheat.getItemMeta();

        ChatColor color = ItemTier.getTierColor(ItemTier.UNCOMMON);

        ArrayList<String> desc = ItemTier.addDesc(ItemTier.UNCOMMON);
        desc.add(color + "Aincradian Wheat that has been inbued with the");
        desc.add(color + "power of a crafting catalyst");
        meta.setLore(desc);
        meta.setDisplayName(color + "Aincradian Wheat");

        wheat.setItemMeta(meta);

        ItemStack glow = Glow.addGlow(wheat);

        return Movable.setMovable(glow, movable);
    }
    
}
