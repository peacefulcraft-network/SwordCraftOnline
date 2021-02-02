package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.LegDaySkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class LegDayItem implements SwordSkillProvider {

    private int quantity;
    private ItemTier tier;
    private double movementModifier;

    public LegDayItem(int quantity, ItemTier tier) {
        this.tier = tier;
        this.quantity = quantity;

        setModifiers();
    }

    @Override
    public String getName() {
        return "Leg Day";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(this.tier) + "Leg Day";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(this.tier) + "Train your legs to increase");
        lore.add(ItemTier.getTierColor(this.tier) + "your movement speed.");
        switch(this.tier) {
            case COMMON:
                lore.add(ItemTier.getTierColor(this.tier) + "Movement Speed: +2.0");
            case UNCOMMON:
                lore.add(ItemTier.getTierColor(this.tier) + "Movement Speed: +2.2");
            case RARE:
                lore.add(ItemTier.getTierColor(this.tier) + "Movement Speed: +2.4");
            case LEGENDARY:
                lore.add(ItemTier.getTierColor(this.tier) + "Movement Speed: +2.6");
            case ETHEREAL:
                lore.add(ItemTier.getTierColor(this.tier) + "Movement Speed: +2.8");
            case GODLIKE:
                lore.add(ItemTier.getTierColor(this.tier) + "Movement Speed: +3.0");
        }
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.SUGAR;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] {
            ItemTier.COMMON,
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
        return new LegDaySkill(caster, this.movementModifier, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {
        switch(this.tier) {
            case COMMON:
                this.movementModifier = 0.0;
            break; case UNCOMMON:
                this.movementModifier = 0.2;
            break; case RARE:
                this.movementModifier = 0.4;
            break; case LEGENDARY:
                this.movementModifier = 0.6;
            break; case ETHEREAL:
                this.movementModifier = 0.8;
            break; case GODLIKE:
                this.movementModifier = 1.0;
        }
    }
    
}
