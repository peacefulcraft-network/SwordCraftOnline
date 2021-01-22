package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.GuardianSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class GuardianItem implements SwordSkillProvider {

    private int quantity;
    private double armorModifier;
    private ItemTier tier;

    public GuardianItem(ItemTier tier, Integer level) {
        this(tier, level, 1);
    }

    public GuardianItem(ItemTier tier, Integer level, int quantity) {
        this.tier = tier;
        this.quantity = quantity;

        setModifiers();
    }

    @Override
    public String getName() {
        return "Guardian";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(this.tier) + "Guardian";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ItemTier.getTierColor(this.tier) + "Protect you and your allies for 20 seconds.");
        switch(this.tier) {
            case RARE:
                lore.add(ItemTier.getTierColor(this.tier) + "Armor: x1.5");
            break; case LEGENDARY:
                lore.add(ItemTier.getTierColor(this.tier) + "Armor: x1.7");
            break; case ETHEREAL:
                lore.add(ItemTier.getTierColor(this.tier) + "Armor: x1.9");
            break; case GODLIKE:
                lore.add(ItemTier.getTierColor(this.tier) + "Armor: x2.0");
            default:
                lore.add("Severe Error: Skill not functional, Contact Admin for correction.");
        }
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.SHULKER_SHELL;
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
        return SwordSkillType.PRIMARY;
    }

    @Override
    public Integer[] getAllowedLevels() {
        return new Integer[] { 1, 2, 3};
    }

    @Override
    public Integer getLevel() {
        return 1;
    }

    @Override
    public void setLevel(Integer level) {

    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        return new GuardianSkill(caster, this.armorModifier, (SwordSkillProvider)this);
    }

    public void setModifiers() {
        switch(this.tier) {
            case RARE:
                this.armorModifier = 1.5;
            break; case LEGENDARY:
                this.armorModifier = 1.7;
            break; case ETHEREAL:
                this.armorModifier = 1.9;
            break; case GODLIKE:
                this.armorModifier = 2.0;
            default:
             this.armorModifier = 0.0;
        }
    }
    
}
