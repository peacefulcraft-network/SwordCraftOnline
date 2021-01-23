package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.SerrationWaveSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class SerrationWaveItem implements SwordSkillProvider {

    private int quantity;
    private double damage;
    private ItemTier tier;
    private int cooldown;

    public SerrationWaveItem(ItemTier tier, Integer level) {
        this.tier = tier;
        this.quantity = 1;

        setModifiers();
    }

    public SerrationWaveItem(ItemTier tier, Integer level, int quantity) {
        this.quantity = quantity;
        this.tier = tier;

        setModifiers();
    }

    @Override
    public String getName() {
        return "Serration Wave";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(this.tier) + "Serration Wave";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ItemTier.getTierColor(this.tier) + "Unleash a flurry of strikes in all directions");
        lore.add(ItemTier.getTierColor(this.tier) + "at the cost of damage.");
        switch (this.tier) {
            case COMMON:
                lore.add(ItemTier.getTierColor(this.tier) + "Damage: 0.5x main weapon");
                lore.add(ItemTier.getTierColor(this.tier) + "Cooldown: 7 Seconds");
                break;
            case UNCOMMON:
                lore.add(ItemTier.getTierColor(this.tier) + "Damage: 0.7x main weapon");
                lore.add(ItemTier.getTierColor(this.tier) + "Cooldown: 8 Seconds");
                break;
            case RARE:
                lore.add(ItemTier.getTierColor(this.tier) + "Damage: 0.9x main weapon");
                lore.add(ItemTier.getTierColor(this.tier) + "Cooldown: 8 Seconds");
                break;
            case LEGENDARY:
                lore.add(ItemTier.getTierColor(this.tier) + "Damage: 1.1x main weapon");
                lore.add(ItemTier.getTierColor(this.tier) + "Cooldown: 9 Seconds");
                break;
            case ETHEREAL:
                lore.add(ItemTier.getTierColor(this.tier) + "Damage: 1.3x main weapon");
                lore.add(ItemTier.getTierColor(this.tier) + "Cooldown: 9 Seconds");
                break;
            case GODLIKE:
                lore.add(ItemTier.getTierColor(this.tier) + "Damage: 1.5x main weapon");
                lore.add(ItemTier.getTierColor(this.tier) + "Cooldown: 11 Seconds");
        }

        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.CYAN_DYE;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] { ItemTier.COMMON, ItemTier.ETHEREAL, ItemTier.GODLIKE, ItemTier.LEGENDARY, ItemTier.RARE,
                ItemTier.UNCOMMON };
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
        return new SerrationWaveSkill(caster, this.damage, this.cooldown * 1000, (SwordSkillProvider) this);
    }

    public void setModifiers() {
        switch (this.tier) {
            case COMMON:
                this.damage = 0.5;
                this.cooldown = 7;
                break;
            case UNCOMMON:
                this.damage = 0.7;
                this.cooldown = 8;
                break;
            case RARE:
                this.damage = 0.9;
                this.cooldown = 8;
                break;
            case LEGENDARY:
                this.damage = 1.1;
                this.cooldown = 9;
                break;
            case ETHEREAL:
                this.damage = 1.3;
                this.cooldown = 9;
                break;
            case GODLIKE:
                this.damage = 1.5;
                this.cooldown = 11;
        }
    }
    
}
