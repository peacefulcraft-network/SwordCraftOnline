package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.items.utilities.Glow;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;
import net.peacefulcraft.sco.swordskills.SystemChainThunderstruckSkill;

public class SystemChainThunderstruckItem implements SwordSkillProvider, EphemeralAttributeHolder {

    private ItemTier tier;
    private int quantity;
    private int rangeModifier;

    public SystemChainThunderstruckItem(ItemTier tier, int quantity) {
        this.quantity = quantity;
        this.tier = tier;

        setModifiers();
    }

    @Override
    public String getName() {
        return "System Chain Thunderstuck";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(this.tier) + "System Chain: Thunderstruck";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(this.tier) + "Channel multiple lightning strikes");
        lore.add(ItemTier.getTierColor(this.tier) + "from the ones above.");
        switch(this.tier) {
            case LEGENDARY:
                lore.add(ItemTier.getTierColor(this.tier) + "Summon lightning wave: 6 blocks");
            break; case ETHEREAL:
                lore.add(ItemTier.getTierColor(this.tier) + "Summon lightning wave: 7 blocks");
            break; case GODLIKE:
                lore.add(ItemTier.getTierColor(this.tier) + "Summon lightning wave: 8 blocks");
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
        return SwordSkillType.PRIMARY;
    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        return new SystemChainThunderstruckSkill(caster, this.rangeModifier, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {
        switch(this.tier) {
            case LEGENDARY:
                this.rangeModifier = 6;
            break; case ETHEREAL:
                this.rangeModifier = 7;
            break; case GODLIKE:
                this.rangeModifier = 8;
            default:
                this.rangeModifier = 0;
        }

    }

    @Override
    public ItemStack applyEphemeralAttributes(ItemStack item) {
        return Glow.addGlow(item);
    }

    @Override
    public void parseEphemeralAttributes(ItemStack item) {

    }
    
}
