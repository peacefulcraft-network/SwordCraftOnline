package net.peacefulcraft.sco.swordskills.skills;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.event.Event;

public class SwordSkillManager
{
	private HashMap<SwordSkillType, ArrayList<SwordSkill>> skills = new HashMap<SwordSkillType, ArrayList<SwordSkill>>();

	private final List<String> skillNames = new ArrayList<String>();
		public List<String> getSkillNames() { return skillNames; }

	public void registerSkill(SwordSkillType type, SwordSkill skill) throws IllegalStateException {
		if(skills.get(type) == null) {
			skills.put(type, new ArrayList<SwordSkill>());
			skills.get(type).add(skill);
			skillNames.add(skill.getName());
			return;
		}

		for(SwordSkill s : skills.get(type)) {
			if(s.getClass().toString().equals(s.getClass().toString())) {
				throw new IllegalStateException("SwordSkill child " + s.getClass() + " is already registered with this executor");
			}

			skills.get(type).add(skill);
			skillNames.add(skill.getName());
			break;
		}
	}

	public void abilityExecuteLoop(SwordSkillType type, Event ev) {
		
		if(skills.get(type) == null) {
			return;
		}

		for(SwordSkill skill : skills.get(type)) {
			if(skill.skillSignature(ev) && skill.canUseSkill()) {
				skill.execute(ev);
				skill.markSkillUsed();
			}
		}
	}
	
	/**Returns true if skill exists in map */
	public final boolean doesSkillExist(SwordSkill skill) {
		for(SwordSkillType type : skills.keySet()) {
			if(skills.get(type).contains(skill)) {
				return true;
			}
		}
		return false;
	}

	public final ArrayList<SwordSkill> getSkills() {
		ArrayList<SwordSkill> dummy = new ArrayList<SwordSkill>();
		for(ArrayList<SwordSkill> s : skills.values()) {
			dummy.addAll(s);
		}
		return dummy;
	}

	public final SwordSkill getSkillByName(String name) {
		for(SwordSkill skill : getSkills()) {
			if(name.equals(skill.getName())) {
				return skill;
			}
		}
		return null;
	}

}
