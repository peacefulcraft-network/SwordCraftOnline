package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class KillerSenseItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;

    public KillerSenseItem(ItemTier tier, int quantity) {
        this.tier = tier;
        this.quantity = quantity;

        setModifiers();
    }

    @Override
    public String getName() {
        return "Killer Sense";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "Killer Sense";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(tier) + "Walk the earth with");
        lore.add(ItemTier.getTierColor(tier) + "a presense that weakens");
        lore.add(ItemTier.getTierColor(tier) + "those around you.");
        lore.add(ItemTier.getTierColor(tier) + "On trigger: nearby entities");
        switch(tier) {
            case COMMON:
                lore.add(ItemTier.getTierColor(tier) + "Armor: -1 for 7 seconds");
                lore.add(ItemTier.getTierColor(tier) + "Parry Chance: -0.05 for 7 seconds");
            break; case UNCOMMON:
                lore.add(ItemTier.getTierColor(tier) + "Armor: -2 for 7 seconds");
                lore.add(ItemTier.getTierColor(tier) + "Parry Chance: -0.05 for 7 seconds");
            break; case RARE:
                lore.add(ItemTier.getTierColor(tier) + "Armor: -3 for 7 seconds");
                lore.add(ItemTier.getTierColor(tier) + "Parry Chance: -0.05 for 7 seconds");
            break; case LEGENDARY:
                lore.add(ItemTier.getTierColor(tier) + "Armor: -4 for 7 seconds");
                lore.add(ItemTier.getTierColor(tier) + "Parry Chance: -0.05 for 7 seconds");
            
            }
        return lore;
    }

    @Override
    public Material getMaterial() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ItemTier getTier() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer getQuantity() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setQuantity(Integer quantity) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isDroppable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isMovable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public JsonObject getCustomData() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setCustomData(JsonObject data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void parseCustomItemData(ItemStack item) {
        // TODO Auto-generated method stub

    }

    @Override
    public ItemStack applyCustomItemData(ItemStack item, JsonObject data) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SwordSkillType getType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setModifiers() {
        // TODO Auto-generated method stub

    }
    
}
