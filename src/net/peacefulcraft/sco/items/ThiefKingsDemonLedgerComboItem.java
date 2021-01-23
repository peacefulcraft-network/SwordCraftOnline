package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;
import net.peacefulcraft.sco.swordskills.ThiefKingsDemonLedgerComboSkill;

public class ThiefKingsDemonLedgerComboItem implements SwordSkillProvider {

    private int quantity;
    private ItemTier tier;

    public ThiefKingsDemonLedgerComboItem(ItemTier tier, Integer quantity) {
        this.quantity = quantity;
        this.tier = ItemTier.ETHEREAL;
    }

    @Override
    public String getName() {
        return "Thief Kings Demon Ledger Combo";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(this.tier) + "Thief King's Demon Ledger Combo";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(this.tier) + "The signature 4 hit combo of the thief king.");
        lore.add(ItemTier.getTierColor(this.tier) + "First hit: Inflicts weakness IV");
        lore.add(ItemTier.getTierColor(this.tier) + "Second hit: Inflicts poison IV");
        lore.add(ItemTier.getTierColor(this.tier) + "Third hit: Inflicts wither IV");
        lore.add(ItemTier.getTierColor(this.tier) + "Fourth hit: Damage x3");
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.GOLD_INGOT;
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
        return SwordSkillType.PRIMARY;
    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        return new ThiefKingsDemonLedgerComboSkill(caster, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {

    }
    
}
