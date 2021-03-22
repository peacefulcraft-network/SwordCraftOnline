package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.SevereUpwardStrikeSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class SevereUpwardStrikeItem implements SwordSkillProvider {

    private int quantity;
    private ItemTier tier;
    private SwordSkillType type;
    private SwordSkillDesc desc;
    private int cooldown;
    private double vectorMultiplier;

    public SevereUpwardStrikeItem(ItemTier tier, Integer quantity) {
        this.tier = tier;
        this.quantity = quantity;
        this.type = SwordSkillType.SWORD;

        setModifiers();

        this.desc = new SwordSkillDesc(tier, type);
        desc.add("A low to high strike that launches");
        desc.add("your foe into the air.");
        desc.add("");
        desc.add("Vector Multiplier: +" + vectorMultiplier);
        desc.add("Cooldown: " + cooldown + " Seconds");
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
        return desc.getDesc();
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
