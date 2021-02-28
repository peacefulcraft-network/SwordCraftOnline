package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;
import net.peacefulcraft.sco.swordskills.UnfortunateDivisorSkill;

public class UnfortunateDivisorItem implements SwordSkillProvider {

    private int quantity;
    private ItemTier tier;
    private SwordSkillType type;
    private SwordSkillDesc desc;

    public UnfortunateDivisorItem(ItemTier tier, Integer quantity) {
        this.tier = ItemTier.ETHEREAL;
        this.quantity = quantity;
        this.type = SwordSkillType.SWORD;
        this.desc = new SwordSkillDesc(tier, type);
        desc.add("Slice your opponents soul in half.");
        desc.add("Divides victims health by 2.");
        desc.add("Cooldown 30 seconds.");
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
        return desc.getDesc();
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
        return type;
    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        return new UnfortunateDivisorSkill(caster, (SwordSkillProvider) this);
    }

    @Override
    public void setModifiers() {

    }
    
}
