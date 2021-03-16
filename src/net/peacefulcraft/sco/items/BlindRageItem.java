package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.BlindRageSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class BlindRageItem implements SwordSkillProvider {

    private int quantity;
    private ItemTier tier;
    private SwordSkillType type;
    private SwordSkillDesc desc;

    public BlindRageItem(ItemTier tier, Integer quantity) {
        this.quantity = quantity;
        this.tier = ItemTier.RARE;
        this.type = SwordSkillType.SECONDARY;

        desc = new SwordSkillDesc(tier, type);
        desc.add("Fill yourself with rage and");
        desc.add("strike down your foes...");
        desc.add("May inflict blindness.");
        desc.add("Gives: Strength II, Blindness I");
        desc.add("Effect time: 15 seconds");
        desc.add("Cooldown time: 25 seconds");
    }

    @Override
    public String getName() {
        return "Blind Rage";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(this.tier) + "Blind Rage";
    }

    @Override
    public ArrayList<String> getLore() {
        return desc.getDesc();
    }

    @Override
    public Material getMaterial() {
        return Material.REDSTONE_BLOCK;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] { ItemTier.RARE };
    }

    @Override
    public ItemTier getTier() {
        return this.tier;
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
        return new BlindRageSkill(caster, (SwordSkillProvider)this, tier);
    }

    @Override
    public void setModifiers() {

    }
    
}
