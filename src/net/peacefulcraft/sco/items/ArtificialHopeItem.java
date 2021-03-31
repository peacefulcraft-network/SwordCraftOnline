package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.ArtificialHopeSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class ArtificialHopeItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;
    private double valueModifier;
    private SwordSkillType type;
    private SwordSkillDesc desc;

    public ArtificialHopeItem(ItemTier tier, Integer quantity) {
        this.tier = tier;
        this.quantity = quantity;
        this.type = SwordSkillType.PASSIVE;

        setModifiers();

        desc = new SwordSkillDesc(tier, type);
        desc.add("Fill your heart with hope.");
        switch(this.tier) {
            case COMMON:
                desc.add("True Damage: +1.2");
                desc.add("Movement Speed: +10%");
            break; case UNCOMMON:
                desc.add("True Damage: +1.3");
                desc.add("Movement Speed: +13%");
            break; case RARE:
                desc.add("True Damage: +1.4");
                desc.add("Movement Speed: +14%");
            break; case LEGENDARY:
                desc.add("True Damage: +1.5");
                desc.add("Movement Speed: +15%");
            break; case ETHEREAL:
                desc.add("True Damage: +1.6");
                desc.add("Movement Speed: +16%");
            break; case GODLIKE:
                desc.add("True Damage: +1.7");
                desc.add("Movement Speed: +17%");
        }
        desc.add("Armor: -2");
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
        return desc.getDesc();
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
