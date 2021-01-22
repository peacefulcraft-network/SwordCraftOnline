package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;
import net.peacefulcraft.sco.swordskills.UnfortunateDivisorSkill;

public class UnfortunateDivisorItem implements SwordSkillProvider {

    private int quantity;
    private ItemTier tier;

    public UnfortunateDivisorItem(ItemTier tier, Integer level) {
        this(tier, level, 1);
    }

    public UnfortunateDivisorItem(ItemTier tier, Integer level, int quantity) {
        this.quantity = quantity;
        this.tier = ItemTier.ETHEREAL;
    }

    @Override
    public String getName() {
        return "Unfortunate Divisor";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(this.tier) + "Unfortunate Divisor";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ItemTier.getTierColor(this.tier) + "Slice your opponents soul in half.");
        lore.add(ItemTier.getTierColor(this.tier) + "Divides victims health by 2.");
        lore.add(ItemTier.getTierColor(this.tier) + "Cooldown 30 seconds.");
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_NUGGET;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] { ItemTier.ETHEREAL };
    }

    @Override
    public ItemTier getTier() {
        return ItemTier.ETHEREAL;
    }

    @Override
    public Integer getQuantity() {
        return this.quantity;
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
    public Integer[] getAllowedLevels() {
        return new Integer[] { 1, 2, 3};
    }

    @Override
    public Integer getLevel() {
        return 1;
    }

    @Override
    public void setLevel(Integer level) {

    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        return new UnfortunateDivisorSkill(caster, (SwordSkillProvider) this);
    }

    @Override
    public void setModifiers() {

    }
    
}
