package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.items.utilities.Glow;

public class HardenedForgeSteelItem implements EphemeralAttributeHolder, ItemIdentifier {

    private int quantity;

    @Override
    public String getName() {
        return "Hardened Forge Steel";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(ItemTier.UNCOMMON) + "Hardened Forge Steel";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> desc = ItemTier.addDesc(ItemTier.UNCOMMON);
        desc.add(ItemTier.getTierColor(ItemTier.UNCOMMON) + "Forge Steel hardened by");
        desc.add(ItemTier.getTierColor(ItemTier.UNCOMMON) + "earthly mob materials.");
        return desc;
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_INGOT;
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

    @Override
    public ItemStack applyEphemeralAttributes(ItemStack item) {
        return Glow.addGlow(item);
    }

    @Override
    public void parseEphemeralAttributes(ItemStack item) {

    }

    public HardenedForgeSteelItem(ItemTier tier, Integer quantity) {
        this.quantity = quantity;
    }
    
}
