package net.peacefulcraft.sco.items.utilityitems;

import java.util.ArrayList;

import org.bukkit.Material;

import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;

public class CraftingBlockedSlot implements ItemIdentifier {

    
    @Override
    public String getName() { return ""; }

    @Override
    public ArrayList<String> getLore() { return new ArrayList<String>(); }

    @Override
    public Material getMaterial() { return Material.BLACK_STAINED_GLASS_PANE; }

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

    public CraftingBlockedSlot(ItemTier tier, Integer quantity) {
        this.quantity = quantity;
    }   
}
