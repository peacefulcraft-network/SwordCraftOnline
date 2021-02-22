package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.RedDawnSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class RedDawnItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;
    private int levelModifier;

    public RedDawnItem(ItemTier tier, int quantity) {
        this.tier = tier;
        this.quantity = quantity;

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
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(tier) + "The sun rises blood red.");
        lore.add(ItemTier.getTierColor(tier) + "Your heart fills with souls");
        lore.add(ItemTier.getTierColor(tier) + "laid to rest.");
        switch(tier) {
            case RARE:
                lore.add(ItemTier.getTierColor(tier) + "Max Health: x1.2 + 0.1");
                lore.add(ItemTier.getTierColor(tier) + "per player kill.");
            break; case LEGENDARY:
                lore.add(ItemTier.getTierColor(tier) + "Max Health: x1.2 + 0.2");
                lore.add(ItemTier.getTierColor(tier) + "per player kill."); 
            break; case ETHEREAL:
                lore.add(ItemTier.getTierColor(tier) + "Max Health: x1.2 + 0.3");
                lore.add(ItemTier.getTierColor(tier) + "per player kill.");
            break; case GODLIKE:
                lore.add(ItemTier.getTierColor(tier) + "Max Health: x1.2 + 0.4");
                lore.add(ItemTier.getTierColor(tier) + "per player kill.");
            default:
        }
        return lore;
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
        return SwordSkillType.PASSIVE;
    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        return new RedDawnSkill(caster, levelModifier, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {

    }
    
}
