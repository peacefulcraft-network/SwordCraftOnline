package net.peacefulcraft.sco.items.utilityitems;

import java.util.ArrayList;

import org.bukkit.Material;

import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;

public class BlueSlotItem implements ItemIdentifier {

    private Integer quantity;

    @Override
    public String getName() {
        return "BlueSlot";
    }

    @Override
    public String getDisplayName() {
        return " ";
    }

    @Override
    public ArrayList<String> getLore() {
        return new ArrayList<String>();
    }

    @Override
    public Material getMaterial() {
        return Material.BLUE_STAINED_GLASS_PANE;
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

    public BlueSlotItem(ItemTier tier, Integer quantity) {
        this.quantity = quantity;
    }
    
}
