package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.KillerSenseSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class KillerSenseItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;
    private SwordSkillType type;
    private SwordSkillDesc desc;
    private int levelModifier;

    public KillerSenseItem(ItemTier tier, Integer quantity) {
        this.tier = tier;
        this.quantity = quantity;
        this.type = SwordSkillType.SECONDARY;
        this.desc = new SwordSkillDesc(tier, type);
        desc.add("Walk the earth with");
        desc.add("a presense that weakens");
        desc.add("those around you.");
        desc.add("On trigger: nearby entities");
        switch(tier) {
            case COMMON:
                desc.add("Armor: -1 for 7 seconds");
                desc.add("Parry Chance: -0.05 for 7 seconds");
            break; case UNCOMMON:
                desc.add("Armor: -2 for 7 seconds");
                desc.add("Parry Chance: -0.05 for 7 seconds");
            break; case RARE:
                desc.add("Armor: -3 for 7 seconds");
                desc.add("Parry Chance: -0.05 for 7 seconds");
            break; case LEGENDARY:
                desc.add("Armor: -4 for 7 seconds");
                desc.add("Parry Chance: -0.05 for 7 seconds");
            break; case ETHEREAL:
                desc.add("Armor: -5 for 7 seconds");
                desc.add("Parry Chance: -0.05 for 7 seconds");
            break; case GODLIKE:
                desc.add("Armor: -6 for 7 seconds");
                desc.add("Parry Chance: -0.05 for 7 seconds");
        }

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
        return desc.getDesc();
    }

    @Override
    public Material getMaterial() {
        return Material.RED_DYE;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] {
            ItemTier.COMMON,
            ItemTier.UNCOMMON,
            ItemTier.RARE,
            ItemTier.LEGENDARY,
            ItemTier.ETHEREAL,
            ItemTier.GODLIKE
        };
    }

    @Override
    public ItemTier getTier() {
        return tier;
    }

    @Override
    public Integer getQuantity() {
        return quantity;
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
        return SwordSkillType.SECONDARY;
    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        return new KillerSenseSkill(caster, this.levelModifier, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {
        switch(tier) {
            case COMMON:
                this.levelModifier = 0;
            break; case UNCOMMON:
                this.levelModifier = 1;
            break; case RARE:
                this.levelModifier = 2;
            break; case LEGENDARY:
                this.levelModifier = 3;
            break; case ETHEREAL:
                this.levelModifier = 4;
            break; case GODLIKE:
                this.levelModifier = 5;
        }

    }
    
}
