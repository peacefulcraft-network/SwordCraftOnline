package net.peacefulcraft.sco.swordskills;

import java.util.UUID;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser.CombatModifier;

public class CriticalStrikeSkill extends SwordSkill{
	
	private Integer increase;
	private UUID change1 = null;

	public CriticalStrikeSkill(SwordSkillCaster c, Integer increase, long delay, SwordSkillProvider provider) {
		super(c, provider);
		this.increase = increase;

		this.listenFor(SwordSkillTrigger.PASSIVE);
	}

	@Override
	public boolean skillSignature(Event ev) {
		// Skill only requires an item be equipped in player's sword skill inventory
		// No physical checks required
		return true;
	}

	@Override
	public boolean skillPreconditions(Event ev) {
		return change1 == null;
	}
	
	@Override
	public void triggerSkill(Event ev) {
		if(this.c instanceof ModifierUser) {
			ModifierUser mu = (ModifierUser)c;
			change1 = mu.queueChange(CombatModifier.CRITICAL_CHANCE, this.increase, -1);
		}
	}

	@Override
	public void skillUsed() {
		// No cleanup steps required		
	}

	@Override
	public void unregisterSkill() {
		if(this.c instanceof ModifierUser) {
			ModifierUser mu = (ModifierUser)c;
			mu.dequeueChange(change1);
		}
	}
}
