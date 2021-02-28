package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.ForwardLungeSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class ForwardLungeItem implements SwordSkillProvider {

    @Override
    public String getName() { return "Forward Lunge"; }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(getTier()) + "Forward Lunge";
    }

    @Override
    public ArrayList<String> getLore() {
        return desc.getDesc();
    }

    @Override
    public Material getMaterial() { return Material.BIRCH_DOOR; }

    private ItemTier tier;
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
        public ItemTier getTier() { return this.tier; }

    private Integer quantity;
        @Override
        public Integer getQuantity() { return this.quantity;}

        @Override
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

    @Override
    public boolean isDroppable() { return false; }

    @Override
    public boolean isMovable() { return true; }

    private Double increase;
    private SwordSkillType type;
    private SwordSkillDesc desc;

    public ForwardLungeItem(ItemTier tier, Integer quantity) {
        this.tier = tier;
        this.quantity = quantity;
        this.type = SwordSkillType.SWORD;
        this.desc = new SwordSkillDesc(tier, type);
        desc.add("Lunge forward with a burst");
        desc.add("of strength.");
        switch (this.tier) {
            case COMMON:
                desc.add("Damage Increased 20% for 2 seconds.");
            break; case UNCOMMON:
                desc.add("Damage Increased 30% for 2 seconds.");
            break; case RARE:
                desc.add("Damage Increased 40% for 2 seconds.");
            break; case LEGENDARY:
                desc.add("Damage Increased 50% for 2 seconds.");
            break; case ETHEREAL:
                desc.add("Damage Increased 60% for 2 seconds.");
            break; case GODLIKE:
                desc.add("Damage Increased 70% for 2 seconds.");
        }

        this.setModifiers();
    }

    public void setModifiers() {
        switch (this.tier) {
            case COMMON:
                this.increase = 1.2;
                break;
            case UNCOMMON:
                this.increase = 1.3;
                break;
            case RARE:
                this.increase = 1.4;
                break;
            case LEGENDARY:
                this.increase = 1.5;
                break;
            case ETHEREAL:
                this.increase = 1.6;
                break;
            case GODLIKE:
                this.increase = 1.7;
        }
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
    public SwordSkillType getType() { return type; }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        switch (this.tier) {
            case UNCOMMON:
                return new ForwardLungeSkill(caster, this.increase, 15000L, (SwordSkillProvider) this);
            case RARE:
                return new ForwardLungeSkill(caster, this.increase, 15000L, (SwordSkillProvider) this);
            case LEGENDARY:
                return new ForwardLungeSkill(caster, this.increase, 15000L, (SwordSkillProvider) this);
            case ETHEREAL:
                return new ForwardLungeSkill(caster, this.increase, 15000L, (SwordSkillProvider) this);
            case GODLIKE:
                return new ForwardLungeSkill(caster, this.increase, 15000L, (SwordSkillProvider) this);
            default:
                return new ForwardLungeSkill(caster, this.increase, 15000L, (SwordSkillProvider) this);
        }
    }
    
}