package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;
import net.peacefulcraft.sco.swordskills.WitheringNecrolysisSkill;

public class WitheringNecrolysisItem implements SwordSkillProvider {

    private int quantity;
    private ItemTier tier;
    private int witherModifier;

    public WitheringNecrolysisItem(ItemTier tier, Integer quantity) {
        this.quantity = quantity;
        this.tier = tier;

        setModifiers();
    }

    @Override
    public String getName() {
        return "Withering Necrolysis";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(this.tier) + "Withering Necrolysis";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ItemTier.getTierColor(this.tier) + "Infuse your blade with the");
        lore.add(ItemTier.getTierColor(this.tier) + "curse of death.");
        switch(this.tier) {
            case UNCOMMON:
                lore.add(ItemTier.getTierColor(this.tier) + "Wither level I");
            break; case RARE:
                lore.add(ItemTier.getTierColor(this.tier) + "Wither level II");
            break; case LEGENDARY:
                lore.add(ItemTier.getTierColor(this.tier) + "Wither level III");
            break; case ETHEREAL:
                lore.add(ItemTier.getTierColor(this.tier) + "Wither level IV");
            break; case GODLIKE:
                lore.add(ItemTier.getTierColor(this.tier) + "Wither level V");
            default:
                lore.add("Severe Error: Skill not functional, Contact Admin for correction.");
        } 
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.WITHER_ROSE;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] {
            ItemTier.UNCOMMON,
            ItemTier.RARE,
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
        return SwordSkillType.PASSIVE;
    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        return new WitheringNecrolysisSkill(caster, this.witherModifier, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {
        switch(this.tier) {
            case UNCOMMON:
                this.witherModifier = 1;
            break; case RARE:
                this.witherModifier = 2;
            break; case LEGENDARY:
                this.witherModifier = 3;
            break; case ETHEREAL:
                this.witherModifier = 4;
            break; case GODLIKE:
                this.witherModifier = 5;
            default:
                this.witherModifier = 0;
        }

    }
    
}
