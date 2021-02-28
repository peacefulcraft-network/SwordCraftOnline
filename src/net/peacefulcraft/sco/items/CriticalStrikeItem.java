package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import net.peacefulcraft.sco.swordskills.CriticalStrikeSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class CriticalStrikeItem implements SwordSkillProvider {

	@Override
	public String getName() { return "Critical Strike"; }

	@Override
	public String getDisplayName() {
		return ItemTier.getTierColor(getTier()) + "Critical Strike";
	}

	@Override
	public ArrayList<String> getLore() {
		return desc.getDesc();
	}

	@Override
	public Material getMaterial() { return Material.FLINT; }

	private ItemTier tier;
		@Override
		public ItemTier[] getAllowedTiers() { return new ItemTier[] { ItemTier.COMMON }; }
		
		@Override
		public ItemTier getTier() { return tier; }

	private Integer level;

	private Integer quantity;
		@Override
		public Integer getQuantity() { return quantity; }

		@Override
		public void setQuantity(Integer quantity) { this.quantity = quantity; }

	@Override
	public boolean isDroppable() { return false; }

	@Override
	public boolean isMovable() { return true; }

	private Integer increase;
	private SwordSkillType type;
	private SwordSkillDesc desc;

	public CriticalStrikeItem(ItemTier tier, Integer quantity) {
		this.tier = tier;
		this.quantity = quantity;
		this.type = SwordSkillType.PASSIVE;

		setModifiers();

		this.desc = new SwordSkillDesc(tier, type);
		desc.add("A Beginners critical damage technique.");
		switch (this.tier) {
			case COMMON:
				desc.add("Critical Chance: + 2%");
			break; case UNCOMMON:
				desc.add("Critical Chance: + 3%");
			break; case RARE:
				desc.add("Critical Chance: + 4%");
			break; case LEGENDARY:
				desc.add("Critical Chance: + 5%");
			break; case ETHEREAL:
				desc.add("Critical Chance: + 6%");
			break; case GODLIKE:
				desc.add("Critical Chance: + 7%");
		}
	}

	public void setModifiers() {
        switch (this.tier) {
            case COMMON:
                this.increase = 2;
                break;
            case UNCOMMON:
                this.increase = 3;
                break;
            case RARE:
                this.increase = 4;
                break;
            case LEGENDARY:
                this.increase = 5;
                break;
            case ETHEREAL:
                this.increase = 6;
                break;
            case GODLIKE:
                this.increase = 7;
        }
    }

	@Override
	public SwordSkillType getType() { return type; }

	@Override
	public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
		return new CriticalStrikeSkill(caster, this.increase, 10L, (SwordSkillProvider) this);
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
}
