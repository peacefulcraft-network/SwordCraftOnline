package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import org.bukkit.Material;

import net.md_5.bungee.api.ChatColor;

public class QuestBookItem implements ItemIdentifier {

    private int quantity;
    private ItemTier tier;

    public QuestBookItem(ItemTier tier, Integer quantity) {
        this.tier = ItemTier.COMMON;
        this.quantity = quantity;
    }

    @Override
    public String getName() {
        return "Quest Book";
    }

    @Override
    public String getDisplayName() {
        return ChatColor.GOLD + "Quest Book";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "Right Click to open");
        lore.add(ChatColor.BLUE + "Quest Book Inventory.");
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.BOOK;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] { ItemTier.COMMON };
    }

    @Override
    public ItemTier getTier() {
        return tier;
    }

    @Override
    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;        
    }

    @Override
    public boolean isDroppable() {
        return true;
    }

    @Override
    public boolean isMovable() {
        return true;
    }
    
}
