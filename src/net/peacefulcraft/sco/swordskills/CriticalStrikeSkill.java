package net.peacefulcraft.sco.swordskills;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.peacefulcraft.sco.items.CriticalStrikeItem;
import net.peacefulcraft.sco.swordskills.modules.BasicCombo;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.BasicCombo.SwordSkillComboType;

public class CriticalStrikeSkill extends SwordSkill{
	
	private long delay;
	private double damageToDeal;
	private double hitsToTrigger;

	public CriticalStrikeSkill(SwordSkillCaster c, long delay, CriticalStrikeItem provider, int hitsToTrigger, double damageToDeal) {
		super(c, provider);
		this.delay = delay;
		this.hitsToTrigger = hitsToTrigger;
		this.damageToDeal = damageToDeal;

		this.listenFor(SwordSkillTrigger.ENTITY_DAMAGE_ENTITY_GIVE);
		this.useModule(new TimedCooldown(provider, delay));
		this.useModule(new BasicCombo(this, SwordSkillComboType.CONSECUTIVE_HITS_WITHOUT_TAKING_DAMAGE, hitsToTrigger));
	}

	@Override
	public boolean skillSignature(Event ev) {
		// Skill only requires an item be equipped in player's sword skill inventory
		// No physical checks required
		return true;
	}

	@Override
	public boolean skillPreconditions(Event ev) {
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
	public void skillUsed() {
		// No cleanup steps required		
	}

	@Override
	public void unregisterSkill() {
		// No player data changes. No unregister.
	}
}
