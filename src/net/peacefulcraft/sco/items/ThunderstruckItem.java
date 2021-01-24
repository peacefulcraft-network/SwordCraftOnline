package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;
import net.peacefulcraft.sco.swordskills.ThunderstruckSkill;

public class ThunderstruckItem implements SwordSkillProvider {

    private int cooldown;
    private int quantity;
    private ItemTier tier;

    public ThunderstruckItem(ItemTier tier, int quantity) {
        this.quantity = quantity;
        this.tier = tier;
    }

    @Override
    public String getName() {
        return "Thunderstruck";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(this.tier) + "Thunderstruck";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(this.tier) + "Mark your foes to be struck");
        lore.add(ItemTier.getTierColor(this.tier) + "by the ones above.");
        lore.add(ItemTier.getTierColor(this.tier) + "Hit enemies are struck by lightning");
        lore.add(ItemTier.getTierColor(this.tier) + "one second after being hit.");
        switch(this.tier) {
            case RARE:
                lore.add(ItemTier.getTierColor(this.tier) + "Cooldown: 15 seconds");
            break; case LEGENDARY:
                lore.add(ItemTier.getTierColor(this.tier) + "Cooldown: 13 seconds");
            break; case ETHEREAL:
                lore.add(ItemTier.getTierColor(this.tier) + "Cooldown: 11 seconds");
            break; case GODLIKE:
                lore.add(ItemTier.getTierColor(this.tier) + "Cooldown: 9 seconds");
            default:
                lore.add("Severe Error: Skill not functional, Contact Admin for correction.");
        }
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.END_ROD;
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
        return new ThunderstruckSkill(caster, this.cooldown, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {
        switch(this.tier) {
            case RARE:
                this.cooldown = 15000;
            break; case LEGENDARY:
                this.cooldown = 13000;
            break; case ETHEREAL:
                this.cooldown = 11000;
            break; case GODLIKE:
                this.cooldown = 9000;
            default:
                this.cooldown = 50000;
        }
    }
    
}
