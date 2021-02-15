package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.ResolutionWindSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class ResolutionWindItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;
    private double vectorModifier;

    public ResolutionWindItem(ItemTier tier, int quantity) {
        this.tier = tier;
        this.quantity = quantity;

        setModifiers();
    }

    @Override
    public String getName() {
        return "Resolution Wind";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "Resolution Wind";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(tier) + "Summon a gust of wind");
        lore.add(ItemTier.getTierColor(tier) + "to force back your foes.");
        switch(tier) {
            case RARE:
                lore.add(ItemTier.getTierColor(tier) + "Wind Modifier: 2.0");
            break; case LEGENDARY:
                lore.add(ItemTier.getTierColor(tier) + "Wind Modifier: 2.1");
            break; case ETHEREAL:
                lore.add(ItemTier.getTierColor(tier) + "Wind Modifier: 2.2");
            break; case GODLIKE:
                lore.add(ItemTier.getTierColor(tier) + "Wind Modifier: 2.3");
            default:
        }
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.COBWEB;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] {
            ItemTier.RARE,
            ItemTier.LEGENDARY,
            ItemTier.ETHEREAL,
            ItemTier.GODLIKE
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
        return SwordSkillType.SECONDARY;
    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        return new ResolutionWindSkill(caster, vectorModifier, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {
        switch(tier) {
            case RARE:
                this.vectorModifier = 0.0;
            break; case LEGENDARY:
                this.vectorModifier = 0.1;
            break; case ETHEREAL:
                this.vectorModifier = 0.2;
            break; case GODLIKE:
                this.vectorModifier = 0.3;
            default:
        }
    }
    
}
