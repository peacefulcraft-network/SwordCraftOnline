package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.GuardianSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class GuardianItem implements SwordSkillProvider {

    private int quantity;
    private int armorModifier;
    private ItemTier tier;
    private SwordSkillType type;
    private SwordSkillDesc desc;

    public GuardianItem(ItemTier tier, Integer quantity) {
        this.tier = tier;
        this.quantity = quantity;
        this.type = SwordSkillType.SECONDARY;
        this.desc = new SwordSkillDesc(tier, type);
        desc.add(ItemTier.getTierColor(this.tier) + "Protect you and your allies for 20 seconds.");
        switch(this.tier) {
            case RARE:
                desc.add("Armor: +5");
            break; case LEGENDARY:
                desc.add("Armor: +7");
            break; case ETHEREAL:
                desc.add("Armor: +9");
            break; case GODLIKE:
                desc.add("Armor: +11");
            default:
        }

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
        return desc.getDesc();
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
        return new GuardianSkill(caster, this.armorModifier, (SwordSkillProvider)this, tier);
    }

    public void setModifiers() {
        switch(this.tier) {
            case RARE:
                this.armorModifier = 5;
            break; case LEGENDARY:
                this.armorModifier = 7;
            break; case ETHEREAL:
                this.armorModifier = 8;
            break; case GODLIKE:
                this.armorModifier = 9;
            default:
             this.armorModifier = 0;
        }
    }
    
}
