package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.SoulsOfTheFallenSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class SoulsOfTheFallenItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;
    private SwordSkillType type;
    private SwordSkillDesc desc;
    private int levelModifier;

    public SoulsOfTheFallenItem(ItemTier tier, Integer quantity) {
        this.tier = tier;
        this.quantity = quantity;
        this.type = SwordSkillType.SWORD;
        this.desc = new SwordSkillDesc(tier, type);
        desc.add("Call upon the souls");
        desc.add("of players you've killed.");
        desc.add("On trigger: Boosts true damage.");
        switch(tier) {
            case ETHEREAL:
                desc.add("True Damage: x2.0 + 0.1");
                desc.add("per player kill.");
            break; case GODLIKE:
                desc.add("True Damage: x2.0 + 0.2");
                desc.add("per player kill.");
            default:
        }
        desc.add("Cooldown: 60 seconds");

        setModifiers();
    }

    @Override
    public String getName() {
        return "Souls Of The Fallen";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "Souls Of The Fallen";
    }

    @Override
    public ArrayList<String> getLore() {
        return desc.getDesc();
    }

    @Override
    public Material getMaterial() {
        return Material.SKELETON_SKULL;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] {
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
        return new SoulsOfTheFallenSkill(caster, levelModifier, (SwordSkillProvider)this, tier);
    }

    @Override
    public void setModifiers() {
        switch(tier) {
            case ETHEREAL:
                this.levelModifier = 0;
            break; case GODLIKE:
                this.levelModifier = 1;
            default:
        }
    }
    
}
