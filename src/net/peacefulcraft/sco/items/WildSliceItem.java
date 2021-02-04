package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;
import net.peacefulcraft.sco.swordskills.WildSliceSkill;

public class WildSliceItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;

    public WildSliceItem(ItemTier tier, int quantity) {
        this.tier = ItemTier.RARE;
        this.quantity = quantity;
    }

    @Override
    public String getName() {
        return "Wild Slice";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "Wild Slice";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(tier) + "Strike your foe hapharzardly.");
        lore.add(ItemTier.getTierColor(tier) + "Next hit deals: 0.5x, 2x, or 4x");
        lore.add(ItemTier.getTierColor(tier) + "after trigger.");
        lore.add(ItemTier.getTierColor(tier) + "Cooldown: 32 seconds");
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.BLACK_DYE;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] { ItemTier.RARE };
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
        return new WildSliceSkill(caster, (SwordSkillProvider) this);
    }

    @Override
    public void setModifiers() {

    }
    
}
