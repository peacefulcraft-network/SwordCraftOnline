package net.peacefulcraft.sco.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.items.SkillIdentifier;
import net.peacefulcraft.sco.storage.tasks.SyncSwordSkillRegistry;

public class SwordSkillRegistery {

	private ArrayList<String> swordSkillNames;
		public ArrayList<String> getSwordSkillNames() { return swordSkillNames; }
	
	private HashMap<SkillIdentifier, Boolean> swordSkills;
		public HashMap<SkillIdentifier, Boolean> getSwordSkills(){ return swordSkills; }
		public boolean isSkillActive(SkillIdentifier skill) { return swordSkills.get(skill); }
		
	public SwordSkillRegistery() {
		swordSkills = new HashMap<SkillIdentifier, Boolean>();
		swordSkillNames = new ArrayList<String>();
		
		// Register sword skills
		
		// Will run blocking, but this data needs to be consistent before any players try to join
		(new SyncSwordSkillRegistry()).runTask(SwordCraftOnline.getPluginInstance());
	}
	
	public void regsiterNewSwordSkill(SkillIdentifier skill, Boolean active) {
		swordSkills.put(skill, active);
		swordSkillNames.add(skill.getSkillName());
	}
	
	public SkillIdentifier getGlobalIdentifier(SkillIdentifier localIdentifier) {
		for(Entry<SkillIdentifier, Boolean> i : swordSkills.entrySet()) {
			if(i.getKey().compareTo(localIdentifier) == 0) {
				return i.getKey();
			}
		}
		
		return null;
	}
}
