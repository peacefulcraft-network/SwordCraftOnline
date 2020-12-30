package net.peacefulcraft.sco.items.utilityitems;

import java.util.ArrayList;

import org.bukkit.Material;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;

public class InfusionStartSlotItem implements ItemIdentifier {

    private Integer quantity;

    @Override
    public String getName() {
        return "InfusionStartSlot";
    }

    @Override
    public String getDisplayName() {
        return ChatColor.DARK_PURPLE + "Click to begin infusion";
    }

    @Override
    public ArrayList<String> getLore() {
        return new ArrayList<String>();
    }

    @Override
    public Material getMaterial() {
        return Material.PURPLE_STAINED_GLASS_PANE;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[]{ ItemTier.COMMON };
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
        return false;
    }

    @Override
    public boolean isMovable() {
        return false;
    }

    public InfusionStartSlotItem(ItemTier tier, Integer quantity) {
        this.quantity = quantity;
    }

}
