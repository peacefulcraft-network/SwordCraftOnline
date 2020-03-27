package net.peacefulcraft.sco.swordskills;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.items.SkillIdentifier;

/**
 * An item which provides a sword skill to the player when equipped
 *
 */
public abstract class SkillProvider{
	
	protected String name;
	protected int level;
	protected ItemTier tier;
	protected ArrayList<String> lore;
	protected Material material;
	
	public SkillProvider(String name, int level, ItemTier tier, ArrayList<String> lore, Material material) {
		this.name = name;
		this.level = level;
		this.tier = tier;
		this.lore = lore;
		this.material = material;
	}
	
	/**
	 * @return IdentifiableItem with item's characteristics ( Material, Rarity )
	 */
	public SkillIdentifier getId() {
		return new SkillIdentifier(name, level, tier);
	}
	
	/**
	 * @return Name of the skill
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return Lore of the skill
	 */
	public ArrayList<String> getLore(){
		return lore;
	}

	/**
	 * @param lore: New item lore
	 */
	public void setLore(ArrayList<String> lore) {
		this.lore = lore;
	}

	/**
	 * @return level of the skill
	 */
	public int getSkillLevel() {
		return level;
	}
	
	/**
	 * @param level: New skill level
	 */
	public void setSkillLevel(int level) {
		this.level = level;
	}
	
	/**
	 * Create item representation of the skill to display in user's inventory
	 * @return item representation of the skill
	 */
	public ItemStack getItem() {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(getTierColor(this.tier) + this.name);
		meta.setLore(this.lore);
		item.setItemMeta(meta);
		
		NBTItem nbti = new NBTItem(item);
		nbti.setString("tier", tier.toString().toUpperCase());
		nbti.setInteger("skill_level", level);
		nbti.setBoolean("movable", true);
		nbti.setBoolean("dropable", false);
		return nbti.getItem();
	}


	/**
	 * Equip the skill
	 * Register SwordSkills to S's SwordSkillManager
	 * @param s: SCOPlayer to register skill listeners to
	 */
	public abstract void registerSkill(SwordSkillCaster c);

	/**
	 * Used to set lore by tier.
	 */
	public abstract void setLore();

	/**
	 * Used to set modifiers. I.e. damage and cooldown.
	 */
	public abstract void setModifiers();
	
	public static ArrayList<String> addDesc(String tier){
		return addDesc(ItemTier.valueOf(tier));
	}
	
	public static ArrayList<String> addDesc(ItemTier tier) {
		ArrayList<String> desc = new ArrayList<String>();
		switch(tier) {
		case COMMON:
			desc.add(getTierColor(tier) + "Common Sword Skill");
		break;case UNCOMMON:
			desc.add(getTierColor(tier) + "Uncommon Sword Skill");
		break;case RARE:
			desc.add(getTierColor(tier) + "Rare Sword Skill");
		break;case LEGENDARY:
			desc.add(getTierColor(tier) + "Legendary Sword Skill");
		break;case ETHEREAL:
			desc.add(getTierColor(tier) + "Mastery Sword Skill");
		break;case GODLIKE:
			desc.add(getTierColor(tier) + "Godlike Sword Skill");
		}
		return desc;
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
