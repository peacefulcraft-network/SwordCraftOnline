package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.GlassCannonSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class GlassCannonItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;
    private int levelModifier;

    public GlassCannonItem(ItemTier tier, int quantity) {
        this.tier = tier;
        this.quantity = quantity;

        setModifiers();
    }

    @Override
    public String getName() {
        return "Glass Cannon";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "Glass Cannon";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(tier) + "A delicate balance between");
        lore.add(ItemTier.getTierColor(tier) + "damage and health.");
        switch(tier) {
            case LEGENDARY:
                lore.add(ItemTier.getTierColor(tier) + "Max Health: -30%");
                lore.add(ItemTier.getTierColor(tier) + "True Damage: +4");
                lore.add(ItemTier.getTierColor(tier) + "Armor: -1");
            break; case ETHEREAL:
                lore.add(ItemTier.getTierColor(tier) + "Max Health: -35%");
                lore.add(ItemTier.getTierColor(tier) + "True Damage: +5");
                lore.add(ItemTier.getTierColor(tier) + "Armor: -2");
            break; case GODLIKE:
                lore.add(ItemTier.getTierColor(tier) + "Max Health: -40%");
                lore.add(ItemTier.getTierColor(tier) + "True Damage: +6");
                lore.add(ItemTier.getTierColor(tier) + "Armor: -3");
            default:
        }
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.GLASS_BOTTLE;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] {
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
        return new GlassCannonSkill(caster, levelModifier, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {
        switch(tier) {
            case LEGENDARY:
                levelModifier = 0;
            break; case ETHEREAL:
                levelModifier = 1;
            break; case GODLIKE:
                levelModifier = 2;
            default:
        }
    }
    
}
