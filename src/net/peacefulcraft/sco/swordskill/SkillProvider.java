package net.peacefulcraft.sco.swordskill;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.item.ItemTier;
import net.peacefulcraft.sco.item.SkillIdentifier;

/**
 * An item which provides a sword skill to the player when equipped
 *
 */
public interface SkillProvider{
	
	/**
	 * @return IdentifiableItem with item's characteristics ( Material, Rarity )
	 */
	public SkillIdentifier getId();
	
	/**
	 * @return Name of the skill
	 */
	public String getName();
	
	/**
	 * @return Lore of the skill
	 */
	public ArrayList<String> getLore();

	/**
	 * @return level of the skill
	 */
	public int getSkillLevel();
	
	/**
	 * @param level: New skill level
	 */
	public void setSkillLevel(int level);
	
	/**
	 * Create item representation of the skill to display in user's inventory
	 * @return item representation of the skill
	 */
	public ItemStack getItem();
	

	/**
	 * Equip the skill
	 * Register SwordSkills to S's SwordSkillManager
	 * @param s: SCOPlayer to register skill listeners to
	 */
	public void registerSkill(SCOPlayer s);
	
	public static ArrayList<String> addDesc(String tier) {
		ArrayList<String> desc = new ArrayList<String>();
		switch(ItemTier.valueOf(tier.toUpperCase())) {
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
		switch(ItemTier.valueOf(tier.toUpperCase())) {
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
