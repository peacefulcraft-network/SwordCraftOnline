package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.ArmoredAgilitySkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class ArmoredAgilityItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;
    private int levelModifier;

    public ArmoredAgilityItem(ItemTier tier, Integer quantity) {
        this.tier = tier;
        this.quantity = quantity;

        setModifiers();
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
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(tier) + "Upgrade your armor with");
        lore.add(ItemTier.getTierColor(tier) + "tougher, more mobile metal.");
        switch(tier) {
            case RARE:
                lore.add(ItemTier.getTierColor(tier) + "Movement Speed: +2");
                lore.add(ItemTier.getTierColor(tier) + "Armor: +2");
            break; case LEGENDARY:
                lore.add(ItemTier.getTierColor(tier) + "Movement Speed: +3");
                lore.add(ItemTier.getTierColor(tier) + "Armor: +3");
            break; case ETHEREAL:
                lore.add(ItemTier.getTierColor(tier) + "Movement Speed: +4");
                lore.add(ItemTier.getTierColor(tier) + "Armor: +4");
            break; case GODLIKE:
                lore.add(ItemTier.getTierColor(tier) + "Movement Speed: +5");
                lore.add(ItemTier.getTierColor(tier) + "Armor: +5");
            default:
        }
        return lore;
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
        return SwordSkillType.PASSIVE;
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
