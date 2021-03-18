package net.peacefulcraft.sco.swordskills;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.SCOInventory;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;

public class SwordSkillManager
{
	private SCOPlayer s;
		public SCOPlayer getSCOPlayer() { return this.s; }

	private LivingEntity e;
		public LivingEntity getLivingEntity() { return this.e; }
		
	private HashMap<SwordSkillTrigger, ArrayList<SwordSkill>> skills = new HashMap<SwordSkillTrigger, ArrayList<SwordSkill>>();

	/**Stores instance of SCOPlayer */
	public SwordSkillManager(SCOPlayer s) {
		this.s = s;
		this.e = (LivingEntity) s.getPlayer();
	}

	/**Converts ActiveMob into LivingEntity and stores. */
	public SwordSkillManager(ActiveMob am) {
		this.e = am.getLivingEntity();
	}
	
	public void registerSkill(SwordSkillTrigger type, SwordSkill skill) throws IllegalStateException {
		if(skills.get(type) == null) {
			skills.put(type, new ArrayList<SwordSkill>());
		} else {
			for(SwordSkill s : skills.get(type)) {
				if(s == skill) {
					throw new IllegalStateException("SwordSkill " + s.getClass() + " is already registered with this executor");
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

		for(SwordSkill skill : skills.get(type)) {
			skill.execSkillSupportLifecycle(type, ev);
			skill.execPrimaryLifecycle(type, ev);
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

	public List<SwordSkill> getSkills() {
		ArrayList<SwordSkill> dummy = new ArrayList<SwordSkill>();
		for(ArrayList<SwordSkill> s : skills.values()) {
			dummy.addAll(s);
		}
		return dummy;
	}
	
	public void unregisterAllSkills() {
		for(SwordSkill skill : getSkills()) {
			skill.execSkillUnregistration();
		}
		skills = new HashMap<SwordSkillTrigger, ArrayList<SwordSkill>>();
	}

	public void unregisterSkill(SwordSkill skill) {
		// Loop through all trigger types
		Iterator<ArrayList<SwordSkill>> skillTypesIterator = this.skills.values().iterator();
		while (skillTypesIterator.hasNext()) {

			// Loop through all sword skills on the trigger type
			Iterator<SwordSkill> skillTypeListIterator = skillTypesIterator.next().iterator();
			while (skillTypeListIterator.hasNext()) {

				// Unregister the skill if it matches the removal target
				SwordSkill loopSkill = skillTypeListIterator.next();
				if (loopSkill == skill) {
					loopSkill.execSkillUnregistration();
					skillTypeListIterator.remove();
					break;
				}

			}

		}
	}

	public void syncSkillInventory(SCOInventory inv) {
		List<ItemIdentifier> items = inv.generateItemIdentifiers();
		List<SwordSkill> skills = getSkills();

		// Diff the current sword skills with the new ones
		Iterator<ItemIdentifier> itemsIterator = items.iterator();
		while (itemsIterator.hasNext()) {
			ItemIdentifier item = itemsIterator.next();
			Iterator<SwordSkill> skillsIterator = skills.iterator();

			while(skillsIterator.hasNext()) {
				SwordSkillProvider skill = skillsIterator.next().getProvider();
				if (ItemIdentifier.compareTo(item, skill, false) == 0) {
					// Make sure items with cooldown match exactly so prevent cooldown bypassing
					if (item instanceof SwordSkillCooldownProvider && skill instanceof SwordSkillCooldownProvider){
						if (((SwordSkillCooldownProvider) item).getCooldownEnd() == ((SwordSkillCooldownProvider) skill).getCooldownEnd()) {
							itemsIterator.remove();
							skillsIterator.remove();
						} else {
							continue;
						}
					}

					// Remove match skill / item identifier
					itemsIterator.remove();
					skillsIterator.remove();
					SwordCraftOnline.logDebug("SSM Inventory sync will not touch already equiped skill from provider " + item.getName());
					break;
				}
			}
		}

		// Only remove the skills that don't have providers in the SCOInventory anymore
		for (SwordSkill oldSkill : skills) {
			SwordCraftOnline.logDebug("SSM Inventory sync is removing skill " + oldSkill.getProvider().getName());
			this.unregisterSkill(oldSkill);
		}

		// Only register the skills that were not found to already be in the inventory
		for(ItemIdentifier item : items) {
			if (item instanceof SwordSkillProvider) {
				SwordCraftOnline.logDebug("SSM Inventory sync is adding skill " + item.getName());
				((SwordSkillProvider) item).registerSwordSkill(s);
			}
		}
	}
}
