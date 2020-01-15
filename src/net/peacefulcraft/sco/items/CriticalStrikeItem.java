package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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
	private static final String NAME = "Critical Strike";
	private static final ArrayList<String> LORE = new ArrayList<String>();
	private static final Material MATERIAL = Material.FLINT;
	private static final ItemTier TIER = ItemTier.COMMON;
	private int skillLevel;
	
	private static final int COOLDOWN = 5000;
	private static final int HITSTOTRIGGER = 3;
	private static final int DAMAGE = 3;
	

	
	@Override
	public void registerSkill(SCOPlayer s) {
		CriticalStrikeSkill cs = new CriticalStrikeSkill(s, COOLDOWN, (SkillProvider) this, HITSTOTRIGGER, DAMAGE);
		s.getSwordSkillManager().registerSkill(SwordSkillType.ENTITY_DAMAGE_ENTITY, cs);
	}



	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public ArrayList<String> getLore() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public int getSkillLevel() {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public void setSkillLevel(int level) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public ItemStack getItem() {
		// TODO Auto-generated method stub
		return null;
	}

}
