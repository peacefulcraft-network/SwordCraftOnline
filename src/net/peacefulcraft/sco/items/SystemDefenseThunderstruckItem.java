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
import net.peacefulcraft.sco.swordskills.SystemDefenseThunderstruckSkill;

public class SystemDefenseThunderstruckItem implements SwordSkillProvider, EphemeralAttributeHolder {

    private ItemTier tier;
    private int quantity;

    public SystemDefenseThunderstruckItem(ItemTier tier, int quantity) {
        this.tier = tier;
        this.quantity = quantity;
    }

    @Override
    public String getName() {
        return "System Defense Thunderstruck";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(this.tier) + "System Defense: Thunderstruck";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(this.tier) + "Call upon the ones above and");
        lore.add(ItemTier.getTierColor(this.tier) + "protect yourself with lightning.");
        if(this.tier.equals(ItemTier.ETHEREAL)) {
            lore.add(ItemTier.getTierColor(this.tier) + "Grants regen II for 5 seconds.");
        } else if(this.tier.equals(ItemTier.GODLIKE)) {
            lore.add(ItemTier.getTierColor(this.tier) + "Grants regen III for 5 seconds.");
        }
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.END_ROD;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] {
            ItemTier.LEGENDARY,
            ItemTier.ETHEREAL,
            ItemTier.GODLIKE
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
        return new SystemDefenseThunderstruckSkill(caster, this.tier, (SwordSkillProvider)this);
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
