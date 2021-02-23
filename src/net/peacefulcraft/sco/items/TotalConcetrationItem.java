package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;
import net.peacefulcraft.sco.swordskills.TotalConcentrationSkill;

public class TotalConcetrationItem implements SwordSkillProvider {

    private ItemTier tier;
    private int quantity;
    private int levelModifier;

    public TotalConcetrationItem(ItemTier tier, int quantity) {
        this.tier = tier;
        this.quantity = quantity;

        setModifiers();
    }

    @Override
    public String getName() {
        return "Total Concentration";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(tier) + "Total Concetration";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(tier) + "Calm your breath and");
        lore.add(ItemTier.getTierColor(tier) + "empty your mind.");
        switch(tier) {
            case RARE:
                lore.add(ItemTier.getTierColor(tier) + "Attack Speed: +2");
            break; case LEGENDARY:
                lore.add(ItemTier.getTierColor(tier) + "Attack Speed: +3");
            break; case ETHEREAL:
                lore.add(ItemTier.getTierColor(tier) + "Attack Speed: +4");
            break; case GODLIKE:
                lore.add(ItemTier.getTierColor(tier) + "Attack Speed: +5");
            default:
        }
        lore.add(ItemTier.getTierColor(tier) + "Movement Speed: -3");
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.BLUE_ICE;
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
        return new TotalConcentrationSkill(caster, levelModifier, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {

    }
    
}
