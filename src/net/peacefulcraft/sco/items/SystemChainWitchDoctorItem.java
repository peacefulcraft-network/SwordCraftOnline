package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;
import net.peacefulcraft.sco.swordskills.SystemChainWitchDoctorSkill;

public class SystemChainWitchDoctorItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;

    public SystemChainWitchDoctorItem(ItemTier tier, int quantity) {
        this.tier = ItemTier.ETHEREAL;
        this.quantity = quantity;
    }

    @Override
    public String getName() {
        return "System Chain Witch Doctor";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "System Chain: Witch Doctor";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(tier) + "Draw out the power of the");
        lore.add(ItemTier.getTierColor(tier) + "grand witch doctor.");
        lore.add(ItemTier.getTierColor(tier) + "On trigger: deal 20 true");
        lore.add(ItemTier.getTierColor(tier) + "damage to nearby entities.");
        lore.add(ItemTier.getTierColor(tier) + "Heals user for 17 health per");
        lore.add(ItemTier.getTierColor(tier) + "effected entity.");
        lore.add(ItemTier.getTierColor(tier) + "Cooldown: 55 seconds");
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.NETHER_WART;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] { ItemTier.ETHEREAL };
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
    public SwordSkillType getType() {
        return SwordSkillType.SWORD;
    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        return new SystemChainWitchDoctorSkill(caster, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {

    }
    
}
