package net.peacefulcraft.sco.items;

import net.md_5.bungee.api.ChatColor;

public enum ItemTier
{
	COMMON, UNCOMMON, RARE, LEGENDARY,
	ETHEREAL, GODLIKE;

	public String toString() {
		switch(this) {
			case COMMON:
			return "Common";
			case UNCOMMON:
			return "Uncommon";
			case RARE:
			return "Rare";
			case LEGENDARY:
			return "Legendary";
			case GODLIKE:
			return "Mastery";
			case ETHEREAL:
			return "Ethereal";
			default:
			return "Common";
		}
	}

	public ChatColor getTierColor() {
		return getTierColor(this);
	}

	public static ChatColor getTierColor(String tier) {
		return getTierColor(ItemTier.valueOf(tier));
	}
	
	public static ChatColor getTierColor(ItemTier tier) {
		switch(tier) {
		case COMMON:
			return ChatColor.WHITE;
		case UNCOMMON:
			return ChatColor.GREEN;
		case RARE:
			return ChatColor.BLUE;
		case LEGENDARY:
			return ChatColor.LIGHT_PURPLE;
		case ETHEREAL:
			return ChatColor.AQUA;
		case GODLIKE:
			return ChatColor.GOLD;
		default:
			return ChatColor.WHITE;
		}
	}
}
