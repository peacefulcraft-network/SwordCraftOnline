package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;
import net.peacefulcraft.sco.swordskills.VampireCarvingSkill;

public class VampireCarvingItem implements SwordSkillProvider {

    private int quantity;
    private ItemTier tier;
    private double lifeDrain;

    public VampireCarvingItem(ItemTier tier, Integer level) {
        this(tier, level, 1);
    }

    public VampireCarvingItem(ItemTier tier, Integer level, int quantity) {
        this.quantity = quantity;
        this.tier = tier;

        setModifiers();
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
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(this.tier) + "Drain the health of your foes");
        lore.add(ItemTier.getTierColor(this.tier) + "with each strike.");
        switch (this.tier) {
            case RARE:
                lore.add(tier.getTierColor() + "Life Drain on Hit: 0.1x");
            break; case LEGENDARY:
                lore.add(tier.getTierColor() + "Life Drain on Hit: 0.12x");
            break; case ETHEREAL:
                lore.add(tier.getTierColor() + "Life Drain on Hit: 0.14x");
            break; case GODLIKE:
                lore.add(tier.getTierColor() + "Life Drain on Hit: 0.15x");
            default:
                lore.add("Severe Error: Skill not functional, Contact Admin for correction.");
        }
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.REDSTONE_WIRE;
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
    public Integer[] getAllowedLevels() {
        return new Integer[] { 1, 2, 3};
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
        return new VampireCarvingSkill(caster, lifeDrain, (SwordSkillProvider)this);
    }

    public void setModifiers() {
        switch(this.tier) {
            case RARE:
                this.lifeDrain = 0.1;
            break; case LEGENDARY:
                this.lifeDrain = 0.12;
            break; case ETHEREAL:
                this.lifeDrain = 0.14;
            break; case GODLIKE:
                this.lifeDrain = 0.16;
            default:
                this.lifeDrain = 0;
        }
    }
    
}