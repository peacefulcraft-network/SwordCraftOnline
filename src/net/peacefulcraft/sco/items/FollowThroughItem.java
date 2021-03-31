package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.FollowThroughSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class FollowThroughItem implements SwordSkillProvider {

    private int quantity;
    private ItemTier tier;
    private double critModifier;
    private SwordSkillType type;
    private SwordSkillDesc desc;

    public FollowThroughItem(ItemTier tier, Integer quantity) {
        this.quantity = quantity;
        this.tier = tier;
        this.type = SwordSkillType.PASSIVE;
        this.desc = new SwordSkillDesc(tier, type);
        desc.add("A more complete strike deals");
        desc.add("more critical damage.");
        switch(this.tier) {
            case COMMON:
                desc.add("Critical Damage Multiplier: +0.5");
            break; case UNCOMMON:
                desc.add("Critical Damage Multiplier: +0.6");
            break; case RARE:
                desc.add("Critical Damage Multiplier: +0.7");
            break; case LEGENDARY:
                desc.add("Critical Damage Multiplier: +0.8");
            break; case ETHEREAL:
                desc.add("Critical Damage Multiplier: +0.9");
            break; case GODLIKE:
                desc.add("Critical Damage Multiplier: +1.0");
        }

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
        return desc.getDesc();
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
        return true;
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
        return type;
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
