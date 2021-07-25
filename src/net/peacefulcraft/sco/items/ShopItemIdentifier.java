package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import net.md_5.bungee.api.ChatColor;

/**
 * Adds additional information for Gambit shop
 */
public class ShopItemIdentifier {

    /**
     * Fetches relevant pricing in gambit shop by tier
     * @param tier
     * @return
     */
    public static int getPrice(ItemTier tier) {
        switch (tier) {
            case COMMON:
                return 200;
            case ETHEREAL:
                return 1300;
            case GODLIKE:
                return 3000;
            case LEGENDARY:
                return 950;
            case RARE:
                return 600;
            case UNCOMMON:
                return 350;
            default:
                return 200;           
        }
    }

    /**
     * Converts normal item identifier into
     * shop item
     * @param item
     * @param price
     * @return
     */
    public static ItemStack generateShopItem(String name, ItemTier tier, int amount, int price) {
        ItemStack item = ItemIdentifier.generateItem(name, tier, amount);
        ItemMeta meta = item.getItemMeta();

        // Re-coloring description
        ArrayList<String> lore = new ArrayList<>();
        for (String s : meta.getLore()) {
            lore.add(ChatColor.GRAY + s);
        }
        lore.add("");
        lore.add(ChatColor.BLUE + "Price: " + ChatColor.GOLD + "" + price);
        meta.setLore(lore);
        item.setItemMeta(meta);

        NBTItem nbti = new NBTItem(item);
        nbti.setBoolean("movable", false);
        nbti.setBoolean("isShop", true);
        nbti.setInteger("price", price);
        item = nbti.getItem();

        return item;
    }

}
