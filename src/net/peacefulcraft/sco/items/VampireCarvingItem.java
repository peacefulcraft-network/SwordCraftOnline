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
import net.peacefulcraft.sco.swordskills.VampireCarvingSkill;

public class VampireCarvingItem implements SwordSkillProvider {

    private int quantity;
    private ItemTier tier;
    private SwordSkillType type;
    private SwordSkillDesc desc;
    private double lifeDrain;

    public VampireCarvingItem(ItemTier tier, Integer quantity) {
        this.tier = tier;
        this.quantity = quantity;
        this.type = SwordSkillType.PASSIVE;
        this.desc = new SwordSkillDesc(tier, type);

        setModifiers();

        desc.add("Drain the health of your foes");
        desc.add("with each strike.");
        desc.add("Life Drain on Hit: " + lifeDrain + "x");
    }

    @Override
    public String getName() {
        return "Vampire Carving";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(this.tier) + "Vampire Carving";
    }

    @Override
    public ArrayList<String> getLore() {
        return desc.getDesc();
    }

    @Override
    public Material getMaterial() {
        return Material.REDSTONE;
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
        return new VampireCarvingSkill(caster, lifeDrain, (SwordSkillProvider)this, tier);
    }

    public void setModifiers() {
        switch(this.tier) {
            case RARE:
                this.lifeDrain = 0.1;
            break; case LEGENDARY:
                this.lifeDrain = 0.15;
            break; case ETHEREAL:
                this.lifeDrain = 0.20;
            break; case GODLIKE:
                this.lifeDrain = 0.25;
            default:
                this.lifeDrain = 0;
        }
    }
    
}
