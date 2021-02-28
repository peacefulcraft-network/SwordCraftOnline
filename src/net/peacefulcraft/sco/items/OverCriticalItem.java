package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.OverCriticalSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class OverCriticalItem implements SwordSkillProvider {

    private double criticalModifier;
    private ItemTier tier;
    private int quantity;
    private SwordSkillType type;
    private SwordSkillDesc desc;

    public OverCriticalItem(ItemTier tier, Integer quantity) {
        this.tier = tier;
        this.quantity = quantity;
        this.type = SwordSkillType.PASSIVE;
        this.desc = new SwordSkillDesc(tier, type);
        desc.add("A hidden technique that");
        desc.add("releases your true potential.");
        switch(this.tier) {
            case RARE:
                desc.add("Critical Chance: +20%");
            break; case LEGENDARY:
                desc.add("Critical Chance: +22%");
            break; case ETHEREAL:
                desc.add("Critical Chance: +24%");
            break; case GODLIKE:
                desc.add("Critcial Chance: +30%");
            default:
        }

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
        return desc.getDesc();
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
        return type;
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
