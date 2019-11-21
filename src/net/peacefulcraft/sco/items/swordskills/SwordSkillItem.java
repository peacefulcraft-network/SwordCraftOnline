package net.peacefulcraft.sco.items.swordskills;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.items.itemTiers;

public interface SwordSkillItem
{
	public ItemStack create(String tier);
	public static ArrayList<String> addDesc(String tier) {
		ArrayList<String> desc = new ArrayList<String>();
		switch(itemTiers.valueOf(tier.toUpperCase())) {
		case COMMON:
			desc.add("Common");
		break;case UNCOMMON:
			desc.add("Uncommon");
		break;case RARE:
			desc.add("Rare");
		break;case LEGENDARY:
			desc.add("Legendary");
		break;case MASTERY:
			desc.add("Mastery");
		break;case ETHEREAL:
			desc.add("Etheral");
		}
		return desc;
	}
}
