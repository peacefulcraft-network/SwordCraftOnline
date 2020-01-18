package net.peacefulcraft.sco.gamehandle.player;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.inventories.InventoryManager;
import net.peacefulcraft.sco.storage.SCOPlayerDataManager;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillManager;

public class SCOPlayer 
{
	private String partyName;
	private String lastInvite;
	private int playerKills;
	
	/**Tracks the players current floor in SCO */
	private int floor;
		public int getFloor() { return floor; }
		public void setFloor(int num) { floor = num; }
	
	private Player user;
		public Player getPlayer() {return this.user;}
		public UUID getUUID() {return this.user.getUniqueId();}
	
	private SCOPlayerDataManager scopData;
		public SCOPlayerDataManager getData() { return scopData; }
		public InventoryManager getInventoryManager() { return scopData.getInventories(); }
		
	/**Time remaining until another attack can be performed */
	private int attackTime;
		public int getAttackTime() { return attackTime; }
		public void setAttackTime(int ticks) { this.attackTime = ticks; }

	private SwordSkillManager swordSkillManager;
		public SwordSkillManager getSwordSkillManager() { return swordSkillManager; }

		
	private double exhaustion = 0.0;
		public double getExhaustion() { return exhaustion; }
		public void addExhaustion(double exhaustion) { this.exhaustion += exhaustion; }
		public void removeExhaustion(double exhaustion) { 
			// Prevent exhaustion from going below 0
			this.exhaustion = ((this.exhaustion - exhaustion) < 0)? 0 : this.exhaustion - exhaustion;
		}
		public void resetExhaustion() { this.exhaustion = 0.0; }

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
		floor = 0; //TODO: Load this from scopData
		
		scopData = new SCOPlayerDataManager(this);
		swordSkillManager = new SwordSkillManager(this);
	}

	/**True if player can perform left click */
	public boolean canAttack() {
		return attackTime == 0 || getPlayer().getGameMode() == GameMode.CREATIVE;
	}

	public void playerDisconnect() {
		scopData.saveConfig();
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
		String pChance = ChatColor.GOLD + "Parry Chance: " + ChatColor.BLUE + getParryChance() + '\n';
		String override = ChatColor.GOLD + "Admin Override: " + ChatColor.BLUE + hasOverride() + '\n';
		
		String skills = ChatColor.GOLD + "Active Skills: " + ChatColor.BLUE;
		for(SwordSkill s : swordSkillManager.getSkills()) {
			skills.concat(s.getProvider().getName() + ", ");
		}

		return header + partyName + playerKills + critChance + critMult + pChance + override + skills;
	}

	private String repeat(int count, String with) {
		return new String(new char[count]).replace("\0", with);
	}	
}
