package net.peacefulcraft.sco.items.swordskills;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BasicComboItem implements SwordSkillItem {

    @Override
    public ItemStack create(String tier) {
        ItemStack item = new ItemStack(Material.SNOW_BLOCK, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(SwordSkillItem.getTierColor(tier) + "Basic Combo");

        ArrayList<String> desc = SwordSkillItem.addDesc(tier);
        desc.add("Basic four hit combo.");
        meta.setLore(desc);

        item.setItemMeta(meta);
        return item;
    }
    
}
