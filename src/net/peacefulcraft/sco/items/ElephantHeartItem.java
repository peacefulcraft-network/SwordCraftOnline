package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.ElephantHeartSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class ElephantHeartItem implements SwordSkillProvider {

    private int quantity;
    private ItemTier tier;
    private SwordSkillType type;
    private SwordSkillDesc desc;

    public ElephantHeartItem(ItemTier tier, Integer quantity) {
        this.quantity = quantity;
        this.tier = ItemTier.RARE;
        this.type = SwordSkillType.PASSIVE;
        
        this.desc = new SwordSkillDesc(tier, type);
        desc.add("Empower yourself with the heart");
        desc.add("of the Aincradian Elephant.");
        desc.add("Max Health: +50%");
        desc.add("Movement Speed: -2");
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
        return desc.getDesc();
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
        return tier;
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
        return type;
    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        return new ElephantHeartSkill(caster, (SwordSkillProvider) this);
    }

    @Override
    public void setModifiers() {

    }
    
}
