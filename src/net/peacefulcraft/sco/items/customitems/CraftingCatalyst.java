package net.peacefulcraft.sco.items.customitems;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.items.utilities.Glow;
import net.peacefulcraft.sco.items.utilities.Movable;

public class CraftingCatalyst implements ICustomItem {

    @Override
    public ItemStack create(Integer amount, Boolean shop, Boolean movable) {
        
        ItemStack catalyst = new ItemStack(Material.NETHER_STAR, amount);
        ItemMeta meta = catalyst.getItemMeta();
        //TODO: Add shop pricing

        ChatColor color = ItemTier.getTierColor(ItemTier.UNCOMMON);

        ArrayList<String> desc = ItemTier.addDesc(ItemTier.UNCOMMON);
        desc.add(color + "Contains immense energy used to combine crafts.");
        meta.setLore(desc);
        meta.setDisplayName(color + "Crafting Catalyst");

        catalyst.setItemMeta(meta);
        ItemStack glow = Glow.addGlow(catalyst);

        return Movable.setMovable(glow, movable);
    }
    
}
