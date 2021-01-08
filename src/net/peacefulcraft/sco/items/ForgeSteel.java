package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import org.bukkit.Material;

public class ForgeSteel implements ItemIdentifier {

    private Integer quantity;

    @Override
    public String getName() {
        return "Forge Steel";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(ItemTier.COMMON) + "Forge Steel";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> desc = ItemTier.addDesc(ItemTier.COMMON);
        desc.add(ItemTier.getTierColor(ItemTier.COMMON) + "Steel made by the ones above.");
        desc.add(ItemTier.getTierColor(ItemTier.COMMON) + "Occasionally falls to Aincrad for");
        desc.add(ItemTier.getTierColor(ItemTier.COMMON) + "use in the forge.");
        return desc;
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_INGOT;
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

    public ForgeSteel(ItemTier tier, Integer quanity) {
        this.quantity = quanity;
    }
    
}
