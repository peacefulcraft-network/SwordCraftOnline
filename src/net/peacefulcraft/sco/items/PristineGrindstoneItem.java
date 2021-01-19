package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.PristineGrindstoneSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class PristineGrindstoneItem implements SwordSkillProvider {

    private double increase;
    private ItemTier tier;
    private int quantity;

    public PristineGrindstoneItem(ItemTier tier, Integer level) {
        this.tier = tier;
        this.quantity = 1;

        setModifiers();
    }

    public PristineGrindstoneItem(ItemTier tier, Integer level, int quantity) {
        this.tier = tier;
        this.quantity = quantity;

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
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ItemTier.getTierColor(this.tier) + "A beginners grindstone. Increases attack damage.");
        switch (this.tier) {
            case COMMON:
                lore.add(ItemTier.getTierColor(this.tier) + "Attack Damage: +0.1");
                break;
            case UNCOMMON:
                lore.add(ItemTier.getTierColor(this.tier) + "Attack Damage: +0.2");
                break;
            case RARE:
                lore.add(ItemTier.getTierColor(this.tier) + "Attack Damage: +0.3");
                break;
            case LEGENDARY:
                lore.add(ItemTier.getTierColor(this.tier) + "Attack Damage: +0.4");
                break;
            case ETHEREAL:
                lore.add(ItemTier.getTierColor(this.tier) + "Attack Damage: +0.5");
                break;
            case GODLIKE:
                lore.add(ItemTier.getTierColor(this.tier) + "Attack Damage: +0.6");
        }

        return lore;
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
        return SwordSkillType.PASSIVE;
    }

    @Override
    public Integer[] getAllowedLevels() {
        return new Integer[] {1, 2, 3};
    }

    @Override
    public Integer getLevel() {
        return 1;
    }

    @Override
    public void setLevel(Integer level) {

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
