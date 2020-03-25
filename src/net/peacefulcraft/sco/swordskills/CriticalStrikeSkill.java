package net.peacefulcraft.sco.swordskills;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

public class CriticalStrikeSkill extends SwordSkill{
	
	private long delay;
	private double damageToDeal;
	private double hitsToTrigger;

	public CriticalStrikeSkill(SwordSkillCaster c, long delay, SkillProvider provider, int hitsToTrigger, double damageToDeal) {
		super(c, provider);
		this.delay = delay;
		this.hitsToTrigger = hitsToTrigger;
		this.damageToDeal = damageToDeal;

		this.listenFor(SwordSkillType.ENTITY_DAMAGE_ENTITY_GIVE);
		this.useModule(new TimedCooldown(delay));
		this.useModule(new BasicCombo(this, SwordSkillComboType.CONSECUTIVE_HITS_WITHOUT_TAKING_DAMAGE, hitsToTrigger));
	}

	@Override
	public boolean skillSignature(Event ev) {
		// Skill only requires an item be equipped in player's sword skill inventory
		// No physical checks required
		return true;
	}

	@Override
	public boolean canUseSkill() {
		// No extra checks required
		return true;
	}
	
	@Override
	public void triggerSkill(Event ev) {
		EntityDamageByEntityEvent ede = (EntityDamageByEntityEvent) ev;
		ede.setDamage( ede.getDamage() + damageToDeal);
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
