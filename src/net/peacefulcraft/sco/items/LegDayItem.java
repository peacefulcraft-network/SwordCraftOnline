package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.LegDaySkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class LegDayItem implements SwordSkillProvider {

    private int quantity;
    private ItemTier tier;
    private SwordSkillType type;
    private SwordSkillDesc desc;
    private double movementModifier;

    public LegDayItem(ItemTier tier, Integer quantity) {
        this.tier = tier;
        this.quantity = quantity;
        this.type = SwordSkillType.PASSIVE;
        this.desc = new SwordSkillDesc(tier, type);
        desc.add("Train your legs to increase");
        desc.add("your movement speed.");

        setModifiers();

        desc.add("Movement Speed: +" + ( 20 + (100 * movementModifier) + "%") );
    }

    @Override
    public String getName() {
        return "Leg Day";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(this.tier) + "Leg Day";
    }

    @Override
    public ArrayList<String> getLore() {
        return desc.getDesc();
    }

    @Override
    public Material getMaterial() {
        return Material.SUGAR;
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
        return new LegDaySkill(caster, this.movementModifier, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {
        switch(this.tier) {
            case COMMON:
                this.movementModifier = 0.0;
            break; case UNCOMMON:
                this.movementModifier = 0.02;
            break; case RARE:
                this.movementModifier = 0.04;
            break; case LEGENDARY:
                this.movementModifier = 0.06;
            break; case ETHEREAL:
                this.movementModifier = 0.08;
            break; case GODLIKE:
                this.movementModifier = 0.1;
        }
    }
    
}
