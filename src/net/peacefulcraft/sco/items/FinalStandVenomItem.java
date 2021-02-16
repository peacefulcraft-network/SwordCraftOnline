package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.FinalStandVenomSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class FinalStandVenomItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;

    public FinalStandVenomItem(ItemTier tier, int quantity) {
        this.tier = ItemTier.LEGENDARY;
        this.quantity = quantity;
    }

    @Override
    public String getName() {
        return "Final Stand Venom";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "Final Stand: Venom";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(tier) + "The pinnacle of poison.");
        lore.add(ItemTier.getTierColor(tier) + "Inflict Poison III for");
        lore.add(ItemTier.getTierColor(tier) + "10 seconds on every entity");
        lore.add(ItemTier.getTierColor(tier) + "within 5 block radius.");
        lore.add(ItemTier.getTierColor(tier) + "Cooldown: 35 seconds");
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.LIME_CONCRETE_POWDER;
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
        return new FinalStandVenomSkill(caster, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {

    }
    
}
