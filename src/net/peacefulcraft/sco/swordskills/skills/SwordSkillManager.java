package net.peacefulcraft.sco.swordskills.skills;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

public class SwordSkillManager
{
	private HashMap<SwordSkillType, ArrayList<SwordSkill>> skills = new HashMap<SwordSkillType, ArrayList<SwordSkill>>();

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
			if(skill.skillSignature(ev) && skill.canUseSkill()) {
				skill.execute(ev);
				skill.markSkillUsed();
			}
		}
	}
	
}
