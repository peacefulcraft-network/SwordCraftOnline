package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import org.bukkit.Material;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.swordskills.CriticalStrikeSkill;
import net.peacefulcraft.sco.swordskills.SkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

/**
 * Common Critical Strike - Flint
 * When the player scores 3 consecutive hits without taking any damage,
 * they will deal an additional 3 points of damage on the 3rd hit. The ability
 * will then go on a 5 second cooldown.
 */
public class CriticalStrikeItem extends SkillProvider{
	private int cooldown;
	private int damage;
	
	public CriticalStrikeItem(int level, ItemTier tier) {
		super("Critical Strike", level, tier, null, Material.FLINT);
		setLore();
	}

	@Override
	public void registerSkill(SwordSkillCaster c) {
		setModifiers();
		CriticalStrikeSkill cs = new CriticalStrikeSkill(c, this.cooldown, (SkillProvider) this, 3, this.damage);
		c.getSwordSkillManager().registerSkill(SwordSkillType.ENTITY_DAMAGE_ENTITY, cs);
		
		// add variance based on item tier and/or level
	}

	@Override
	public void setLore() {
		ArrayList<String> lore = SkillProvider.addDesc(this.tier);
		lore.add(getTierColor(this.tier) + "A Beginners 3 hit combo.");
		switch(this.tier) {
		case COMMON:
			lore.add(getTierColor(this.tier) + "Combo Damage: 3");
			lore.add(getTierColor(this.tier) + "Cooldown: 5 seconds");
		break;case UNCOMMON:
			lore.add(getTierColor(this.tier) + "Combo Damage: 4");
			lore.add(getTierColor(this.tier) + "Cooldown: 5 seconds");
		break;case RARE:
			lore.add(getTierColor(this.tier) + "Combo Damage: 5");
			lore.add(getTierColor(this.tier) + "Cooldown: 5 seconds");
		break;case LEGENDARY:
			lore.add(getTierColor(this.tier) + "Combo Damage: 7");
			lore.add(getTierColor(this.tier) + "Cooldown: 5 seconds");
		break;case ETHEREAL:
			lore.add(getTierColor(this.tier) + "Combo Damage: 10");
			lore.add(getTierColor(this.tier) + "Cooldown: 4 seconds");
		break;case GODLIKE:
			lore.add(getTierColor(this.tier) + "Combo Damage: 12");
			lore.add(getTierColor(this.tier) + "Cooldown: 5 seconds");
		}
		this.setLore(lore);
	}

	@Override
	public void setModifiers() {
		switch(this.tier) {
			case COMMON:
				this.cooldown = 5000; 
				this.damage = 3;
			break;case UNCOMMON:
				this.cooldown = 5000;
				this.damage = 4;
			break;case RARE:
				this.cooldown = 5000;
				this.damage = 5;
			break;case LEGENDARY:
				this.cooldown = 5000;
				this.damage = 7;
			break;case ETHEREAL:
				this.cooldown = 4000;
				this.damage = 10;
			break;case GODLIKE:
				this.cooldown = 5000;
				this.damage = 12;
		}
	}
}
