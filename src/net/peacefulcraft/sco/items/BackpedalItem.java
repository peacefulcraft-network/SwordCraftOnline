package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.BackPedalSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class BackpedalItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;
    private double vectorMultiplier;
    private SwordSkillType type;
    private SwordSkillDesc desc;

    public BackpedalItem(ItemTier tier, Integer level) {
        this.tier = tier;
        this.quantity = 1;
        this.type = SwordSkillType.SWORD;

        setModifiers();

        desc = new SwordSkillDesc(tier, type);
        desc.add("Leap backwards after performing a strike.");
        desc.add("After trigger, strike with your weapon to perform back pedal.");
        switch (this.tier) {
            case COMMON:
                desc.add("Vector Multiplier: +0.3");
            break; case UNCOMMON:
                desc.add("Vector Multiplier: +0.5");
            break; case RARE:
                desc.add("Vector Multiplier: +0.7");
            break; case LEGENDARY:
                desc.add("Vector Multiplier: +0.9");
            break; case ETHEREAL:
                desc.add("Vector Multiplier: +1.1");
            break; case GODLIKE:
                desc.add("Vector Multiplier: +1.3");
        }
        desc.add("Cooldown: 10 seconds");

    }

    public BackpedalItem(ItemTier tier, Integer level, int quantity) {
        this.tier = tier;
        this.quantity = quantity;

        setModifiers();
    }

    @Override
    public String getName() {
        return "Back Pedal";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(getTier()) + "Back Pedal";
    }

    @Override
    public ArrayList<String> getLore() {
        return desc.getDesc();
    }

    @Override
    public Material getMaterial() {
        return Material.SLIME_BALL;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[]{
            ItemTier.COMMON,
            ItemTier.ETHEREAL,
            ItemTier.GODLIKE,
            ItemTier.LEGENDARY,
            ItemTier.RARE,
            ItemTier. UNCOMMON
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
        return new BackPedalSkill(caster, this.vectorMultiplier, (SwordSkillProvider) this);
    }

    public void setModifiers() {
        switch (this.tier) {
            case COMMON:
                this.vectorMultiplier = 0.3;
                break;
            case UNCOMMON:
                this.vectorMultiplier = 0.5;
                break;
            case RARE:
                this.vectorMultiplier = 0.7;
                break;
            case LEGENDARY:
                this.vectorMultiplier = 0.9;
                break;
            case ETHEREAL:
                this.vectorMultiplier = 1.1;
                break;
            case GODLIKE:
                this.vectorMultiplier = 1.3;
        }
    }
    
}
