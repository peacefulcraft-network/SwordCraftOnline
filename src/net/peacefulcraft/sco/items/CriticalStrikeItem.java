package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.item.ItemTier;
import net.peacefulcraft.sco.item.SkillIdentifier;
import net.peacefulcraft.sco.swordskill.SkillProvider;
import net.peacefulcraft.sco.swordskill.SwordSkillType;
import net.peacefulcraft.sco.swordskills.skills.CriticalStrikeSkill;

/**
 * Common Critical Strike - Flint
 * When the player scores 3 consecutive hits without taking any damage,
 * they will deal an additional 3 points of damage on the 3rd hit. The ability
 * will then go on a 5 second cooldown.
 */
public class CriticalStrikeItem implements SkillProvider{
	
	private SkillIdentifier id;
		public SkillIdentifier getId() { return id; }
	private String NAME = "Critical Strike";
	private int level;
	private ItemTier tier;
	private ArrayList<String> LORE = new ArrayList<String>();
	private Material MATERIAL = Material.FLINT;

	
	public CriticalStrikeItem(int level, ItemTier tier) {
		this.level = level;
		this.tier = tier;
	}

	
	@Override
	public void registerSkill(SCOPlayer s) {
		CriticalStrikeSkill cs = new CriticalStrikeSkill(s, 5000, (SkillProvider) this, 3, 4);
		s.getSwordSkillManager().registerSkill(SwordSkillType.ENTITY_DAMAGE_ENTITY, cs);
		
		// add variance based on item teir and/or level
	}



	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public ArrayList<String> getLore() {
		return LORE;
	}



	@Override
	public int getSkillLevel() {
		return this.level;
	}



	@Override
	public void setSkillLevel(int level) {
		this.level = level;
	}



	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(MATERIAL);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Critical Strike");
		item.setItemMeta(meta);
		
		NBTItem nbti = new NBTItem(item);
		nbti.setString("tier", tier.toString());
		nbti.setInteger("skill_level", level);
		nbti.setBoolean("movable", true);
		nbti.setBoolean("dropable", false);
		return nbti.getItem();
	}

}
