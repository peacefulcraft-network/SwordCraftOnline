package net.peacefulcraft.sco.items;

import org.bukkit.ChatColor;

public enum ItemTier
{
	COMMON, UNCOMMON, RARE, LEGENDARY,
	ETHEREAL, GODLIKE;

	public String toString() {
		switch(this) {
			case COMMON:
			return "common";
			case UNCOMMON:
			return "uncommon";
			case RARE:
			return "rare";
			case LEGENDARY:
			return "legendary";
			case GODLIKE:
			return "mastery";
			case ETHEREAL:
			return "ethereal";
			default:
			return "common";
		}
	}

	/**
	 * Gets server constant chat colors for item tiers
	 * @param tier Item tier we encode
	 * @return Respective Chatcolor of tier
	 */
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

	/**
	 * Gets server constant chat colors for item tiers
	 * @param tier String of tier we want
	 * @return Respective chatcolor of tier, white if none found
	 */
	public static ChatColor getTierColor(String tier) {
		try{
			return getTierColor(ItemTier.valueOf(tier));
		} catch(IllegalArgumentException ex) {
			return ChatColor.WHITE;
		}
	}
}
