package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.RedDawnSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class RedDawnItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;
    private int levelModifier;
    private SwordSkillType type;
    private SwordSkillDesc desc;

    public RedDawnItem(ItemTier tier, Integer quantity) {
        this.tier = tier;
        this.quantity = quantity;
        this.type = SwordSkillType.PASSIVE;
        this.desc = new SwordSkillDesc(tier, type);
        desc.add("The sun rises blood red.");
        desc.add("Your heart fills with souls");
        desc.add("laid to rest.");
        switch(tier) {
            case RARE:
                desc.add("Max Health: x1.2 + 0.1");
                desc.add("per player kill.");
            break; case LEGENDARY:
                desc.add("Max Health: x1.2 + 0.2");
                desc.add("per player kill."); 
            break; case ETHEREAL:
                desc.add("Max Health: x1.2 + 0.3");
                desc.add("per player kill.");
            break; case GODLIKE:
                desc.add("Max Health: x1.2 + 0.4");
                desc.add("per player kill.");
            default:
        }

        setModifiers();
    }

    @Override
    public String getName() {
        return "Red Dawn";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "Red Dawn";
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
        return type;
    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        return new RedDawnSkill(caster, levelModifier, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {

    }
    
}
