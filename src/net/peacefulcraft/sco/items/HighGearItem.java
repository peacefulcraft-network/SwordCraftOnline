package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.HighGearSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class HighGearItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;
    private int levelModifier;

    public HighGearItem(ItemTier tier, int quantity) {
        this.tier = tier;
        this.quantity = quantity;
        
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
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(tier) + "Shift your body into");
        lore.add(ItemTier.getTierColor(tier) + "high gear.");
        switch(tier) {
            case RARE:
                lore.add(ItemTier.getTierColor(tier) + "True Damage: +3");
                lore.add(ItemTier.getTierColor(tier) + "Movement Speed: +2");
                lore.add(ItemTier.getTierColor(tier) + "Parry Chance: -20%");
            break; case LEGENDARY:
                lore.add(ItemTier.getTierColor(tier) + "True Damage: +4");
                lore.add(ItemTier.getTierColor(tier) + "Movement Speed: +3");
                lore.add(ItemTier.getTierColor(tier) + "Parry Chance: -22%");
            break; case ETHEREAL:
                lore.add(ItemTier.getTierColor(tier) + "True Damage: +5");
                lore.add(ItemTier.getTierColor(tier) + "Movement Speed: +4");
                lore.add(ItemTier.getTierColor(tier) + "Parry Chance: -24%");
            break; case GODLIKE:
                lore.add(ItemTier.getTierColor(tier) + "True Damage: +6");
                lore.add(ItemTier.getTierColor(tier) + "Movement Speed: +5");
                lore.add(ItemTier.getTierColor(tier) + "Parry Chance: -26%");
            default:
        }
        return lore;
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
        return SwordSkillType.PASSIVE;
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
