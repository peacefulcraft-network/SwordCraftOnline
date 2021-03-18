package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import net.peacefulcraft.sco.swordskills.CriticalStrikeSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillCooldownProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class CriticalStrikeItem implements SwordSkillProvider, SwordSkillCooldownProvider {

	@Override
	public String getName() { return "Critical Strike"; }

	@Override
	public String getDisplayName() {
		return ItemTier.getTierColor(getTier()) + "Critical Strike";
	}

	@Override
	public ArrayList<String> getLore() {
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(tier.getTierColor() + "A Beginners 3 hit combo.");
		switch (this.tier) {
			case COMMON:
				lore.add(tier.getTierColor() + "Combo Damage: 3");
				lore.add(tier.getTierColor() + "Cooldown: 5 seconds");
				break;
			case UNCOMMON:
				lore.add(tier.getTierColor() + "Combo Damage: 4");
				lore.add(tier.getTierColor() + "Cooldown: 5 seconds");
				break;
			case RARE:
				lore.add(tier.getTierColor() + "Combo Damage: 5");
				lore.add(tier.getTierColor() + "Cooldown: 5 seconds");
				break;
			case LEGENDARY:
				lore.add(tier.getTierColor() + "Combo Damage: 7");
				lore.add(tier.getTierColor() + "Cooldown: 5 seconds");
				break;
			case ETHEREAL:
				lore.add(tier.getTierColor() + "Combo Damage: 10");
				lore.add(tier.getTierColor() + "Cooldown: 4 seconds");
				break;
			case GODLIKE:
				lore.add(tier.getTierColor() + "Combo Damage: 12");
				lore.add(tier.getTierColor() + "Cooldown: 5 seconds");
		}
		return lore;
	}

	@Override
	public Material getMaterial() { return Material.FLINT; }

	private ItemTier tier;
		@Override
		public ItemTier[] getAllowedTiers() { return new ItemTier[] { ItemTier.COMMON }; }
		
		@Override
		public ItemTier getTier() { return tier; }

	private Integer level;
		@Override
		public Integer[] getAllowedLevels() { return new Integer[] { 1 }; }
		
		@Override
		public Integer getLevel() { return level; }
		
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

	private Integer quantity;
		@Override
		public Integer getQuantity() { return quantity; }

		@Override
		public void setQuantity(Integer quantity) { this.quantity = quantity; }

	@Override
	public boolean isDroppable() { return false; }

	@Override
	public boolean isMovable() { return true; }

	public CriticalStrikeItem(ItemTier tier, Integer quantity) {
		this.tier = tier;
		this.quantity = quantity;
	}

	@Override
	public SwordSkillType getType() { return SwordSkillType.PASSIVE; }

	@Override
	public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
		switch (this.tier) {
			case UNCOMMON:
				return new CriticalStrikeSkill(caster, 5000, this, 3, 4);
			case RARE:
				return new CriticalStrikeSkill(caster, 5000, this, 3, 5);
			case LEGENDARY:
				return new CriticalStrikeSkill(caster, 5000, this, 3, 7);
			case ETHEREAL:
				return new CriticalStrikeSkill(caster, 5000, this, 3, 10);
			case GODLIKE:
				return new CriticalStrikeSkill(caster, 5000, this, 3, 12);
			default:
				return new CriticalStrikeSkill(caster, 5000, this, 3, 3);
		}
	}

	@Override
	public JsonObject getCustomData() {
		JsonObject json = new JsonObject();
		json.addProperty("level", this.level);
		json.addProperty("ss_cooldown", this.cooldownEnd);
		return json;
	}

	@Override
	public void setCustomData(JsonObject data) {
		this.level = data.get("level").getAsInt();
		this.cooldownEnd = data.get("ss_cooldown").getAsLong();
	}

	@Override
	public void parseCustomItemData(ItemStack item) {
		NBTItem nbti = new NBTItem(item);
		this.level = nbti.getInteger("level");
		this.cooldownEnd = nbti.getLong("ss_cooldown");
	}

	@Override
	public ItemStack applyCustomItemData(ItemStack item, JsonObject data) {
		NBTItem nbti = new NBTItem(item);
		nbti.setInteger("level", data.get("level").getAsInt());
		nbti.setLong("ss_cooldown", data.get("ss_cooldown").getAsLong());
		return nbti.getItem();
	}
}
