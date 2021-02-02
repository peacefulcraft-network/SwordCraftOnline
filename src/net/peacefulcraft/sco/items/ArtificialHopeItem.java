package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.ArtificialHopeSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class ArtificialHopeItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;
    private double valueModifier;

    public ArtificialHopeItem(ItemTier tier, int quantity) {
        this.tier = tier;
        this.quantity = quantity;

        setModifiers();
    }

    @Override
    public String getName() {
        return "Artificial Hope";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(this.tier) + "Artifical Hope";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(this.tier) + "Fill your heart with hope.");
        switch(this.tier) {
            case COMMON:
                lore.add(ItemTier.getTierColor(this.tier) + "True Damage: +1.2");
                lore.add(ItemTier.getTierColor(this.tier) + "Movement Speed: +1.2");
            break; case UNCOMMON:
                lore.add(ItemTier.getTierColor(this.tier) + "True Damage: +1.3");
                lore.add(ItemTier.getTierColor(this.tier) + "Movement Speed: +1.3");
            break; case RARE:
                lore.add(ItemTier.getTierColor(this.tier) + "True Damage: +1.4");
                lore.add(ItemTier.getTierColor(this.tier) + "Movement Speed: +1.4");
            break; case LEGENDARY:
                lore.add(ItemTier.getTierColor(this.tier) + "True Damage: +1.5");
                lore.add(ItemTier.getTierColor(this.tier) + "Movement Speed: +1.5");
            break; case ETHEREAL:
                lore.add(ItemTier.getTierColor(this.tier) + "True Damage: +1.6");
                lore.add(ItemTier.getTierColor(this.tier) + "Movement Speed: +1.6");
            break; case GODLIKE:
                lore.add(ItemTier.getTierColor(this.tier) + "True Damage: +1.7");
                lore.add(ItemTier.getTierColor(this.tier) + "Movement Speed: +1.7");
        }
        lore.add(ItemTier.getTierColor(this.tier) + "Armor: -2");
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.WHITE_STAINED_GLASS;
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
        return new ArtificialHopeSkill(caster, this.valueModifier, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {
        switch(this.tier) {
            case COMMON:
                this.valueModifier = 0.0;
            break; case UNCOMMON:
                this.valueModifier = 0.1;
            break; case RARE:
                this.valueModifier = 0.2;
            break; case LEGENDARY:
                this.valueModifier = 0.3;
            break; case ETHEREAL:
                this.valueModifier = 0.4;
            break; case GODLIKE:
                this.valueModifier = 0.5;
        }
    }
    
}
