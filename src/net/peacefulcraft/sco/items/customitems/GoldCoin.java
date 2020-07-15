package net.peacefulcraft.sco.items.customitems;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GoldCoin implements ICustomItem {
    public ItemStack create(Integer amount) {
        ItemStack coin = new ItemStack(Material.GOLD_NUGGET, amount);
        ItemMeta meta = coin.getItemMeta();
        meta.setDisplayName("Gold Coin");

        ArrayList<String> desc = new ArrayList<String>();
        desc.add("Just a shiny coin.");
        meta.setLore(desc);

        coin.setItemMeta(meta);

        return coin;
    }

    @Override
    public ItemStack create(Integer amount, Integer price) {
        ItemStack coin = new ItemStack(Material.GOLD_NUGGET, amount);
        ItemMeta meta = coin.getItemMeta();
        meta.setDisplayName("Gold Coin - " + String.valueOf(price));

        ArrayList<String> desc = new ArrayList<String>();
        desc.add("Just a shiny coin.");
        meta.setLore(desc);

        coin.setItemMeta(meta);

        return coin;
    }
}