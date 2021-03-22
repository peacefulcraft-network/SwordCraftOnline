package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.HighGearSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class HighGearItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;
    private SwordSkillType type;
    private SwordSkillDesc desc;
    private int levelModifier;

    public HighGearItem(ItemTier tier, Integer quantity) {
        this.tier = tier;
        this.quantity = quantity;
        this.type = SwordSkillType.PASSIVE;
        this.desc = new SwordSkillDesc(tier, type);
        desc.add("Shift your body into");
        desc.add("high gear.");
        switch(tier) {
            case RARE:
                desc.add("True Damage: +3");
                desc.add("Movement Speed: +20%");
                desc.add("Parry Chance: -20%");
            break; case LEGENDARY:
                desc.add("True Damage: +4");
                desc.add("Movement Speed: +22%");
                desc.add("Parry Chance: -22%");
            break; case ETHEREAL:
                desc.add("True Damage: +5");
                desc.add("Movement Speed: +24%");
                desc.add("Parry Chance: -24%");
            break; case GODLIKE:
                desc.add("True Damage: +6");
                desc.add("Movement Speed: +26%");
                desc.add("Parry Chance: -26%");
            default:
        }
        
        setModifiers();
    }

    @Override
    public String getName() {
        return "High Gear";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "High Gear";
    }

    @Override
    public ArrayList<String> getLore() {
        return desc.getDesc();
    }

    @Override
    public Material getMaterial() {
        return Material.GLOWSTONE_DUST;
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
        return new HighGearSkill(caster, levelModifier, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {
        switch(tier) {
            case RARE:
                levelModifier = 0;
            break; case LEGENDARY:
                levelModifier = 1;
            break; case ETHEREAL:
                levelModifier = 2;
            break; case GODLIKE:
                levelModifier = 3;
            default:
        }
    }
    
}
