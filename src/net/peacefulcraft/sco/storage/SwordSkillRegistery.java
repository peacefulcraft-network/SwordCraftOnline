package net.peacefulcraft.sco.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.items.CriticalStrikeItem;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.items.SkillIdentifier;
import net.peacefulcraft.sco.storage.tasks.SyncSwordSkillRegistry;

public class SwordSkillRegistery {

	private static ArrayList<String> swordSkillNames;
		public static ArrayList<String> getSwordSkillNames() { return swordSkillNames; }
	
	private static HashMap<SkillIdentifier, Boolean> swordSkills;
		public static HashMap<SkillIdentifier, Boolean> getSwordSkills(){ return swordSkills; }
		public static boolean isSkillActive(SkillIdentifier skill) { return swordSkills.get(skill); }
		
	public SwordSkillRegistery() {
		swordSkills = new HashMap<SkillIdentifier, Boolean>();
		swordSkillNames = new ArrayList<String>();
		
		this.regsiterSwordSkill(new CriticalStrikeItem(1, ItemTier.COMMON).getId(), true);
		this.regsiterSwordSkill(new CriticalStrikeItem(1, ItemTier.UNCOMMON).getId(), true);
		this.regsiterSwordSkill(new CriticalStrikeItem(1, ItemTier.RARE).getId(), true);
		this.regsiterSwordSkill(new CriticalStrikeItem(1, ItemTier.LEGENDARY).getId(), true);
		this.regsiterSwordSkill(new CriticalStrikeItem(1, ItemTier.MASTERY).getId(), true);
		this.regsiterSwordSkill(new CriticalStrikeItem(1, ItemTier.ETHEREAL).getId(), true);
		
		// Will run blocking, but this data needs to be consistent before any players try to join
		(new SyncSwordSkillRegistry()).run();
	}
	
	public void regsiterSwordSkill(SkillIdentifier skill, Boolean active) {
		swordSkills.put(skill, active);
		swordSkillNames.add(skill.getSkillName());
	}
	
	public static SkillIdentifier getGlobalIdentifier(SkillIdentifier localIdentifier) {
		for(Entry<SkillIdentifier, Boolean> i : swordSkills.entrySet()) {
			if(i.getKey().compareTo(localIdentifier) == 0) {
				return i.getKey();
			}
		}
		
		return null;
	}
}
