package net.peacefulcraft.sco.items.utilityitems;

import java.util.ArrayList;

import org.bukkit.Material;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;

public class PrimarySkillCooldownItem implements ItemIdentifier {

    private int quantity;

    @Override
    public String getName() {
        return "Primary Skill Cooldown";
    }

    @Override
    public String getDisplayName() {
        return ChatColor.RED + "Primary skill in cooldown";
    }

    @Override
    public ArrayList<String> getLore() {
        return new ArrayList<String>();
    }

    @Override
    public Material getMaterial() {
        return Material.RED_STAINED_GLASS_PANE;
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
        return false;
    }

    @Override
    public boolean isMovable() {
        return true;
    }

    public PrimarySkillCooldownItem(ItemTier tier, Integer quantity) {
        this.quantity = quantity;
    }
    
}
