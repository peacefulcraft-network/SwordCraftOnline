package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.items.utilities.Glow;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;
import net.peacefulcraft.sco.swordskills.TenCommandmentsLoveSkill;

public class TenCommandmentsLoveItem implements SwordSkillProvider, EphemeralAttributeHolder {

    private ItemTier tier;
    private int quantity;

    public TenCommandmentsLoveItem(ItemTier tier, int quantity) {
        this.tier = ItemTier.GODLIKE;
        this.quantity = quantity;
    }

    @Override
    public String getName() {
        return "Ten Commandments Love";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "Ten Commandments: Love";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(tier) + "Become the conduit for Love.");
        lore.add(ItemTier.getTierColor(tier) + "Any player with player kills");
        lore.add(ItemTier.getTierColor(tier) + "cannot deal damage in your");
        lore.add(ItemTier.getTierColor(tier) + "presence.");
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.PINK_GLAZED_TERRACOTTA;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] { ItemTier.GODLIKE };
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
    public ItemStack applyEphemeralAttributes(ItemStack item) {
        return Glow.addGlow(item);
    }

    @Override
    public void parseEphemeralAttributes(ItemStack item) {

    }

    @Override
    public SwordSkillType getType() {
        return SwordSkillType.PASSIVE;
    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        return new TenCommandmentsLoveSkill(caster, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {

    }
    
}
