package net.peacefulcraft.sco.items;

import java.util.ArrayList;

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

	/**
	 * Creates base description for item based on tier
	 * For use on ICustomItems not Sword Skills
	 * @param tier Tier of item
	 * @return ArrayList of strings for description of item
	 */
	public static ArrayList<String> addDesc(ItemTier tier) {
		ArrayList<String> desc = new ArrayList<>();
		ChatColor color = ItemTier.getTierColor(tier);
		switch(tier) {
		case COMMON:
			desc.add(color + "Common Item");
		break;case UNCOMMON:
			desc.add(color + "Uncommon Item");
		break;case RARE:
			desc.add(color + "Rare Item");
		break;case LEGENDARY:
			desc.add(color + "Legendary Item");
		break;case ETHEREAL:
			desc.add(color + "Ethereal Item");
		break;case GODLIKE:
			desc.add(color + "Godlike Item");
		}
		return desc;
	}

	/**
	 * Returns next tier
	 * @param tier Input tier
	 * @return Next tier up
	 */
	public static ItemTier progressTier(ItemTier tier) {
		switch(tier) {
			case COMMON:
				return ItemTier.UNCOMMON;
			case UNCOMMON:
				return ItemTier.RARE;
			case RARE:
				return ItemTier.LEGENDARY;
			case LEGENDARY:
				return ItemTier.ETHEREAL;
			case ETHEREAL:
				return ItemTier.GODLIKE;
			case GODLIKE:
				return ItemTier.GODLIKE;
			default:
				return tier;
		}
	}
}
