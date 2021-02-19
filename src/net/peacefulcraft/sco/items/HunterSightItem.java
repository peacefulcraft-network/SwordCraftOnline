package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.HunterSightSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class HunterSightItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;

    public HunterSightItem(ItemTier tier, int quantity) {
        this.tier = ItemTier.UNCOMMON;
        this.quantity = quantity;
    }

    @Override
    public String getName() {
        return "Hunter Sight";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "Hunter Sight";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(tier) + "Enhance your eyesight to");
        lore.add(ItemTier.getTierColor(tier) + "see those all in your area.");
        lore.add(ItemTier.getTierColor(tier) + "Glow effect: 20 seconds");
        lore.add(ItemTier.getTierColor(tier) + "Cooldown: 43 seconds");
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.LEATHER;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] { ItemTier.UNCOMMON };
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
        return SwordSkillType.SECONDARY;
    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        return new HunterSightSkill(caster, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {

    }
    
}
