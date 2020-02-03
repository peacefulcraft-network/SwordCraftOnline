package net.peacefulcraft.sco.items;

import org.bukkit.Material;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.swordskills.CriticalStrikeSkill;
import net.peacefulcraft.sco.swordskills.SkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

/**
 * Common Critical Strike - Flint
 * When the player scores 3 consecutive hits without taking any damage,
 * they will deal an additional 3 points of damage on the 3rd hit. The ability
 * will then go on a 5 second cooldown.
 */
public class CriticalStrikeItem extends SkillProvider{

	public CriticalStrikeItem(int level, ItemTier tier) {
		super("Critical Strike", level, tier, null, Material.FLINT);	
	}

	@Override
	public void registerSkill(SCOPlayer s) {
		CriticalStrikeSkill cs = new CriticalStrikeSkill(s, 5000, (SkillProvider) this, 3, 4);
		s.getSwordSkillManager().registerSkill(SwordSkillType.ENTITY_DAMAGE_ENTITY, cs);
		
		// add variance based on item tier and/or level
	}
}
