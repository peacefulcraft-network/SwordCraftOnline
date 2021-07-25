package net.peacefulcraft.sco.swordskills;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;

/**
 * Used to change item lore in itemidentifier generation
 */
public class SwordSkillInfoProvider {
    
    /**
     * Parses data into item lore
     * @param item
     * @param data
     * @return modified item
     */
    public static ItemStack getInfoProvider(ItemStack item, ItemIdentifier ident) {
        
        ArrayList<String> lore = ident.getLore();
        ArrayList<String> loree = new ArrayList<>();
        for (String ss : lore) {
            loree.add(ChatColor.GRAY + ss);
        }

        loree.add("");
        loree.add(ChatColor.GRAY + "Possible tiers: ");
        String s = "";
        for (ItemTier tier : ident.getAllowedTiers()) {
            s += ItemTier.getTierColor(tier) + tier.toString() + ChatColor.GRAY + ", ";
        }
        s = StringUtils.substring(s, 0, s.length() - 2) + ChatColor.GRAY + ".";
        loree.add(s);

        ItemMeta meta = item.getItemMeta();
        meta.setLore(loree);
        item.setItemMeta(meta);

        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("movable", false);
        nbti.setBoolean("isInfoProvider", true);
        item = nbti.getItem();

        return item;
    }

}
