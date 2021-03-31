package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;
import net.peacefulcraft.sco.swordskills.WeakenedPulseSkill;

public class WeakenedPulseItem implements SwordSkillProvider {

    private int quantity;
    private ItemTier tier;
    private SwordSkillType type;
    private SwordSkillDesc desc;
    private int weakModifier;

    public WeakenedPulseItem(ItemTier tier, Integer quantity) {
        this.tier = tier;
        this.quantity = quantity;
        this.type = SwordSkillType.PASSIVE;
        this.desc = new SwordSkillDesc(tier, type);
        desc.add("Inflict weakness on your enemies.");
        switch(this.tier) {
            case UNCOMMON:
                desc.add("Weakness level I");
            break; case RARE:
                desc.add("Weakness level II");
            break; case LEGENDARY:
                desc.add("Weakness level III");
            break; case ETHEREAL:
                desc.add("Weakness level IV");
            break; case GODLIKE:
                desc.add("Weakness level V");
            default:
        } 
    }

    @Override
    public String getName() {
        return "Weakened Pulse";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(this.tier) + "Weakened Pulse";
    }

    @Override
    public ArrayList<String> getLore() {
        return desc.getDesc();
    }

    @Override
    public Material getMaterial() {
        return Material.LIGHT_BLUE_DYE;
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
        return new WeakenedPulseSkill(caster, this.weakModifier, (SwordSkillProvider)this, tier);
    }

    @Override
    public void setModifiers() {
        switch(this.tier) {
            case UNCOMMON:
                this.weakModifier = 1;
            break; case RARE:
                this.weakModifier = 2;
            break; case LEGENDARY:
                this.weakModifier = 3;
            break; case ETHEREAL:
                this.weakModifier = 4;
            break; case GODLIKE:
                this.weakModifier = 5;
            default:
                this.weakModifier = 0;
        }

    }
    
}
