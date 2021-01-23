package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import net.peacefulcraft.sco.swordskills.ForwardLungeSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class ForwardLungeItem implements SwordSkillProvider {

    @Override
    public String getName() { return "Forward Lunge"; }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(getTier()) + "Forward Lunge";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<String>();
        switch (this.tier) {
            case COMMON:
                lore.add(ItemTier.getTierColor(this.tier) + "Damage Increased 20% for 2 seconds.");
                break;
            case UNCOMMON:
                lore.add(ItemTier.getTierColor(this.tier) + "Damage Increased 30% for 2 seconds.");
                break;
            case RARE:
                lore.add(ItemTier.getTierColor(this.tier) + "Damage Increased 40% for 2 seconds.");
                break;
            case LEGENDARY:
                lore.add(ItemTier.getTierColor(this.tier) + "Damage Increased 50% for 2 seconds.");
                break;
            case ETHEREAL:
                lore.add(ItemTier.getTierColor(this.tier) + "Damage Increased 60% for 2 seconds.");
                break;
            case GODLIKE:
                lore.add(ItemTier.getTierColor(this.tier) + "Damage Increased 70% for 2 seconds.");
        }

        return lore;
    }

    @Override
    public Material getMaterial() { return Material.BIRCH_DOOR; }

    private ItemTier tier;
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
        public ItemTier getTier() { return this.tier; }

    private Integer quantity;
        @Override
        public Integer getQuantity() { return this.quantity;}

        @Override
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

    @Override
    public boolean isDroppable() { return false; }

    @Override
    public boolean isMovable() { return true; }

    private Double increase;

    public ForwardLungeItem(ItemTier tier, Integer level) {
        this.tier = tier;
        this.level = level;
        this.quantity = 1;
        this.setModifiers();
    }

    public ForwardLungeItem(ItemTier tier, Integer level, Integer quanity) {
        this.tier = tier;
        this.level = level;
        this.quantity = quanity;
        this.setModifiers();
    }

    public void setModifiers() {
        switch (this.tier) {
            case COMMON:
                this.increase = 1.2;
                break;
            case UNCOMMON:
                this.increase = 1.3;
                break;
            case RARE:
                this.increase = 1.4;
                break;
            case LEGENDARY:
                this.increase = 1.5;
                break;
            case ETHEREAL:
                this.increase = 1.6;
                break;
            case GODLIKE:
                this.increase = 1.7;
        }
    }

    @Override
    public JsonObject getCustomData() {
        JsonObject json = new JsonObject();
        json.addProperty("level", this.level);
        return json;
    }

    @Override
    public void setCustomData(JsonObject data) {
        this.level = data.get("level").getAsInt();
    }

    @Override
    public void parseCustomItemData(ItemStack item) {
        NBTItem nbti = new NBTItem(item);
        this.level = nbti.getInteger("level");
    }

    @Override
    public ItemStack applyCustomItemData(ItemStack item, JsonObject data) {
        NBTItem nbti = new NBTItem(item);
        nbti.setInteger("level", data.get("level").getAsInt());
        return nbti.getItem();
    }

    @Override
    public SwordSkillType getType() { return SwordSkillType.SECONDARY; }

    private Integer level;

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        switch (this.tier) {
            case UNCOMMON:
                return new ForwardLungeSkill(caster, this.increase, 15000L, (SwordSkillProvider) this);
            case RARE:
                return new ForwardLungeSkill(caster, this.increase, 15000L, (SwordSkillProvider) this);
            case LEGENDARY:
                return new ForwardLungeSkill(caster, this.increase, 15000L, (SwordSkillProvider) this);
            case ETHEREAL:
                return new ForwardLungeSkill(caster, this.increase, 15000L, (SwordSkillProvider) this);
            case GODLIKE:
                return new ForwardLungeSkill(caster, this.increase, 15000L, (SwordSkillProvider) this);
            default:
                return new ForwardLungeSkill(caster, this.increase, 15000L, (SwordSkillProvider) this);
        }
    }
    
}