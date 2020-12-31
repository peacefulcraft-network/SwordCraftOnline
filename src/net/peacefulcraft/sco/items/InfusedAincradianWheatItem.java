package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.items.utilities.Glow;

public class InfusedAincradianWheatItem implements EphemeralAttributeHolder, ItemIdentifier {

    @Override
    public String getName() {
        return "Infused Aincradian Wheat";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(ItemTier.UNCOMMON) + "Infused Aincradian Wheat";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> desc = ItemTier.addDesc(ItemTier.UNCOMMON);
        desc.add(ItemTier.getTierColor(ItemTier.UNCOMMON) + "Aincradian Wheat that has been inbued with the");
        desc.add(ItemTier.getTierColor(ItemTier.UNCOMMON) + "power of a crafting catalyst");
        return desc;
    }

    @Override
    public Material getMaterial() {
        return Material.WHEAT;
    }

    private Integer quantity;

    @Override
    public Integer getQuantity() {
        return this.quantity;
    }

    @Override
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] { ItemTier.UNCOMMON };
    }

    @Override
    public ItemTier getTier() {
        return ItemTier.UNCOMMON;
    }

    @Override
    public boolean isDroppable() {
        return false;
    }

    @Override
    public boolean isMovable() {
        return true;
    }

    public InfusedAincradianWheatItem(ItemTier tier, Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public ItemStack applyEphemeralAttributes(ItemStack item) {
        return Glow.addGlow(item);
    }

    @Override
    public void parseEphemeralAttributes(ItemStack item) {
        // Attributes for effect based only. Nothing to do
    }
}
