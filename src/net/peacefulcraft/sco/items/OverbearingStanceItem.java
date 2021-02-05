package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.OverbearingStanceSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class OverbearingStanceItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;
    private int knockbackModifier;

    public OverbearingStanceItem(ItemTier tier, int quantity) {
        this.tier = tier;
        this.quantity = quantity;
        
        setModifiers();
    }

    @Override
    public String getName() {
        return "Overbearing Stance";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "Overbearing Stance";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(tier) + "A firm stance increases");
        lore.add(ItemTier.getTierColor(tier) + "your knockback resistance.");
        switch(tier) {
            case RARE:
                lore.add(ItemTier.getTierColor(tier) + "Knockback Resistance: +4");
            break; case LEGENDARY:
                lore.add(ItemTier.getTierColor(tier) + "Knockback Resistance: +5");
            break; case ETHEREAL:
                lore.add(ItemTier.getTierColor(tier) + "Knockback Resistance: +6");
            break; case GODLIKE:
                lore.add(ItemTier.getTierColor(tier) + "Knockback Resistance: +7");
            default:
        }
        lore.add(ItemTier.getTierColor(tier) + "Movement Speed: -1");
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_BARS;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] {
            ItemTier.RARE,
            ItemTier.ETHEREAL,
            ItemTier.LEGENDARY,
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
        return new OverbearingStanceSkill(caster, this.knockbackModifier, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {
        switch(tier) {
            case RARE:
                this.knockbackModifier = 0;
            break; case LEGENDARY:
                this.knockbackModifier = 1;
            break; case ETHEREAL:
                this.knockbackModifier = 2;
            break; case GODLIKE:
                this.knockbackModifier = 3;
            default:
        }
    }
    
}
