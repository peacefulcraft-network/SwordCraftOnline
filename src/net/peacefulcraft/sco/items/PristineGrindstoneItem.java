package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.PristineGrindstoneSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class PristineGrindstoneItem implements SwordSkillProvider {

    private double increase;
    private ItemTier tier;
    private int quantity;
    private SwordSkillDesc desc;
    private SwordSkillType type;

    public PristineGrindstoneItem(ItemTier tier, Integer quantity) {
        this.tier = tier;
        this.quantity = quantity;
        this.type = SwordSkillType.PASSIVE;
        this.desc = new SwordSkillDesc(tier, type);
        desc.add("A beginners grindstone. Increases attack damage.");
        switch (this.tier) {
            case COMMON:
                desc.add("Attack Damage: +0.1");
            break; case UNCOMMON:
                desc.add("Attack Damage: +0.2");
            break; case RARE:
                desc.add("Attack Damage: +0.3");
            break; case LEGENDARY:
                desc.add("Attack Damage: +0.4");
            break; case ETHEREAL:
                desc.add("Attack Damage: +0.5");
            break; case GODLIKE:
                desc.add("Attack Damage: +0.6");
        }

        setModifiers();
    }

    @Override
    public String getName() {
        return "Pristine Grindstone";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(getTier()) + "Pristine Grindstone";
    }

    @Override
    public ArrayList<String> getLore() {
        return desc.getDesc();
    }

    @Override
    public Material getMaterial() {
        return Material.QUARTZ;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[]{
            ItemTier.COMMON,
            ItemTier.ETHEREAL,
            ItemTier.GODLIKE,
            ItemTier.LEGENDARY,
            ItemTier.RARE,
            ItemTier. UNCOMMON
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
        return type;
    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        return new PristineGrindstoneSkill(caster, this.increase, (SwordSkillProvider) this);
    }

    public void setModifiers() {
        switch (this.tier) {
            case COMMON:
                this.increase = 0.1;
                break;
            case UNCOMMON:
                this.increase = 0.2;
                break;
            case RARE:
                this.increase = 0.3;
                break;
            case LEGENDARY:
                this.increase = 0.4;
                break;
            case ETHEREAL:
                this.increase = 0.5;
                break;
            case GODLIKE:
                this.increase = 0.6;
        }
    }
    
}
