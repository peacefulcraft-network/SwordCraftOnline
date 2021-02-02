package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.items.utilities.Glow;
import net.peacefulcraft.sco.swordskills.EnderBlitzControlSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class EnderBlitzControlItem implements SwordSkillProvider, EphemeralAttributeHolder {

    private int quantity;
    private ItemTier tier;

    public EnderBlitzControlItem(ItemTier tier, int quantity) {
        this.quantity = quantity;
        this.tier = ItemTier.ETHEREAL;
    }

    @Override
    public String getName() {
        return "Ender Blitz Control";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "Ender Blitz: Control";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(tier) + "Channel and control the");
        lore.add(ItemTier.getTierColor(tier) + "power of the end.");
        lore.add(ItemTier.getTierColor(tier) + "Teleport with direction control");
        lore.add(ItemTier.getTierColor(tier) + "5 times.");
        lore.add(ItemTier.getTierColor(this.tier) + "True Damage: x2 for 2 seconds");
        lore.add(ItemTier.getTierColor(this.tier) + "per teleport.");
        lore.add(ItemTier.getTierColor(this.tier) + "Cooldown: 32 seconds");
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.END_CRYSTAL;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] {
            ItemTier.ETHEREAL
        };
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
        return SwordSkillType.SECONDARY;
    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        return new EnderBlitzControlSkill(caster, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {

    }

    @Override
    public ItemStack applyEphemeralAttributes(ItemStack item) {
        return Glow.addGlow(item);
    }

    @Override
    public void parseEphemeralAttributes(ItemStack item) {

    }
    
}
