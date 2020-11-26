package net.peacefulcraft.sco.gamehandle.player;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.gamehandle.regions.Region;
import net.peacefulcraft.sco.quests.QuestBookManager;
import net.peacefulcraft.sco.gamehandle.duel.Duel;
import net.peacefulcraft.sco.inventories.InventoryBase;
import net.peacefulcraft.sco.inventories.InventoryManager;
import net.peacefulcraft.sco.inventories.InventoryType;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicPet;
import net.peacefulcraft.sco.particles.DisplayType;
import net.peacefulcraft.sco.storage.PlayerDataManager;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillManager;
import net.peacefulcraft.sco.swordskills.utilities.DirectionalUtil;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class SCOPlayer extends ModifierUser implements SwordSkillCaster
{
	private String partyName;
	private String lastInvite;
	private int playerKills;
	
	/**Tracks the players current floor in SCO */
	private int floor;
		public int getFloor() { return floor; }
		public void setFloor(int num) { floor = num; }
	
	private UUID uuid;
		public UUID getUUID() { return uuid; }
		
	private Player user;
		public Player getPlayer() { return this.user; }
		public void linkPlayer(Player p) { this.user = p; }
		@Override
		public LivingEntity getLivingEntity() { return (LivingEntity)this.user; }
	
	private PlayerDataManager scopData;
		public PlayerDataManager getData() { return scopData; }
	
	private InventoryManager inventoryManager;
		public InventoryManager getInventoryManager() { return this.inventoryManager; }
		public InventoryBase getInventory(InventoryType inventory) { return inventoryManager.getInventory(inventory); }
		
	/**Time remaining until another attack can be performed */
	private int attackTime;
		public int getAttackTime() { return attackTime; }
		public void setAttackTime(int ticks) { this.attackTime = ticks; }

	/**Stores SwordSkills */
	private SwordSkillManager swordSkillManager;
		/**Returns instance SwordSkillManager from interface */
		public SwordSkillManager getSwordSkillManager() { return swordSkillManager; }
		
	private double exhaustion = 0.0;
		public double getExhaustion() { return exhaustion; }
		public void addExhaustion(double exhaustion) { this.exhaustion += exhaustion; }
		public void removeExhaustion(double exhaustion) { 
			// Prevent exhaustion from going below 0
			this.exhaustion = ((this.exhaustion - exhaustion) < 0)? 0 : this.exhaustion - exhaustion;
		}
		public void resetExhaustion() { this.exhaustion = 0.0; }

	private boolean adminOverride = false;
		public boolean hasOverride() { return this.adminOverride; }
		public void setAdminOverride(boolean bool) { this.adminOverride = bool; }

	/**Additional chance to increase item level on drop */
	private double bonusLevelMod = 0.0D;

	/**Additional chance to get more items on drop */
	private double bonusDropMod = 0.0D;

	/**Players exp multiplier */
	private double expMod = 1.0D;

	/**Players current movement direction */
	private DirectionalUtil.Movement movement;
		public DirectionalUtil.Movement getMovement() { return this.movement; }
		public void setMovement(DirectionalUtil.Movement m) { this.movement = m; }

	/**Players private bank */
	private int bank;
		public void depositBank(int i) { this.bank += i; }
		public boolean withdrawBank(int i) { 
			if(this.bank - i < 0) { return false; }
			this.bank -= i;
			return true;
		}
		/**
		 * Transfer money from wallet to bank.
		 * @param into true if moving money from wallet to bank. False is reverse.
		 * @return false if transfer failed.
		 */
		public boolean transfer(int i, boolean into) {
			if(into && withdrawBank(i)) { 
				depositBank(i);
				return true;
			} else if(!(into) && withdrawBank(i)) {
				depositBank(i);
				return true;
			}
			return false;
		}
	
	/**Players wallet. Can be stolen. */
	private int wallet;
		public void depositWallet(int i) { this.wallet += i; }
		public boolean withdrawWallet(int i) {
			if(this.wallet - i < 0) { return false; }
			this.wallet -= i;
			return true;
		}

	private QuestBookManager questBookManager;
		public QuestBookManager getQuestBookManager() { return questBookManager; }

	/**Players region */
	private Region r = null;
		public Region getRegion() { return this.r; }
		public void setRegion(Region r, boolean silent) { 
			this.r = r; 
			if(!silent) { r.sendTitle(this.user); }
		}

	/**Players level of particle display. Defaulted to full effect */
	private DisplayType displayType = DisplayType.FULL;
		public DisplayType getDisplayType() { return this.displayType; }
		public void setDisplayType(DisplayType t) { this.displayType = t; }

	/**Determines if player is in duel or not */
	private Duel duel = null;
		public void setDuel(Duel d) { this.duel = d; }
		public Duel getDuel() { return this.duel; }

	/**Players active pet */
	private MythicPet pet = null;
		public MythicPet getPet() { return this.pet; }
		public void setPet(MythicPet pet) { this.pet = pet; }

	/**The last mob related damage of this player */
	private ModifierUser lastCauseOfDamage = null;

	public SCOPlayer (UUID uuid) {
		this.uuid = uuid;
		playerKills = 0;
		floor = 0; //TODO: Load this from scopData
		
		scopData = new PlayerDataManager(this);
		
		swordSkillManager = new SwordSkillManager(this);
		questBookManager = new QuestBookManager(this);
		
		inventoryManager = new InventoryManager(this);
		inventoryManager.fetchInventory(InventoryType.SWORD_SKILL);
		inventoryManager.fetchInventory(InventoryType.QUEST_BOOK);

		//TODO: Remove this and replace with loading the wallet/bank from data
		this.wallet = 1000;
	}

	public Boolean isInDuel() {
		if(this.duel != null) { return true; }
		return false;
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

	/**
	 * Gets SCOPlayer current location
	 * @return Location of player
	 */
	public Location getLocation() {
		return user.getLocation();
	}

	/**
	 * Sets players LCOD to modifier user
	 * @param damager Instance that caused damage
	 */
	public void setLastCauseOfDamage(ModifierUser damager) {
		this.lastCauseOfDamage = damager;
	}

	/**
	 * Safely returns last cause of damage
	 * @return ModifierUser to cause damage, null otherwise
	 */
	public ModifierUser getLastCauseOfDamage() {
		if(lastCauseOfDamage == null) { return null; }

		// Last mob to cause damage is dead
		if(lastCauseOfDamage instanceof ActiveMob && ((ActiveMob)lastCauseOfDamage).isDead()) {
			return null;
		}

		return lastCauseOfDamage;
	}

	@Override
	public double getCombatModifier(CombatModifier mod) {
		switch(mod) {
            case CRITICAL_CHANCE:
                return criticalChance;
            case CRITICAL_MULTIPLIER:
                return criticalMultiplier;
            case PARRY_CHANCE:
                return parryChance;
            case PARRY_MULTIPLIER:
				return parryMultiplier;
			case ITEM_LEVEL:
				return this.bonusLevelMod;
			case BONUS_DROP:
				return this.bonusDropMod;
			case BONUS_EXP:
				return this.expMod;
            default:
                return -1;
        }
	}

	@Override
	public void setCombatModifier(CombatModifier mod, double amount, int duration) {
		double d = this.getCombatModifier(mod);

		switch(mod) {
            case CRITICAL_CHANCE:
                criticalChance = (int)amount;
            case CRITICAL_MULTIPLIER:
                criticalMultiplier = amount;
            case PARRY_CHANCE:
                parryChance = (int)amount;
            case PARRY_MULTIPLIER:
                parryMultiplier = amount;
			case ITEM_LEVEL:
				this.bonusLevelMod = amount;
			case BONUS_DROP:
				this.bonusDropMod = amount;
			case BONUS_EXP:
				this.expMod = amount;
		}
		
		if(duration != -1) {
			_setCombatModifier(mod, d, duration);
		}
	}

	@Override
	public void multiplyCombatModifier(CombatModifier mod, double amount, int duration) {
		double d = this.getCombatModifier(mod);

		this.setCombatModifier(mod, d * amount, -1);
		if(duration != -1) {
			_setCombatModifier(mod, d, duration);
		}
	}

	@Override
	public void addCombatModifier(CombatModifier mod, double amount, int duration) {
		double d = this.getCombatModifier(mod);

		this.setCombatModifier(mod, d + amount, -1);
		if(duration != -1) {
			_setCombatModifier(mod, d, duration);
		}
	}

	public String getPlayerData() {		
		String header = repeat(5, " ") + ChatColor.GOLD + "[" + ChatColor.BLUE + "SCOPlayer" + ChatColor.GOLD + "]" + ChatColor.BLUE + getName() + "'s Data" + '\n' 
		+ ChatColor.GOLD + repeat(40, "-") + '\n';
		String partyName = ChatColor.GOLD + "Party Name: " + ChatColor.BLUE + getPartyName() + '\n';
		String playerKills = ChatColor.GOLD + "Player Kills: " + ChatColor.BLUE + getPlayerKills() + '\n';
		String critChance = ChatColor.GOLD + "Critical Chance: " + ChatColor.BLUE + getCombatModifier(CombatModifier.CRITICAL_CHANCE) + '\n';
		String critMult = ChatColor.GOLD + "Critical Multiplier: " + ChatColor.BLUE + getCombatModifier(CombatModifier.CRITICAL_MULTIPLIER) + '\n'; 
		String pChance = ChatColor.GOLD + "Parry Chance: " + ChatColor.BLUE + getCombatModifier(CombatModifier.PARRY_CHANCE) + '\n';
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
