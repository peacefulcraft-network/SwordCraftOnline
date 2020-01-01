package net.peacefulcraft.sco.gamehandle.player;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.gamehandle.storage.SCOPlayerDataManager;
import net.peacefulcraft.sco.swordskills.ISkillProvider;
import net.peacefulcraft.sco.swordskills.skills.SkillActive;
import net.peacefulcraft.sco.swordskills.skills.SkillBase;

public class SCOPlayer 
{
	private String partyName;
	private String lastInvite;
	private int playerKills;
	
	private Player user;
		public Player getPlayer() {return this.user;}
		public UUID getUUID() {return this.user.getUniqueId();}
	
	private SCOPlayerDataManager scopData;
		public SCOPlayerDataManager getData() { return scopData; }

	/**Time remaining until another attack can be performed */
	private int attackTime;
		public int getAttackTime() { return attackTime; }
		public void setAttackTime(int ticks) { this.attackTime = ticks; }

	/**Stores info on player's skills */
	private final Map<Integer, SkillBase> skills;

	/**Used to temp store skill */
	private SkillBase itemSkill = null;

	/**Dummy version of basic sword skill for use with SkillItem skills when skill level 0 */
	private SkillBase dummySwordSkill = null;

	/**Slot of item providing persistent dummy sword skill if any */
	private int persistentDummySlot = -1;

	private final List<SkillActive> activeSkills = new LinkedList<SkillActive>();

	public SCOPlayer (Player user) {
		this.user = user;
		
		partyName = "";
		lastInvite = "";
		playerKills = 0;
		
		scopData = new SCOPlayerDataManager(this);

		this.skills = new HashMap<Integer, SkillBase>(SkillBase.getNumSkills());
	}

	/**True if player can perform left click */
	public boolean canAttack() {
		return attackTime == 0 || getPlayer().getGameMode() == GameMode.CREATIVE;
	}

	public boolean removeSkill(String name) {
		if(("all").equals(name)) {
			resetSkills();
			return true;
		} else {
			SkillBase dummy = null;
			for(SkillBase skill : skills.values()) {
				if(skill.getName().equals(name)) {
					dummy = skill;
					break;
				}
			}
			if(dummy != null) {
				removeSkill(dummy);
				return true;
			}
		}
		return false;
	}

	private void removeSkill(SkillBase skill) {
		SkillBase dummy = skill.newInstance();
		skills.put(dummy.getId(), dummy);
		//validateSkill();
		skills.remove(dummy.getId());
	}

	/**Resets all data related to skills */
	public void resetSkills() {
		for(SkillBase skill : SkillBase.getSkills()) {
			skills.put(skill.getId(), skill.newInstance());
		}
		//validateSkill();
		skills.clear();
	}
	/*
	public boolean hasSkill(SkillBase skill) {
		return hasSkill(skill.getId());
	}
	
	private boolean hasSkill(int id) {
		return getSkillLevel(id) > 0;
	}

	public int getSkillLevel(SkillBase skill) {
		return getSkillLevel(skill.getId());
	}*/
	/*
	public int getSkillLevel(int id) {
		int level = 0;
		if(itemSkill != null && itemSkill.getId() == id) {
			level = itemSkill.getLevel();
		} else if (id == SkillBase.skillBasic.getId()) {
			if(user.getInventory().getItemInMainHand() == null || user.getInventory().getItemInMainHand().getType() == Material.AIR) {
				retrieveDummySwordSkill();
			}
			if(dummySwordSkill != null) {
				level = dummySwordSkill.getLevel();
			}
 		} else if(itemSkill == null || dummySwordSkill == null) {
			 for(int i = 0; i < 9; ++i) {
				 ItemStack stack = user.getInventory().getItem(i);
				 
			 }
		 }
	}*/

	/**Return player's true skill level. */
	private int getTrueSkillLevel(int id) {
		return(skills.containsKey(id) ? skills.get(id).getLevel() : 0);
	}

	private void retrieveDummySwordSkill() {
		boolean needsDummy = (getTrueSkillLevel(SkillBase.skillBasic.getId()) < 1 && dummySwordSkill == null);
		if((needsDummy || itemSkill == null) && persistentDummySlot == -1) {
			for(int i = 0; i < 9; ++i) {
				ItemStack stack = user.getInventory().getItem(i);
				//TODO:Double check this cast
				if(((ISkillProvider) stack).grantsBasicSwordSkill(stack)) {
					dummySwordSkill = SkillBase.createLeveledSkill(SkillBase.skillBasic.getId(), 1);
					persistentDummySlot = i;
				}
				if(itemSkill == null) {
					itemSkill = SkillBase.getSkillFromItem(stack, (ISkillProvider) stack);
				}
				if(!needsDummy || dummySwordSkill != null) {
					break;
				}
			}
			if(dummySwordSkill == null) {
				persistentDummySlot = -30;
			}
		}
	}
	
	public void playerDisconnect() {
		scopData.saveConfig();
		scopData.getInventories().unregisterInventories();
	}
	
	public boolean isInParty() {
		if(partyName.equalsIgnoreCase("")) {
			return false;
		} else {
			return true;
		}
	}
	
	public int getLevel() {
		return user.getLevel();
	}
	
	public String getName() {
		return user.getName();
	}

	public String getPartyName() {
		return this.partyName;
	}

	public void setPartyName(String name) {
		this.partyName = name;
	}

	public String getLastInvite() {
		return this.lastInvite;
	}

	public void setLastInvite(String invite) {
		this.lastInvite = invite;
	}

	public int getPlayerKills() {
		return this.playerKills;
	}
	
	public void setPlayerKills(int red) {
		this.playerKills = red;
	}
	
}
