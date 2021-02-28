package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.ParrySkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class ParryItem implements SwordSkillProvider {

    @Override
    public String getName() { return "Parry"; }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(getTier()) + "Parry";
    }

    @Override
    public ArrayList<String> getLore() {
        return desc.getDesc();
    }

    @Override
    public Material getMaterial() { return Material.IRON_INGOT; }

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
        public Integer getQuantity() { return this.quantity; }

        @Override
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

    @Override
    public boolean isDroppable() { return false; }

    @Override
    public boolean isMovable() { return true; }

    private int increase;
    private SwordSkillType type;
    private SwordSkillDesc desc;

    public ParryItem(ItemTier tier, Integer quantity) {
        this.tier = tier;
        this.quantity = quantity;
        this.type = SwordSkillType.PASSIVE;
        this.desc = new SwordSkillDesc(tier, type);
        desc.add("A beginners parry technique.");
        switch (this.tier) {
            case COMMON:
                desc.add("Parry Chance: +5%");
            break; case UNCOMMON:
                desc.add("Parry Chance: +7%");
            break; case RARE:
                desc.add("Parry Chance: +9%");
            break; case LEGENDARY:
                desc.add("Parry Chance: +11%");
            break; case ETHEREAL:
                desc.add("Parry Chance: +13%");
            break; case GODLIKE:
                desc.add("Parry Chance: +15%");
        }

        setModifiers();
    }

    public void setModifiers() {
        switch (this.tier) {
            case COMMON:
                this.increase = 5;
                break;
            case UNCOMMON:
                this.increase = 7;
                break;
            case RARE:
                this.increase = 9;
                break;
            case LEGENDARY:
                this.increase = 11;
                break;
            case ETHEREAL:
                this.increase = 13;
                break;
            case GODLIKE:
                this.increase = 15;
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
                return new ParrySkill(caster, this.increase, 10L, (SwordSkillProvider) this);
            case RARE:
                return new ParrySkill(caster, this.increase, 10L, (SwordSkillProvider) this);
            case LEGENDARY:
                return new ParrySkill(caster, this.increase, 10L, (SwordSkillProvider) this);
            case ETHEREAL:
                return new ParrySkill(caster, this.increase, 10L, (SwordSkillProvider) this);
            case GODLIKE:
                return new ParrySkill(caster, this.increase, 10L, (SwordSkillProvider) this);
            default:
                return new ParrySkill(caster, this.increase, 10L, (SwordSkillProvider) this);
        }
    }
    
}