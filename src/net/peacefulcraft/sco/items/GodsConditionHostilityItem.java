package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.items.utilities.Glow;
import net.peacefulcraft.sco.swordskills.GodsConditionHostilitySkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class GodsConditionHostilityItem implements SwordSkillProvider, EphemeralAttributeHolder {

    private ItemTier tier;
    private int quantity;

    public GodsConditionHostilityItem(ItemTier tier, int quantity) {
        this.tier = ItemTier.GODLIKE;
        this.quantity = quantity;
    }

    @Override
    public String getName() {
        return "Gods Condition Hostility";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "Gods Condition: Hostility";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(tier) + "Force your presense onto");
        lore.add(ItemTier.getTierColor(tier) + "reality itself. All damaged");
        lore.add(ItemTier.getTierColor(tier) + "foes cannot move unless");
        lore.add(ItemTier.getTierColor(tier) + "holding a sword.");
        lore.add(ItemTier.getTierColor(tier) + "Effect Time: 35 seconds");
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.NETHERITE_SCRAP;
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
    public SwordSkillType getType() {
        return SwordSkillType.PASSIVE;
    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        return new GodsConditionHostilitySkill(caster, (SwordSkillProvider)this);
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
