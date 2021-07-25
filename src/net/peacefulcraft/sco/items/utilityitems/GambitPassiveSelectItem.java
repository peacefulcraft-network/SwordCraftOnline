package net.peacefulcraft.sco.items.utilityitems;

import java.util.ArrayList;

import org.bukkit.Material;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;

public class GambitPassiveSelectItem implements ItemIdentifier {

    private Integer quantity;

    @Override
    public String getName() {
        return "GambitPassiveSelect";
    }

    @Override
    public String getDisplayName() {
        return ChatColor.BLUE + "Click to open Passive Skils!";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Passive stat boosts.");
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.SPRUCE_SAPLING;
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
        return quantity;
    }

    @Override
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean isDroppable() {
        return false;
    }

    @Override
    public boolean isMovable() {
        return false;
    }

    public GambitPassiveSelectItem(ItemTier tier, Integer quantity) {
        this.quantity = quantity;
    }
    
}
