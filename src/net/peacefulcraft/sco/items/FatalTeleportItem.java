package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.FatalTeleportSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class FatalTeleportItem implements SwordSkillProvider {

    private int quantity;
    private ItemTier tier;
    private double damageModifier;
    private int cooldown;
    private SwordSkillType type;
    private SwordSkillDesc desc;

    public FatalTeleportItem(ItemTier tier, Integer quantity) {
        this.quantity = quantity;
        this.tier = tier;
        this.type = SwordSkillType.SWORD;

        setModifiers();

        this.desc = new SwordSkillDesc(tier, type);
        desc.add("Appear behind your last hit");
        desc.add("enemy. On trigger, teleport");
        desc.add("directly behind and strike");
        desc.add("them down.");
        switch(this.tier) {
            case RARE:
                desc.add("Damage: x2.0 after teleport.");
                desc.add("Cooldown: 20 seconds.");
            break; case LEGENDARY:
                desc.add("Damage: x2.2 after teleport.");
                desc.add("Cooldown: 22 seconds.");
            break; case ETHEREAL:
                desc.add("Damage: x2.4 after teleport.");
                desc.add("Cooldown: 24 seconds.");
            break; case GODLIKE:
                desc.add("Damage: x2.6 after teleport.");
                desc.add("Cooldown: 26 seconds.");
            default:
        }
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
        return desc.getDesc();
    }

    @Override
    public Material getMaterial() {
        return Material.END_STONE;
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
        return new FatalTeleportSkill(caster, this.damageModifier, this.cooldown, (SwordSkillProvider)this, tier);
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
