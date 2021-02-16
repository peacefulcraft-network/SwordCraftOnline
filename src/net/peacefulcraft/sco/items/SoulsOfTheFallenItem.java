package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.SoulsOfTheFallenSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class SoulsOfTheFallenItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;
    private int levelModifier;

    public SoulsOfTheFallenItem(ItemTier tier, int quantity) {
        this.tier = tier;
        this.quantity = quantity;

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
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(tier) + "Call upon the souls");
        lore.add(ItemTier.getTierColor(tier) + "of players you've killed.");
        lore.add(ItemTier.getTierColor(tier) + "On trigger: Boosts true damage.");
        switch(tier) {
            case ETHEREAL:
                lore.add(ItemTier.getTierColor(tier) + "True Damage: x2.0 + 0.1");
                lore.add(ItemTier.getTierColor(tier) + "per player kill.");
            break; case GODLIKE:
                lore.add(ItemTier.getTierColor(tier) + "True Damage: x2.0 + 0.2");
                lore.add(ItemTier.getTierColor(tier) + "per player kill.");
            default:
        }
        lore.add(ItemTier.getTierColor(tier) + "Cooldown: 60 seconds");
        return lore;
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
        return SwordSkillType.SWORD;
    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        return new SoulsOfTheFallenSkill(caster, levelModifier, (SwordSkillProvider)this);
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
