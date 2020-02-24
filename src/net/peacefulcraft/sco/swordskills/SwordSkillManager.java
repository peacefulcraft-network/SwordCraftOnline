package net.peacefulcraft.sco.swordskills;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.SwordSkillInventory;
import net.peacefulcraft.sco.items.SkillIdentifier;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;

public class SwordSkillManager
{
	private SCOPlayer s = null;
		public SCOPlayer getPlayer() { return this.s; }

	private LivingEntity e = null;
		public LivingEntity getLivingEntity() { return this.e; }
		
	private HashMap<SwordSkillType, ArrayList<SwordSkill>> skills = new HashMap<SwordSkillType, ArrayList<SwordSkill>>();

	/**Stores instance of SCOPlayer */
	public SwordSkillManager(SCOPlayer s) {
		this.s = s;
		this.e = (LivingEntity)s.getPlayer();
	}

	/**Converts ActiveMob into LivingEntity and stores. */
	public SwordSkillManager(ActiveMob am) {
		this.e = am.getLivingEntity();
	}
	
	public void registerSkill(SwordSkillType type, SwordSkill skill) throws IllegalStateException {
		if(skills.get(type) == null) {
			skills.put(type, new ArrayList<SwordSkill>());
			skills.get(type).add(skill);
			return;
		}

		for(SwordSkill s : skills.get(type)) {
			if(s.getClass().toString().equals(s.getClass().toString())) {
				throw new IllegalStateException("SwordSkill child " + s.getClass() + " is already registered with this executor");
			}

			skills.get(type).add(skill);
			break;
		}
	}

	public void abilityExecuteLoop(SwordSkillType type, Event ev) {
		
		if(skills.get(type) == null) {
			return;
		}

		for(SwordSkill skill : skills.get(type)) {
			// Check if this skill needs to know about this event
			if(!skill.skillSignature(ev)) {
				continue;
			}
			
			// Don't process event if the skill is cooling down
			if(skill.isCoolingDown()) {
				continue;
			}
			
			//Timer Logic
			if(skill.isUsingTimer()) {
				// If using timer and it's not running, we should start it
				if(!skill.isCountingDown()) {
					skill.startObjectiveTimer();
				}
				/*
				 *	The timer resets the combo objective automatically 
				 *	so if time has expired for the combo to trigger, 
				 *	it will automatically be reset when we check the combo
				 *	threshold next. 
				 *
				 */
			}
			
			if(skill.isTrackingCombos()) {
				// Player Hit / Damage Combos
				if(ev instanceof EntityDamageByEntityEvent) {
					
					EntityDamageByEntityEvent ede = (EntityDamageByEntityEvent) ev;
					// If our player was the one dealing damage
					if(ede.getDamager() == s.getPlayer()){
						if(
							skill.getComboType() == SwordSkillComboType.CONSECUTIVE_HITS_IGNORE_TAKEN_DAMAGE
							|| skill.getComboType() == SwordSkillComboType.CONSECUTIVE_HITS_WITHOUT_TAKING_DAMAGE
						) {
							skill.comboAccumulate(1.0);

						}else if(
							skill.getComboType() == SwordSkillComboType.CUMULATIVE_DAMAGE_IGNORE_TAKEN_DAMAGE
							|| skill.getComboType() == SwordSkillComboType.CUMULATIVE_DAMAGE_WITHOUT_TAKING_DAMAGE
						) {
							skill.comboAccumulate(((EntityDamageEvent) ev).getDamage());
						}
					
					// Else, we must have been the one receiving damage
					} else {
						// If the ability requires we don't take damage to activate the combo, reset the combo counter because we took damage
						if(
							skill.getComboType() == SwordSkillComboType.CONSECUTIVE_HITS_WITHOUT_TAKING_DAMAGE
							|| skill.getComboType() == SwordSkillComboType.CUMULATIVE_DAMAGE_WITHOUT_TAKING_DAMAGE
						) {
							
							// Reset tracking because the player took damage
							if(skill.isUsingTimer()) { skill.resetObjectiveTimer(); }
							if(skill.isTrackingCombos()) { skill.comboResetAccumulation(); }
						}
					
					}
				}
			}
			
			// Final custom ability checks before we trigger the ability
			if(!skill.canUseSkill()) {
				continue;
			}
			
			skill.triggerSkill(ev);
			
			skill.markSkillUsed();
			skill.triggerCooldown();
			if(skill.isUsingTimer()) { skill.resetObjectiveTimer(); }
			if(skill.isTrackingCombos()) { skill.comboResetAccumulation(); }
		}
	}
	
	/**Returns true if skill exists in map */
	public boolean isSkillRegistered(SwordSkill skill) {
		for(SwordSkillType type : skills.keySet()) {
			if(skills.get(type).contains(skill)) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<SwordSkill> getSkills() {
		ArrayList<SwordSkill> dummy = new ArrayList<SwordSkill>();
		for(ArrayList<SwordSkill> s : skills.values()) {
			dummy.addAll(s);
		}
		return dummy;
	}
	
	public void unregisterAllSkills() {
		for(SwordSkill skill : getSkills()) {
			unregisterSkill(skill);
		}
		skills = new HashMap<SwordSkillType, ArrayList<SwordSkill>>();
	}
	
		/**
		 * Doesn't actually remove the skill, just tells it it is going to be removed
		 * @param skill to remove
		 */
		private void unregisterSkill(SwordSkill skill) {
			for(SwordSkill registeredSkill : getSkills()) {
				if(registeredSkill == skill) {
					((SwordSkill) skill).destroy();
					skill.unregisterSkill();
					skill.destroy();
					return;
				}
			}
		}
	
	/**
	 * Unregisters all of a player's SwordSkills, then re-registers a new
	 * set of abilities based off of what is currently in the player's SwordSkill Inventory
	 */
	public void syncSkillInventory(SwordSkillInventory inv) {
		unregisterAllSkills();
		for(SkillIdentifier identifier : inv.generateSkillIdentifiers()) {
			identifier.getProvider().registerSkill(s);
		}
	}

}
