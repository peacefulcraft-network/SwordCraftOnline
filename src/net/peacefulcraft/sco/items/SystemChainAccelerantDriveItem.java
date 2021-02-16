package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.items.utilities.Glow;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;
import net.peacefulcraft.sco.swordskills.SystemChainAccelerantDriveSkill;

public class SystemChainAccelerantDriveItem implements SwordSkillProvider, EphemeralAttributeHolder {

    private ItemTier tier;
    private int quantity;

    public SystemChainAccelerantDriveItem(ItemTier tier, int quantity) {
        this.tier = ItemTier.GODLIKE;
        this.quantity = quantity;
    }

    @Override
    public String getName() {
        return "System Chain Accelerant Drive";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "System Chain: Accelerant Drive";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(tier) + "Empower yourself with the");
        lore.add(ItemTier.getTierColor(tier) + "flames around you.");
        lore.add(ItemTier.getTierColor(tier) + "True Damage: x2.0 + 0.1");
        lore.add(ItemTier.getTierColor(tier) + "per nearby fire.");
        lore.add(ItemTier.getTierColor(tier) + "Provides no boost if there");
        lore.add(ItemTier.getTierColor(tier) + "no surrounding flames.");
        lore.add(ItemTier.getTierColor(tier) + "Cooldown: 45 seconds");
        return lore;
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
        return SwordSkillType.SWORD;
    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        return new SystemChainAccelerantDriveSkill(caster, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {

    }
    
}
