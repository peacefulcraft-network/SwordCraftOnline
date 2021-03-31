package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.ResolutionWindSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class ResolutionWindItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;
    private SwordSkillType type;
    private SwordSkillDesc desc;
    private double vectorModifier;

    public ResolutionWindItem(ItemTier tier, Integer quantity) {
        this.tier = tier;
        this.quantity = quantity;
        this.type = SwordSkillType.SECONDARY;
        this.desc = new SwordSkillDesc(tier, type);
        desc.add("Summon a gust of wind");
        desc.add("to force back your foes.");
        switch(tier) {
            case RARE:
                desc.add("Wind Modifier: 2.0");
            break; case LEGENDARY:
                desc.add("Wind Modifier: 2.1");
            break; case ETHEREAL:
                desc.add("Wind Modifier: 2.2");
            break; case GODLIKE:
                desc.add("Wind Modifier: 2.3");
            default:
        }

        setModifiers();
    }

    @Override
    public String getName() {
        return "Resolution Wind";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "Resolution Wind";
    }

    @Override
    public ArrayList<String> getLore() {
        return desc.getDesc();
    }

    @Override
    public Material getMaterial() {
        return Material.COBWEB;
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
        return new ResolutionWindSkill(caster, vectorModifier, (SwordSkillProvider)this, tier);
    }

    @Override
    public void setModifiers() {
        switch(tier) {
            case RARE:
                this.vectorModifier = 0.0;
            break; case LEGENDARY:
                this.vectorModifier = 0.1;
            break; case ETHEREAL:
                this.vectorModifier = 0.2;
            break; case GODLIKE:
                this.vectorModifier = 0.3;
            default:
        }
    }
    
}
