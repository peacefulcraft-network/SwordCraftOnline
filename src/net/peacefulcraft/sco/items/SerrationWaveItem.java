package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.SerrationWaveSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class SerrationWaveItem implements SwordSkillProvider {

    private int quantity;
    private double damage;
    private ItemTier tier;
    private SwordSkillType type;
    private SwordSkillDesc desc;
    private int cooldown;

    public SerrationWaveItem(ItemTier tier, Integer quantity) {
        this.tier = tier;
        this.quantity = quantity;
        this.type = SwordSkillType.SWORD;
        this.desc = new SwordSkillDesc(tier, type);
        desc.add("Unleash a flurry of strikes in all directions");
        desc.add("at the cost of damage.");
        switch (this.tier) {
            case COMMON:
                desc.add("Damage: 0.5x main weapon");
                desc.add("Cooldown: 7 Seconds");
            break; case UNCOMMON:
                desc.add("Damage: 0.7x main weapon");
                desc.add("Cooldown: 8 Seconds");
            break; case RARE:
                desc.add("Damage: 0.9x main weapon");
                desc.add("Cooldown: 8 Seconds");
            break; case LEGENDARY:
                desc.add("Damage: 1.1x main weapon");
                desc.add("Cooldown: 9 Seconds");
            break; case ETHEREAL:
                desc.add("Damage: 1.3x main weapon");
                desc.add("Cooldown: 9 Seconds");
            break; case GODLIKE:
                desc.add("Damage: 1.5x main weapon");
                desc.add("Cooldown: 11 Seconds");
        }

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
        return desc.getDesc();
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
        return type;
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
