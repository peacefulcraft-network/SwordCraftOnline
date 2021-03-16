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
import net.peacefulcraft.sco.swordskills.VolatileContainmentSkill;

public class VolatileContainmentItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;
    private SwordSkillType type;
    private SwordSkillDesc desc;

    public VolatileContainmentItem(ItemTier tier, Integer quantity) {
        this.tier = ItemTier.RARE;
        this.quantity = quantity;
        this.type = SwordSkillType.PRIMARY;
        this.desc = new SwordSkillDesc(tier, type);
        desc.add("Fill the nearby area");
        desc.add("with a sinister aura.");
        desc.add("On trigger: For 10 seconds");
        desc.add("every entity within");
        desc.add("5 blocks is withered.");
        desc.add("Cooldown: 27 seconds");
    }

    @Override
    public String getName() {
        return "Volatile Containment";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "Volatile Containment";
    }

    @Override
    public ArrayList<String> getLore() {
        return desc.getDesc();
    }

    @Override
    public Material getMaterial() {
        return Material.WITHER_ROSE;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] { 
            ItemTier.RARE
        };
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
        return type;
    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        return new VolatileContainmentSkill(caster, (SwordSkillProvider)this, tier);
    }

    @Override
    public void setModifiers() {

    }
    
}
