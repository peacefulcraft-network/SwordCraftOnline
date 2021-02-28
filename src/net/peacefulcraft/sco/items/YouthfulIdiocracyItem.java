package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;
import net.peacefulcraft.sco.swordskills.YouthfulIdiocracySkill;

public class YouthfulIdiocracyItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;
    private SwordSkillType type;
    private SwordSkillDesc desc;
    private int levelModifier;

    public YouthfulIdiocracyItem(ItemTier tier, Integer quantity) {
        this.tier = tier;
        this.quantity = quantity;
        this.type = SwordSkillType.PASSIVE;
        this.desc = new SwordSkillDesc(tier, type);
        desc.add("Be inspired by the");
        desc.add("power of youth!");
        switch(tier) {
            case RARE:
                desc.add("Movement Speed: +2");
                desc.add("Max Health: +20%");
                desc.add("Armor: -1");
            break; case LEGENDARY:
                desc.add("Movement Speed: +3");
                desc.add("Max Health: +25%");
                desc.add("Armor: -2");
            break; case ETHEREAL:
                desc.add("Movement Speed: +4");
                desc.add("Max Health: +30%");
                desc.add("Armor: -3");
            break; case GODLIKE:
                desc.add("Movement Speed: +5");
                desc.add("Max Health: +35%");
                desc.add("Armor: -4");
            default:
        }

        setModifiers();
    }

    @Override
    public String getName() {
        return "Youthful Idiocracy";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "Youthful Idiocracy";
    }

    @Override
    public ArrayList<String> getLore() {
        return desc.getDesc();
    }

    @Override
    public Material getMaterial() {
        return Material.OAK_SAPLING;
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
        return new YouthfulIdiocracySkill(caster, levelModifier, (SwordSkillProvider)this);
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
