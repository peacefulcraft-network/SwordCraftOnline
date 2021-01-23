package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.SevereUpwardStrikeSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class SevereUpwardStrikeItem implements SwordSkillProvider {

    private int quantity;
    private ItemTier tier;
    private int cooldown;
    private double vectorMultiplier;

    public SevereUpwardStrikeItem(ItemTier tier, Integer level) {
        this(tier, level, 1);
    }

    public SevereUpwardStrikeItem(ItemTier tier, Integer level, int quantity) {
        this.quantity = quantity;
        this.tier = tier;

        setModifiers();
    }

    @Override
    public String getName() {
        return "Severe Upward Strike";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(this.tier) + "Severe Upward Strike";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ItemTier.getTierColor(this.tier) + "A low to high strike that launches");
        lore.add(ItemTier.getTierColor(this.tier) + "your foe into the air.");
        switch (this.tier) {
            case COMMON:
                lore.add(ItemTier.getTierColor(this.tier) + "Vector Multiplier: +0.1");
                lore.add(ItemTier.getTierColor(this.tier) + "Cooldown: 7 Seconds");
                break;
            case UNCOMMON:
                lore.add(ItemTier.getTierColor(this.tier) + "Vector Multiplier: +0.3");
                lore.add(ItemTier.getTierColor(this.tier) + "Cooldown: 8 Seconds");
                break;
            case RARE:
                lore.add(ItemTier.getTierColor(this.tier) + "Vector Multiplier: +0.5");
                lore.add(ItemTier.getTierColor(this.tier) + "Cooldown: 8 Seconds");
                break;
            case LEGENDARY:
                lore.add(ItemTier.getTierColor(this.tier) + "Vector Multiplier: +0.7");
                lore.add(ItemTier.getTierColor(this.tier) + "Cooldown: 9 Seconds");
                break;
            case ETHEREAL:
                lore.add(ItemTier.getTierColor(this.tier) + "Vector Multiplier: +0.9");
                lore.add(ItemTier.getTierColor(this.tier) + "Cooldown: 9 Seconds");
                break;
            case GODLIKE:
                lore.add(ItemTier.getTierColor(this.tier) + "Vector Multiplier: +1.1");
                lore.add(ItemTier.getTierColor(this.tier) + "Cooldown: 11 Seconds");
        }

        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.FEATHER;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] { 
            ItemTier.COMMON, 
            ItemTier.ETHEREAL, 
            ItemTier.GODLIKE, 
            ItemTier.LEGENDARY, 
            ItemTier.RARE,
            ItemTier.UNCOMMON 
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
        return SwordSkillType.SWORD;
    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        return new SevereUpwardStrikeSkill(
            caster, 
            this.vectorMultiplier, 
            this.cooldown * 1000, 
            (SwordSkillProvider) this
        );
    }
    
    public void setModifiers() {
        switch (this.tier) {
            case COMMON:
                this.vectorMultiplier = 0.1;
                this.cooldown = 7;
                break;
            case UNCOMMON:
            this.vectorMultiplier = 0.3;
                this.cooldown = 8;
                break;
            case RARE:
            this.vectorMultiplier = 0.5;
                this.cooldown = 8;
                break;
            case LEGENDARY:
            this.vectorMultiplier = 0.7;
                this.cooldown = 9;
                break;
            case ETHEREAL:
            this.vectorMultiplier = 0.9;
                this.cooldown = 9;
                break;
            case GODLIKE:
            this.vectorMultiplier = 1.1;
                this.cooldown = 11;
        }
    }
}
