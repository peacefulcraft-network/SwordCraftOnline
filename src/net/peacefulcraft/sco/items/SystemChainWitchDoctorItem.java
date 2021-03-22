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
import net.peacefulcraft.sco.swordskills.SystemChainWitchDoctorSkill;

public class SystemChainWitchDoctorItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;
    private SwordSkillType type;
    private SwordSkillDesc desc;

    public SystemChainWitchDoctorItem(ItemTier tier, Integer quantity) {
        this.tier = ItemTier.ETHEREAL;
        this.quantity = quantity;
        this.type = SwordSkillType.SWORD;
        this.desc = new SwordSkillDesc(tier, type);
        desc.add("Draw out the power of the");
        desc.add("grand witch doctor.");
        desc.add("On trigger: deal 15 true");
        desc.add("damage to nearby entities.");
        desc.add("Heals user for 12 health per");
        desc.add("effected entity.");
        desc.add("Cooldown: 55 seconds");
    }

    @Override
    public String getName() {
        return "System Chain Witch Doctor";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "System Chain: Witch Doctor";
    }

    @Override
    public ArrayList<String> getLore() {
        return desc.getDesc();
    }

    @Override
    public Material getMaterial() {
        return Material.NETHER_WART;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] { ItemTier.ETHEREAL };
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
        return true;
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
        return new SystemChainWitchDoctorSkill(caster, (SwordSkillProvider)this, tier);
    }

    @Override
    public void setModifiers() {

    }
    
}
