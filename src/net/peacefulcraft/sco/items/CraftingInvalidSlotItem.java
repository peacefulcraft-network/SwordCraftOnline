package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import org.bukkit.Material;

public class CraftingInvalidSlotItem implements ItemIdentifier {

    @Override
    public String getName() { return ""; }

    @Override
    public ArrayList<String> getLore() { return new ArrayList<String>(); }

    @Override
    public Material getMaterial() { return Material.RED_STAINED_GLASS_PANE; }

    private Integer quantity;
        @Override
        public Integer getQuantity() { return this.quantity; }

        @Override
        public void setQuantity(Integer quantity) { this.quantity = quantity; }


    @Override
    public ItemTier[] getAllowedTiers() { return new ItemTier[]{ ItemTier.COMMON }; }

    @Override
    public ItemTier getTier() { return ItemTier.COMMON; }

    @Override
    public boolean isDroppable() { return false; }

    @Override
    public boolean isMovable() { return false; }

    public CraftingInvalidSlotItem(ItemTier tier, Integer quantity) {
        this.quantity = quantity;
    }
}