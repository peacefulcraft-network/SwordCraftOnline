package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import org.bukkit.Material;

import net.md_5.bungee.api.ChatColor;

public class HardenedScalesItem implements ItemIdentifier {

    private int quantity;

    @Override
    public String getName() {
        return "Hardened Scales";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(ItemTier.COMMON) + "Hardened Scales";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> desc = ItemTier.addDesc(ItemTier.COMMON);
        ChatColor color = ItemTier.getTierColor(ItemTier.COMMON);
        desc.add(color + "The outer skin of certain mobs in Aincrad.");
        return desc;
    }

    @Override
    public Material getMaterial() {
        return Material.PHANTOM_MEMBRANE;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] { ItemTier.COMMON };
    }

    @Override
    public ItemTier getTier() {
        return ItemTier.COMMON;
    }

    @Override
    public Integer getQuantity() {
        return this.quantity;
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

    public HardenedScalesItem(ItemTier tier, Integer quantity) {
        this.quantity = quantity;
    }
    
}
