package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.HeavyClothSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class HeavyClothItem implements SwordSkillProvider {

    private int quantity;
    private ItemTier tier;
    private int armorModifier;

    public HeavyClothItem(ItemTier tier, int quantity) {
        this.quantity = quantity;
        this.tier = tier;

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
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(this.tier) +"Weave Aincradian Steel into");
        lore.add(ItemTier.getTierColor(this.tier) +"your clothing.");
        switch(this.tier) {
            case UNCOMMON:
                lore.add(ItemTier.getTierColor(this.tier) +"Armor: +2");
                lore.add(ItemTier.getTierColor(this.tier) +"Armor Toughness +2");
            break; case RARE:
                lore.add(ItemTier.getTierColor(this.tier) +"Armor: +4");
                lore.add(ItemTier.getTierColor(this.tier) +"Armor Toughness +4");
            break; case LEGENDARY:
                lore.add(ItemTier.getTierColor(this.tier) +"Armor: +6");
                lore.add(ItemTier.getTierColor(this.tier) +"Armor Toughness +6");
            break; case ETHEREAL:
                lore.add(ItemTier.getTierColor(this.tier) +"Armor: +8");
                lore.add(ItemTier.getTierColor(this.tier) +"Armor Toughness +8");
            break; case GODLIKE:
                lore.add(ItemTier.getTierColor(this.tier) +"Armor: +10");
                lore.add(ItemTier.getTierColor(this.tier) +"Armor Toughness +10");
            default:
        }
        return lore;
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
