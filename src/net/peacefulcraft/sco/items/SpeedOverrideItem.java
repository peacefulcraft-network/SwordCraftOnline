package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.SpeedOverrideSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class SpeedOverrideItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;
    private SwordSkillType type;
    private SwordSkillDesc desc;

    public SpeedOverrideItem(ItemTier tier, Integer quantity) {
        this.tier = ItemTier.LEGENDARY;
        this.quantity = quantity;
        this.type = SwordSkillType.PASSIVE;
        this.desc = new SwordSkillDesc(tier, type);
        desc.add("Override your bodys max");
        desc.add("speed abilities.");
        desc.add("Attack Speed: +4");
        desc.add("Movement Speed: +30%");
    }

    @Override
    public String getName() {
        return "Speed Override";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "Speed Override";
    }

    @Override
    public ArrayList<String> getLore() {
        return desc.getDesc();
    }

    @Override
    public Material getMaterial() {
        return Material.GLOWSTONE_DUST;
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
        return new SpeedOverrideSkill(caster, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {

    }
    
}
