package net.peacefulcraft.sco.gamehandle.player;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.swordskills.skills.SwordSkill;

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
	private final ArrayList<SwordSkill> skills;
		public ArrayList<SwordSkill> getSkills() { return skills; }

	/**Stores players critical damage chance 
	 * TODO:Handle situation where chance is above 100
	*/
	private int criticalChance = 2;
		public int getCriticalChance() { return criticalChance; }
		public void setCriticalChance(int num) { criticalChance = num; }
		public void addCritical(int num) { criticalChance+=num; }

	private double criticalMultiplier = 1.2;
		public double getCriticalMultiplier() { return criticalMultiplier; }
		public void setCriticalMultiplier(double num) { criticalMultiplier = num; }

	private boolean adminOverride = false;
		public boolean hasOverride() { return adminOverride; }
		public void setAdminOverride(boolean bool) { adminOverride = bool; }

	private int parryChance = 0;
		public int getParryChance() { return parryChance; }
		public void setParryChance(int num) { parryChance = num; }

	public SCOPlayer (Player user) {
		this.user = user;
		
		partyName = "";
		lastInvite = "";
		playerKills = 0;
		
		scopData = new SCOPlayerDataManager(this);

		this.skills = new ArrayList<SwordSkill>();
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
			for(SwordSkill skill : skills) {
				if(name == skill.getName()) {
					removeSkill(skill);
					return true;
				}
			}
		}
		return false;
	}

	private void removeSkill(SwordSkill skill) {
		skills.remove(skill);
	}	

	public void resetSkills() {
		skills.clear();
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

	public String getPlayerData() {		
		String header = repeat(5, " ") + ChatColor.GOLD + "[" + ChatColor.BLUE + "SCOPlayer" + ChatColor.GOLD + "]" + ChatColor.BLUE + getName() + "'s Data" + '\n' 
		+ ChatColor.GOLD + repeat(40, "-") + '\n';
		String partyName = ChatColor.GOLD + "Party Name: " + ChatColor.BLUE + getPartyName() + '\n';
		String playerKills = ChatColor.GOLD + "Player Kills: " + ChatColor.BLUE + getPlayerKills() + '\n';
		String critChance = ChatColor.GOLD + "Critical Chance: " + ChatColor.BLUE + getCriticalChance() + '\n';
		String critMult = ChatColor.GOLD + "Critical Multiplier: " + ChatColor.BLUE + getCriticalMultiplier() + '\n'; 
		String pChance = ChatColor.GOLD + "Parry Chance: " + ChatColor.BLUE + getParryChance();
		String override = ChatColor.GOLD + "Admin Override: " + ChatColor.BLUE + hasOverride() + '\n';
		
		String skills = ChatColor.GOLD + "Active Skills: " + ChatColor.BLUE;
		for(SwordSkill s : getSkills()) {
			skills.concat(s.getName() + ", ");
		}

		return header + partyName + playerKills + critChance + critMult + pChance + override + skills;
	}

	private String repeat(int count, String with) {
		return new String(new char[count]).replace("\0", with);
	}	
}
