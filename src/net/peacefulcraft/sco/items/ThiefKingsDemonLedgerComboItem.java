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
import net.peacefulcraft.sco.swordskills.ThiefKingsDemonLedgerComboSkill;

public class ThiefKingsDemonLedgerComboItem implements SwordSkillProvider {

    private int quantity;
    private ItemTier tier;
    private SwordSkillType type;
    private SwordSkillDesc desc;

    public ThiefKingsDemonLedgerComboItem(ItemTier tier, Integer quantity) {
        this.quantity = quantity;
        this.tier = ItemTier.ETHEREAL;
        this.type = SwordSkillType.PRIMARY;
        this.desc = new SwordSkillDesc(tier, type);
        desc.add("The signature 4 hit combo of the thief king.");
        desc.add("First hit: Inflicts weakness IV");
        desc.add("Second hit: Inflicts poison IV");
        desc.add("Third hit: Inflicts wither IV");
        desc.add("Fourth hit: Damage x3");
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
        return desc.getDesc();
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
        return type;
    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        return new ThiefKingsDemonLedgerComboSkill(caster, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {

    }
    
}
