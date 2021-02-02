package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.OverCriticalSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class OverCriticalItem implements SwordSkillProvider {

    private double criticalModifier;
    private ItemTier tier;
    private int quantity;

    public OverCriticalItem(ItemTier tier, int quantity) {
        this.tier = tier;
        this.quantity = quantity;

        setModifiers();
    }

    @Override
    public String getName() {
        return "Over Critical";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(this.tier) + "Over Critical";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(this.tier) + "A hidden technique that");
        lore.add(ItemTier.getTierColor(this.tier) + "releases your true potential.");
        switch(this.tier) {
            case RARE:
                lore.add(ItemTier.getTierColor(this.tier) + "Critical Chance: +20%");
            break; case LEGENDARY:
                lore.add(ItemTier.getTierColor(this.tier) + "Critical Chance: +22%");
            break; case ETHEREAL:
                lore.add(ItemTier.getTierColor(this.tier) + "Critical Chance: +24%");
            break; case GODLIKE:
                lore.add(ItemTier.getTierColor(this.tier) + "Critcial Chance: +30%");
            default:
        }
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.OBSIDIAN;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] {
            ItemTier.RARE,
            ItemTier.LEGENDARY,
            ItemTier.ETHEREAL,
            ItemTier.GODLIKE
        };
    }

    @Override
    public ItemTier getTier() {
        return this.tier;
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

    @Override
    public JsonObject getCustomData() {
        return new JsonObject();
    }

    @Override
    public void setCustomData(JsonObject data) {

    }

    @Override
    public void parseCustomItemData(ItemStack item) {

    }

    @Override
    public ItemStack applyCustomItemData(ItemStack item, JsonObject data) {
        return item;
    }

    @Override
    public SwordSkillType getType() {
        return SwordSkillType.PASSIVE;
    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        return new OverCriticalSkill(caster, this.criticalModifier, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {
        switch(this.tier) {
            case RARE:
                this.criticalModifier = 0.02;
            break; case LEGENDARY:
                this.criticalModifier = 0.04;
            break; case ETHEREAL:
                this.criticalModifier = 0.06;
            break; case GODLIKE:
                this.criticalModifier = 0.1;
            default:
        }
    }
    
}
