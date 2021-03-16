package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.items.utilities.Glow;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;
import net.peacefulcraft.sco.swordskills.SystemBreakAccelerantBurstSkill;

public class SystemBreakAccelerantBurstItem implements SwordSkillProvider, EphemeralAttributeHolder {

    private ItemTier tier;
    private int quantity;
    private SwordSkillType type;
    private SwordSkillDesc desc;

    public SystemBreakAccelerantBurstItem(ItemTier tier, Integer quantity) {
        this.tier = ItemTier.GODLIKE;
        this.quantity = quantity;
        this.type = SwordSkillType.PRIMARY;
        this.desc = new SwordSkillDesc(tier, type);
        desc.add("Create explosions from");
        desc.add("the flames around you.");
        desc.add("1 explosion per nearby");
        desc.add("fire.");
        desc.add("Creates no explosions if");
        desc.add("there are no flames.");
        desc.add("Cooldown: 45 seconds");
    }

    @Override
    public String getName() {
        return "System Break Accelerant Burst";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "System Break: Accelerant Burst";
    }

    @Override
    public ArrayList<String> getLore() {
        return desc.getDesc();
    }

    @Override
    public Material getMaterial() {
        return Material.BLAZE_ROD;
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
        return type;
    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        return new SystemBreakAccelerantBurstSkill(caster, (SwordSkillProvider)this, tier);
    }

    @Override
    public void setModifiers() {

    }
    
}
