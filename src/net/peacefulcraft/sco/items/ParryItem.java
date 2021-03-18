package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import net.peacefulcraft.sco.swordskills.ParrySkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillCooldownProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class ParryItem implements SwordSkillProvider, SwordSkillCooldownProvider {

    @Override
    public String getName() { return "Parry"; }

    @Override
    public String getDisplayName() {
        return ItemTier.getTierColor(getTier()) + "Parry";
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ItemTier.getTierColor(this.tier) + "A beginners parry technique.");
        switch (this.tier) {
            case COMMON:
                lore.add(ItemTier.getTierColor(this.tier) + "Parry Chance: +5%");
                break;
            case UNCOMMON:
                lore.add(ItemTier.getTierColor(this.tier) + "Parry Chance: +7%");
                break;
            case RARE:
                lore.add(ItemTier.getTierColor(this.tier) + "Parry Chance: +9%");
                break;
            case LEGENDARY:
                lore.add(ItemTier.getTierColor(this.tier) + "Parry Chance: +11%");
                break;
            case ETHEREAL:
                lore.add(ItemTier.getTierColor(this.tier) + "Parry Chance: +13%");
                break;
            case GODLIKE:
                lore.add(ItemTier.getTierColor(this.tier) + "Parry Chance: +15%");
        }

        return lore;
    }

    @Override
    public Material getMaterial() { return Material.IRON_INGOT; }

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
        public Integer getQuantity() { return this.quantity; }

        @Override
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

    @Override
    public boolean isDroppable() { return false; }

    @Override
    public boolean isMovable() { return true; }

    private Integer level;
    private int increase;

    public ParryItem(ItemTier tier, Integer level) {
        this.tier = tier;
        this.level = level;
        this.quantity = 1;
    }

    public ParryItem(ItemTier tier, Integer level, Integer quantity) {
        this.tier = tier;
        this.level = level;
        this.quantity = quantity;
    }

    public void setModifiers() {
        switch (this.tier) {
            case COMMON:
                this.increase = 5;
                break;
            case UNCOMMON:
                this.increase = 7;
                break;
            case RARE:
                this.increase = 9;
                break;
            case LEGENDARY:
                this.increase = 11;
                break;
            case ETHEREAL:
                this.increase = 13;
                break;
            case GODLIKE:
                this.increase = 15;
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
    public SwordSkillType getType() { return SwordSkillType.PASSIVE; }

    @Override
    public Integer[] getAllowedLevels() { return new Integer[]{ 1, 2, 3}; }

    @Override
    public Integer getLevel() { return this.level; }

    @Override
    public void setLevel(Integer level) { this.level = level; }

	private Long cooldownEnd;
		@Override
		public Boolean isOnCooldown() {
			return this.cooldownEnd > System.currentTimeMillis();
		}

		@Override
		public Long getCooldownEnd() {
			return this.cooldownEnd;
		}

		@Override
		public void markCooldownEnd(Long cooldownEnd) {
			this.cooldownEnd = cooldownEnd;
		}

    @Override
    public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
        switch (this.tier) {
            case UNCOMMON:
                return new ParrySkill(caster, this.increase, 10L, this);
            case RARE:
                return new ParrySkill(caster, this.increase, 10L, this);
            case LEGENDARY:
                return new ParrySkill(caster, this.increase, 10L, this);
            case ETHEREAL:
                return new ParrySkill(caster, this.increase, 10L, this);
            case GODLIKE:
                return new ParrySkill(caster, this.increase, 10L, this);
            default:
                return new ParrySkill(caster, this.increase, 10L, this);
        }
    }
}