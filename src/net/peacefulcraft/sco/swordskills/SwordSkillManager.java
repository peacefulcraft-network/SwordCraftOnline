package net.peacefulcraft.sco.swordskills;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.inventories.SCOInventory;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;

public class SwordSkillManager
{
	private SwordSkillCaster c;

	private HashMap<SwordSkillTrigger, ArrayList<SwordSkill>> skills = new HashMap<SwordSkillTrigger, ArrayList<SwordSkill>>();

	/**Stores instance of SCOPlayer */
	public SwordSkillManager(SwordSkillCaster c) {
		this.c = c;
	}
	
	public void registerSkill(SwordSkillTrigger type, SwordSkill skill) throws IllegalStateException {
		if(skills.get(type) == null) {
			skills.put(type, new ArrayList<SwordSkill>());
		} else {
			for(SwordSkill s : skills.get(type)) {
				if(s.getClass().toString().equals(s.getClass().toString())) {
					throw new IllegalStateException("SwordSkill child " + s.getClass() + " is already registered with this executor");
				}
			}
		}

		skills.get(type).add(skill);
	}

	/**
	 * Used by SwordSkill to dynamically register listeners for SwordSkillModules.
	 * @param type The event loop type to register on
	 * @param skill The SwordSkill instance which needs to be notified of the given event type
	 */
	public void registerListener(SwordSkillTrigger type, SwordSkill skill) {
		ArrayList<SwordSkill> listeners = skills.get(type);
		
		// Create the list for this loop type and add the skill if the list doesn't exist
		if(listeners == null) {
			listeners = new ArrayList<SwordSkill>();
			listeners.add(skill);
			skills.put(type, listeners);
			return;
		}

		// Skill already listens here and doesn't need to be double registered
		if(listeners.contains(skill)) {
			return;
		}

		// List already existed and the skill was not on it. Add the skill to this listener loop
		listeners.add(skill);
	}

	/**
	 * Used by SwordSkill to dynamically unregister itself from listeners for SwordSkillModules
	 * @param skill The SwordSkill instance which needs to be removed from the notifier list
	 */
	public void unregisterListener(SwordSkillTrigger type, SwordSkill skill) {
		ArrayList<SwordSkill> listeners = skills.get(type);
		listeners.remove(skill);

		if(listeners.size() == 0) {
			skills.remove(type);
		}
	}

	public void abilityExecuteLoop(SwordSkillTrigger type, Event ev) {
		if(skills.get(type) == null) {
			return;
		}

		if(this.c instanceof ActiveMob) {
			SwordCraftOnline.logDebug("[Sword Skill Manager] Active Mob skill select hit.");

			ArrayList<SwordSkill> skillss = skills.get(type);
			SwordSkill skill = skillss.get(SwordCraftOnline.r.nextInt(skillss.size()));
			skill.execSkillSupportLifecycle(type, ev);
			skill.execPrimaryLifecycle(type, ev);
		} else {
			for(SwordSkill skill : skills.get(type)) {
				skill.execSkillSupportLifecycle(type, ev);
				skill.execPrimaryLifecycle(type, ev);
			}
		}
	}
	
	/**Returns true if skill exists in map */
	public boolean isSkillRegistered(SwordSkill skill) {
		for(SwordSkillTrigger type : skills.keySet()) {
			if(skills.get(type).contains(skill)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get list of all registered skills
	 */
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
		skills = new HashMap<SwordSkillTrigger, ArrayList<SwordSkill>>();
	}
	
		/**
		 * Doesn't actually remove the skill, just tells it it is going to be removed
		 * @param skill to remove
		 */
		private void unregisterSkill(SwordSkill skill) {
			for(SwordSkill registeredSkill : getSkills()) {
				if(registeredSkill == skill) {
					skill.execSkillUnregistration();
					return;
				}
			}
		}
	
	/**
	 * Unregisters all of a player's SwordSkills, then re-registers a new
	 * set of abilities based off of what is currently in the player's SwordSkill Inventory
	 */
	public void syncSkillInventory(SCOInventory inv) {
		unregisterAllSkills();
		for(ItemIdentifier identifier : inv.generateItemIdentifiers()) {
			if (identifier instanceof SwordSkillProvider) {
				((SwordSkillProvider) identifier).registerSwordSkill(c);
			}
		}
	}
}
