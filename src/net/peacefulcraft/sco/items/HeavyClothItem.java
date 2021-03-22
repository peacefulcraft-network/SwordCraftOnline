package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.HeavyClothSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class HeavyClothItem implements SwordSkillProvider {

    private int quantity;
    private ItemTier tier;
    private SwordSkillType type;
    private SwordSkillDesc desc;
    private int armorModifier;

    public HeavyClothItem(ItemTier tier, Integer quantity) {
        this.quantity = quantity;
        this.tier = tier;
        this.type = SwordSkillType.PASSIVE;
        this.desc = new SwordSkillDesc(tier, type);
        desc.add("Weave Aincradian Steel into");
        desc.add("your clothing.");
        switch(this.tier) {
            case UNCOMMON:
                desc.add("Armor: +2");
                desc.add("Armor Toughness +2");
            break; case RARE:
                desc.add("Armor: +4");
                desc.add("Armor Toughness +4");
            break; case LEGENDARY:
                desc.add("Armor: +6");
                desc.add("Armor Toughness +6");
            break; case ETHEREAL:
                desc.add("Armor: +8");
                desc.add("Armor Toughness +8");
            break; case GODLIKE:
                desc.add("Armor: +10");
                desc.add("Armor Toughness +10");
            default:
        }

        setModifiers();
    }

    @Override
    public String getName() {
        return "Heavy Cloth";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(this.tier) + "Heavy Cloth";
    }

    @Override
    public ArrayList<String> getLore() {
        return desc.getDesc();
    }

    @Override
    public Material getMaterial() {
        return Material.BLACK_WOOL;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] {
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
        return new HeavyClothSkill(caster, this.armorModifier, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {
        switch(this.tier) {
            case UNCOMMON:
                this.armorModifier = 0;
            case RARE:
                this.armorModifier = 2;
            case LEGENDARY:
                this.armorModifier = 4;
            case ETHEREAL:
                this.armorModifier = 6;
            case GODLIKE:
                this.armorModifier = 8;
            default:
        }
    }
    
}
