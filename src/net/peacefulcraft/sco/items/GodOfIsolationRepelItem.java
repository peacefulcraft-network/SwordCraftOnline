package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.GodOfIsolationRepelSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class GodOfIsolationRepelItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;
    private SwordSkillType type;
    private SwordSkillDesc desc;

    public GodOfIsolationRepelItem(ItemTier tier, Integer quantity) {
        this.tier = ItemTier.LEGENDARY;
        this.quantity = quantity;
        this.type = SwordSkillType.PRIMARY;
        this.desc = new SwordSkillDesc(tier, type);
        desc.add("Call on the ones above");
        desc.add("to blast your foes away.");
        desc.add("Blindness II for 5 seconds");
        desc.add("on all effected entities.");
        desc.add("Blindess II for 3 seconds to self.");
        desc.add("Cooldown: 20 seconds");
    }

    @Override
    public String getName() {
        return "God Of Isolation Repel";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "God Of Isolation: Repel";
    }

    @Override
    public ArrayList<String> getLore() {
        return desc.getDesc();
    }

    @Override
    public Material getMaterial() {
        return Material.BLACK_CONCRETE_POWDER;
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
        return new GodOfIsolationRepelSkill(caster, (SwordSkillProvider)this, tier);
    }

    @Override
    public void setModifiers() {

    }
    
}
