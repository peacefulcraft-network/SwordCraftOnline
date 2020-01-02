package net.peacefulcraft.sco.items.swordskills;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.items.itemTiers;

public interface SwordSkillItem
{
	public ItemStack create(String tier);
	public static ArrayList<String> addDesc(String tier) {
		ArrayList<String> desc = new ArrayList<String>();
		switch(itemTiers.valueOf(tier.toUpperCase())) {
		case COMMON:
			desc.add(getTierColor(tier) + "Common Sword Skill");
		break;case UNCOMMON:
			desc.add(getTierColor(tier) + "Uncommon Sword Skill");
		break;case RARE:
			desc.add(getTierColor(tier) + "Rare Sword Skill");
		break;case LEGENDARY:
			desc.add(getTierColor(tier) + "Legendary Sword Skill");
		break;case MASTERY:
			desc.add(getTierColor(tier) + "Mastery Sword Skill");
		break;case ETHEREAL:
			desc.add(getTierColor(tier) + "Etheral Sword Skill");
		}
		return desc;
	}
	
	public static ChatColor getTierColor(String tier) {
		switch(itemTiers.valueOf(tier.toUpperCase())) {
		case COMMON:
			return ChatColor.WHITE;
		case UNCOMMON:
			return ChatColor.GREEN;
		case RARE:
			return ChatColor.BLUE;
		case LEGENDARY:
			return ChatColor.LIGHT_PURPLE;
		case MASTERY:
			return ChatColor.AQUA;
		case ETHEREAL:
			return ChatColor.GOLD;
		default:
			return ChatColor.WHITE;
		}
	}
}
