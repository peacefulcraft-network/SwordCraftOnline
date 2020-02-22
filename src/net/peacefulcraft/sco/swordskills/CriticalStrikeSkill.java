package net.peacefulcraft.sco.swordskills;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.peacefulcraft.sco.swordskills.modules.BasicCombo;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.BasicCombo.SwordSkillComboType;

public class CriticalStrikeSkill extends SwordSkill{
	
	private double damageToDeal;
	
	public CriticalStrikeSkill(SwordSkillCaster c, long delay, SkillProvider provider, int hitsToTrigger, double damageToDeal) {
		super(c, provider);
		useModule(new TimedCooldown(delay));
		useModule(new BasicCombo(SwordSkillComboType.CONSECUTIVE_HITS_WITHOUT_TAKING_DAMAGE, hitsToTrigger));
		
		this.damageToDeal = damageToDeal;
	}

	@Override
	public boolean skillSignature(Event ev) {
		// Skill only requires an item be equipped in player's sword skill inventory
		// No physical checks required
		return true;
	}

	@Override
	public boolean canUseSkill(Event ev) {
		// No extra checks required
		return true;
	}
	
	@Override
	public void triggerSkill(Event ev) {
		EntityDamageByEntityEvent ede = (EntityDamageByEntityEvent) ev;
		ede.setDamage( ede.getFinalDamage() + damageToDeal);
		((Player) ede.getDamager()).sendMessage("Critial Hit!");
	}

	@Override
	public void markSkillUsed() {
		// No cleanup steps required		
	}

	@Override
	public void unregisterSkill() {
		// No player data changes. No unregister.
	}
}
