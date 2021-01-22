package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.ElephantHeartSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class ElephantHeartItem implements SwordSkillProvider {

    private int quantity;

    public ElephantHeartItem(ItemTier tier, Integer level) {
        this(tier, level, 1);
    }

    public ElephantHeartItem(ItemTier tier, Integer level, int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String getName() {
        return "Elephant Heart";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(ItemTier.RARE) + "Elephant Heart";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ItemTier.getTierColor(ItemTier.RARE) + "Empower yourself with the heart");
        lore.add(ItemTier.getTierColor(ItemTier.RARE) + "of the Aincradian Elephant.");
        lore.add(ItemTier.getTierColor(ItemTier.RARE) + "Max Health: +50%");
        lore.add(ItemTier.getTierColor(ItemTier.RARE) + "Movement Speed: -20%");
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.RED_DYE;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] { ItemTier.RARE };
    }

    @Override
    public ItemTier getTier() {
        return ItemTier.RARE;
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
        return SwordSkillType.PASSIVE;
    }

    @Override
    public Integer[] getAllowedLevels() {
        return new Integer[] { 1 };
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
        return new ElephantHeartSkill(caster, (SwordSkillProvider) this);
    }

    @Override
    public void setModifiers() {

    }
    
}