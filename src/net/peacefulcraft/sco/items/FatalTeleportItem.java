package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.FatalTeleportSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class FatalTeleportItem implements SwordSkillProvider {

    private int quantity;
    private ItemTier tier;
    private double damageModifier;
    private int cooldown;

    public FatalTeleportItem(ItemTier tier, int quantity) {
        this.quantity = quantity;
        this.tier = tier;

        setModifiers();
    }

    @Override
    public String getName() {
        return "Fatal Teleport";
    }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(this.tier) + "Fatal Teleport";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ItemTier.getTierColor(this.tier) + "Teleport behind your last");
        lore.add(ItemTier.getTierColor(this.tier) + "hit enemy and strike them down.");
        switch(this.tier) {
            case RARE:
                lore.add(ItemTier.getTierColor(this.tier) + "Damage: x2.0 after teleport.");
                lore.add(ItemTier.getTierColor(this.tier) + "Cooldown: 20 seconds.");
            break; case LEGENDARY:
                lore.add(ItemTier.getTierColor(this.tier) + "Damage: x2.2 after teleport.");
                lore.add(ItemTier.getTierColor(this.tier) + "Cooldown: 22 seconds.");
            break; case ETHEREAL:
                lore.add(ItemTier.getTierColor(this.tier) + "Damage: x2.4 after teleport.");
                lore.add(ItemTier.getTierColor(this.tier) + "Cooldown: 24 seconds.");
            break; case GODLIKE:
                lore.add(ItemTier.getTierColor(this.tier) + "Damage: x2.6 after teleport.");
                lore.add(ItemTier.getTierColor(this.tier) + "Cooldown: 26 seconds.");
            default:
                lore.add("Severe Error: Skill not functional, Contact Admin for correction.");
        }
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.END_GATEWAY;
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
        return SwordSkillType.SWORD;
    }

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        return new FatalTeleportSkill(caster, this.damageModifier, this.cooldown, (SwordSkillProvider)this);
    }

    @Override
    public void setModifiers() {
        switch(this.tier) {
            case RARE:
                this.damageModifier = 0.0;
                this.cooldown = 20000;
            break; case LEGENDARY:
                this.damageModifier = 0.2;
                this.cooldown = 22000;
            break; case ETHEREAL:
                this.damageModifier = 0.4;
                this.cooldown = 24000;
            break; case GODLIKE:
                this.damageModifier = 0.6;
                this.cooldown = 26000;
            default:
                this.damageModifier = 0.0;
                this.cooldown = 60000;
        }
    }
    
}
