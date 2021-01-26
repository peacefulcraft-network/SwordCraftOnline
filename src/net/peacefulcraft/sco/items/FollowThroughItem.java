package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.FollowThroughSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class FollowThroughItem implements SwordSkillProvider {

    private int quantity;
    private ItemTier tier;
    private double critModifier;

    public FollowThroughItem(ItemTier tier, int quantity) {
        this.quantity = quantity;
        this.tier = tier;
        
        setModifiers();
    }

    @Override
    public String getName() {
        return "Follow Through";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(this.tier) + "Follow Through";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(this.tier) + "A more complete strike deals");
        lore.add(ItemTier.getTierColor(this.tier) + "more critical damage.");
        switch(this.tier) {
            case COMMON:
                lore.add(ItemTier.getTierColor(this.tier) + "Critical Damage Multiplier: +0.5");
            break; case UNCOMMON:
                lore.add(ItemTier.getTierColor(this.tier) + "Critical Damage Multiplier: +0.6");
            break; case RARE:
                lore.add(ItemTier.getTierColor(this.tier) + "Critical Damage Multiplier: +0.7");
            break; case LEGENDARY:
                lore.add(ItemTier.getTierColor(this.tier) + "Critical Damage Multiplier: +0.8");
            break; case ETHEREAL:
                lore.add(ItemTier.getTierColor(this.tier) + "Critical Damage Multiplier: +0.9");
            break; case GODLIKE:
                lore.add(ItemTier.getTierColor(this.tier) + "Critical Damage Multiplier: +1.0");
        }
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.STICK;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] {
            ItemTier.COMMON,
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
        return new FollowThroughSkill(caster, this.critModifier, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {
        switch(this.tier) {
            case COMMON:
                this.critModifier = 0.0;
            break; case UNCOMMON:
                this.critModifier = 0.1;
            break; case RARE:
                this.critModifier = 0.2;
            break; case LEGENDARY:
                this.critModifier = 0.3;
            break; case ETHEREAL:
                this.critModifier = 0.4;
            break; case GODLIKE:
                this.critModifier = 0.5;
        }
    }
    
}
