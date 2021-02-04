package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.FullCounterSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class FullCounterItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;

    public FullCounterItem(ItemTier tier, int quantity) {
        this.tier = ItemTier.ETHEREAL;
        this.quantity = quantity;
    }

    @Override
    public String getName() {
        return "Full Counter";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "Full Counter";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(tier) + "A powerful skill passed");
        lore.add(ItemTier.getTierColor(tier) + "down from a legendary soldier.");
        lore.add(ItemTier.getTierColor(tier) + "On trigger, next damage taken");
        lore.add(ItemTier.getTierColor(tier) + "from a blade is reflected twice");
        lore.add(ItemTier.getTierColor(tier) + "as strong as true damage.");
        lore.add(ItemTier.getTierColor(tier) + "Cooldown: 45 seconds");
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.EMERALD;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] { ItemTier.ETHEREAL };
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
        return SwordSkillType.SWORD;
    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        return new FullCounterSkill(caster, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {

    }
    
}
