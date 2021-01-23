package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.SerpentsBiteSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class SerpentsBiteItem implements SwordSkillProvider {

    private int quantity;
    private ItemTier tier;
    private int poisonModifier;

    public SerpentsBiteItem(ItemTier tier, int quantity) {
        this.tier = tier;
        this.quantity = quantity;
        
        setModifiers();
    }

    @Override
    public String getName() {
        return "Serpents Bite";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(this.tier) + "Serpents Bite";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ItemTier.getTierColor(this.tier) + "Coat your blade with a powerful poison.");
        switch(this.tier) {
            case UNCOMMON:
                lore.add(ItemTier.getTierColor(this.tier) + "Poison level I");
            break; case RARE:
                lore.add(ItemTier.getTierColor(this.tier) + "Poison level II");
            break; case LEGENDARY:
                lore.add(ItemTier.getTierColor(this.tier) + "Poison level III");
            break; case ETHEREAL:
                lore.add(ItemTier.getTierColor(this.tier) + "Poison level IV");
            break; case GODLIKE:
                lore.add(ItemTier.getTierColor(this.tier) + "Poison level V");
            default:
                lore.add("Severe Error: Skill not functional, Contact Admin for correction.");
        } 
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.LIME_DYE;
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
        return new SerpentsBiteSkill(caster, this.poisonModifier, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {
        switch(this.tier) {
            case UNCOMMON:
                this.poisonModifier = 1;
            break; case RARE:
                this.poisonModifier = 2;
            break; case LEGENDARY:
                this.poisonModifier = 3;
            break; case ETHEREAL:
                this.poisonModifier = 4;
            break; case GODLIKE:
                this.poisonModifier = 5;
            default:
                this.poisonModifier = 0;
        }

    }
    
}
