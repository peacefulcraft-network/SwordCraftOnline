package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;
import net.peacefulcraft.sco.swordskills.VolatileContainmentSkill;

public class VolatileContainmentItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;

    public VolatileContainmentItem(ItemTier tier, int quantity) {
        this.tier = ItemTier.RARE;
        this.quantity = quantity;
    }

    @Override
    public String getName() {
        return "Volatile Containment";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "Volatile Containment";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(tier) + "Fill the nearby area");
        lore.add(ItemTier.getTierColor(tier) + "with a sinister aura.");
        lore.add(ItemTier.getTierColor(tier) + "On trigger: For 10 seconds");
        lore.add(ItemTier.getTierColor(tier) + "every entity within");
        lore.add(ItemTier.getTierColor(tier) + "5 blocks is withered.");
        lore.add(ItemTier.getTierColor(tier) + "Cooldown: 27 seconds");
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.WITHER_ROSE;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] { 
            ItemTier.RARE
        };
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
        return SwordSkillType.PRIMARY;
    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        return new VolatileContainmentSkill(caster, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {

    }
    
}
