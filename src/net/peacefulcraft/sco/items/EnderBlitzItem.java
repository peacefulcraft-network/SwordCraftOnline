package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.EnderBlitzSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class EnderBlitzItem implements SwordSkillProvider {

    private int quantity;
    private ItemTier tier;

    public EnderBlitzItem(ItemTier tier, int quantity) {
        this.quantity = quantity;
        this.tier = ItemTier.RARE;
    }

    @Override
    public String getName() {
        return "Ender Blitz";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(this.tier) + "Ender Blitz";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(this.tier) + "Channel the power of the end.");
        lore.add(ItemTier.getTierColor(this.tier) + "Teleport randomly 5 times.");
        lore.add(ItemTier.getTierColor(this.tier) + "True Damage: x2 for 2 seconds");
        lore.add(ItemTier.getTierColor(this.tier) + "per teleport.");
        lore.add(ItemTier.getTierColor(this.tier) + "Cooldown: 25 seconds");
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.END_CRYSTAL;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] {
            ItemTier.RARE
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
        return new EnderBlitzSkill(caster, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {

    }
    
}
