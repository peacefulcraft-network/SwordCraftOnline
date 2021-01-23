package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.RootedSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class RootedItem implements SwordSkillProvider {

    private int quantity;
    private ItemTier tier;
    private int regenModifier;

    public RootedItem(ItemTier tier, Integer level) {
        this(tier, level, 1);
    }

    public RootedItem(ItemTier tier, Integer level, int quantity) {
        this.tier = tier;
        this.quantity = quantity;

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
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ItemTier.getTierColor(this.tier) + "Absorb the life energy from the soil around you.");
        switch(this.tier) {
            case LEGENDARY:
                lore.add(ItemTier.getTierColor(this.tier) + "Grants regeneration I while on grass.");
            break; case ETHEREAL:
                lore.add(ItemTier.getTierColor(this.tier) + "Grants regeneration II while on grass.");
            break; case GODLIKE:
                lore.add(ItemTier.getTierColor(this.tier) + "Grants regeneration III while on grass.");
            default:
                lore.add("Severe Error: Skill not functional, Contact Admin for correction.");
        }
        return lore;
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
        return new RootedSkill(caster, this.regenModifier, (SwordSkillProvider)this);
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
