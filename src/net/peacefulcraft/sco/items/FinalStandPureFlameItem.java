package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.FinalStandPureFlameSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class FinalStandPureFlameItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;

    public FinalStandPureFlameItem(ItemTier tier, int quantity) {
        this.tier = ItemTier.LEGENDARY;
        this.quantity = quantity;
    }

    @Override
    public String getName() {
        return "Final Stand Pure Flame";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "Final Stand: Pure Flame";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(tier) + "Wield the strongest flames");
        lore.add(ItemTier.getTierColor(tier) + "in Aincrad.");
        lore.add(ItemTier.getTierColor(tier) + "Light every entity in 5");
        lore.add(ItemTier.getTierColor(tier) + "blocks for 10 seconds.");
        lore.add(ItemTier.getTierColor(tier) + "Max Health: 80% for 10 seconds");
        lore.add(ItemTier.getTierColor(tier) + "Cooldown: 35 seconds");
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.GRAY_CONCRETE_POWDER;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] { ItemTier.LEGENDARY };
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
        return SwordSkillType.PRIMARY;
    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        return new FinalStandPureFlameSkill(caster, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {

    }
    
}
