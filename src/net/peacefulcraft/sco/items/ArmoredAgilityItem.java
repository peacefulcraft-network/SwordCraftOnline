package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.ArmoredAgilitySkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class ArmoredAgilityItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;
    private int levelModifier;
    private SwordSkillType type;
    private SwordSkillDesc desc;

    public ArmoredAgilityItem(ItemTier tier, Integer quantity) {
        this.tier = tier;
        this.quantity = quantity;
        this.type = SwordSkillType.PASSIVE;

        setModifiers();

        desc = new SwordSkillDesc(tier, type);
        desc.add("Upgrade your armor with");
        desc.add("tougher, more mobile metal.");
        switch(tier) {
            case RARE:
                desc.add("Movement Speed: +2");
                desc.add("Armor: +2");
            break; case LEGENDARY:
                desc.add("Movement Speed: +3");
                desc.add("Armor: +3");
            break; case ETHEREAL:
                desc.add("Movement Speed: +4");
                desc.add("Armor: +4");
            break; case GODLIKE:
                desc.add("Movement Speed: +5");
                desc.add("Armor: +5");
            default:
        }
    }

    @Override
    public String getName() {
        return "Armored Agility";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "Armored Agility";
    }

    @Override
    public ArrayList<String> getLore() {
        return desc.getDesc();
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_NUGGET;
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
        return new ArmoredAgilitySkill(caster, levelModifier, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {
        switch(tier) {
            case RARE:
                this.levelModifier = 0;
            break; case LEGENDARY:
                this.levelModifier = 1;
            break; case ETHEREAL:
                this.levelModifier = 2;
            break; case GODLIKE:
                this.levelModifier = 3;
            default:
        }
    }
    
}
