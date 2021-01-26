package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.PoorMansThirdStrikeSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class PoorMansThirdStrikeItem implements SwordSkillProvider {

    private int quantity;
    private ItemTier tier;

    public PoorMansThirdStrikeItem(ItemTier tier, int quantity) {
        this.quantity = quantity;
        this.tier = ItemTier.UNCOMMON;
    }

    @Override
    public String getName() {
        return "Poor Mans Third Strike";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(this.tier) + "Poor Mans Third Strike";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(this.tier) + "A swift three hit combo.");
        lore.add(ItemTier.getTierColor(this.tier) + "On the third strike you deal 1.8x damage.");
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.LEATHER;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] { ItemTier.UNCOMMON };
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
        return new PoorMansThirdStrikeSkill(caster, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {

    }
    
}
