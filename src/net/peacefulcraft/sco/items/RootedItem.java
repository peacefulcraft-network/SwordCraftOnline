package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.RootedSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class RootedItem implements SwordSkillProvider {

    private int quantity;
    private ItemTier tier;
    private SwordSkillType type;
    private SwordSkillDesc desc;
    private int regenModifier;

    public RootedItem(ItemTier tier, Integer quantity) {
        this.tier = tier;
        this.quantity = quantity;
        this.type = SwordSkillType.PASSIVE;
        this.desc = new SwordSkillDesc(tier, type);
        desc.add( "Absorb the life energy from the soil around you.");
        switch(this.tier) {
            case LEGENDARY:
                desc.add("Grants regeneration I while on grass.");
            break; case ETHEREAL:
                desc.add("Grants regeneration II while on grass.");
            break; case GODLIKE:
                desc.add("Grants regeneration III while on grass.");
            default:
        }

        setModifiers();
    }

    @Override
    public String getName() {
        return "Rooted";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(this.tier) + "Rooted";
    }

    @Override
    public ArrayList<String> getLore() {
        return desc.getDesc();
    }

    @Override
    public Material getMaterial() {
        return Material.SPRUCE_SAPLING;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] {
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
        return true;
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
        return new RootedSkill(caster, this.regenModifier, (SwordSkillProvider)this, tier);
    }

    @Override
    public void setModifiers() {
        switch(this.tier) {
            case LEGENDARY:
                this.regenModifier = 1;
            break; case ETHEREAL:
                this.regenModifier = 2;
            break; case GODLIKE:
                this.regenModifier = 3;
            default:
                this.regenModifier = 0;
        }
    }
    
}
