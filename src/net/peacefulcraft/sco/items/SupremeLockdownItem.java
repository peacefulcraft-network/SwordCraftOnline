package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.chat.hover.content.Item;
import net.peacefulcraft.sco.swordskills.SupremeLockdownSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class SupremeLockdownItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;

    public SupremeLockdownItem(ItemTier tier, int quantity) {
        this.tier = ItemTier.LEGENDARY;
        this.quantity = quantity;
    }

    @Override
    public String getName() {
        return "Supreme Lockdown";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "Supreme Lockdown";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(tier) + "Temporarily halt the");
        lore.add(ItemTier.getTierColor(tier) + "movements of all nearby.");
        lore.add(ItemTier.getTierColor(tier) + "Freezes allies and foes: 7 seconds");
        lore.add(ItemTier.getTierColor(tier) + "Cooldown: 25 seconds");
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.ANVIL;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] { ItemTier.LEGENDARY };
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
        return new SupremeLockdownSkill(caster, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {

    }
    
}
