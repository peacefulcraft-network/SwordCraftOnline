package net.peacefulcraft.sco.gamehandle.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.gamehandle.announcer.Announcer;
import net.peacefulcraft.sco.gamehandle.regions.Region;
import net.peacefulcraft.sco.inventories.InventoryBase;
import net.peacefulcraft.sco.inventories.InventoryManager;
import net.peacefulcraft.sco.inventories.InventoryType;
import net.peacefulcraft.sco.quests.QuestBookManager;
import net.peacefulcraft.sco.storage.PlayerDataManager;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillManager;
import net.peacefulcraft.sco.swordskills.utilities.DirectionalUtil;
import net.peacefulcraft.sco.swordskills.utilities.IDamage;
import net.peacefulcraft.sco.swordskills.utilities.Modifier;

public class SCOPlayer implements SwordSkillCaster, IDamage
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

	/**Stores players critical damage chance*/
	private int criticalChance = 2;
		public int getCriticalChance() { return this.criticalChance; }
		public void setCriticalChance(int num) { this.criticalChance = num; }
		public void addCritical(int num) { this.criticalChance+=num; }

	private double criticalMultiplier = 1.2;
		public double getCriticalMultiplier() { return this.criticalMultiplier; }
		public void setCriticalMultiplier(double num) { this.criticalMultiplier = num; }

	private boolean adminOverride = false;
		public boolean hasOverride() { return this.adminOverride; }
		public void setAdminOverride(boolean bool) { this.adminOverride = bool; }

	private int parryChance = 0;
		public int getParryChance() { return this.parryChance; }
		public void setParryChance(int num) { this.parryChance = num; }

	private double parryMultiplier = 0.95D;
		public double getParryMultiplier() { return this.parryMultiplier; }
		public void setParryMultiplier(double num) { this.parryMultiplier = num; }

	/**Additional chance to increase item level on drop */
	private double bonusLevelMod = 0.0D;
		public double getLevelMod() { return this.bonusLevelMod; }
		public void setLevelMod(double d) { this.bonusLevelMod = d; }

	/**Additional chance to get more items on drop */
	private double bonusDropMod = 0.0D;
		public double getDropMod() { return this.bonusDropMod; }
		public void setDropMod(double d) { this.bonusDropMod = d; }

	/**Players exp multiplier */
	private double expMod = 1.0D;
		public double getExpMod() { return this.expMod; }
		public void  setExpMod(double d) { this.expMod = d; }

	/**Players current movement direction */
	private DirectionalUtil.Movement movement;
		public DirectionalUtil.Movement getMovement() { return this.movement; }
		public void setMovement(DirectionalUtil.Movement m) { this.movement = m; }

	/**Players damage modifiers*/
	private List<Modifier> damageModifiers;
		public List<Modifier> getDamageModifiers() { return Collections.unmodifiableList(this.damageModifiers); }
		public void setDamageModifiers(List<Modifier> l) { this.damageModifiers = l; }
		public void addDamageModifier(Modifier m) { this.damageModifiers.add(m); }
		public void removeDamageModifier(Modifier m) { this.damageModifiers.remove(m); }

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
	
	/**Determines if player is marked for a duel or not */
	private Boolean isInDuel = false;
		public Boolean isInDuel() { return this.isInDuel; }
		public void setInDueal(Boolean b) { this.isInDuel = b; }

	public SCOPlayer (UUID uuid) {
		this.uuid = uuid;
		playerKills = 0;
		floor = 0; //TODO: Load this from scopData
		
		scopData = new PlayerDataManager(this);

		this.damageModifiers = new ArrayList<Modifier>();
		
		swordSkillManager = new SwordSkillManager(this);
		questBookManager = new QuestBookManager(this);
		
		inventoryManager = new InventoryManager(this);
		inventoryManager.fetchInventory(InventoryType.SWORD_SKILL);
		inventoryManager.fetchInventory(InventoryType.QUEST_BOOK);

		//TODO: Remove this and replace with loading the wallet/bank from data
		this.wallet = 1000;
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

	/**Returns players attack damage attribute. */
	@Override
	public double getAttackDamage() { return getPlayer().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue(); }
	/**
	 * Sets players attack damage attribute.
	 * @param multiply if True attack damage is multiplied by given value. If False damage is set to value.
	 */
	@Override
	public void setAttackDamage(double mod, boolean multiply) { 
		if(multiply) {
			getPlayer().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(mod * getAttackDamage()); 
		} else {
			getPlayer().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(mod); 
		}
	}

	/**Returns players movement speed attribute */
	@Override
	public double getMovementSpeed() { return getPlayer().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue(); }
	/**
	 * Sets players movement speed attribute
	 * @param multiply if True movement speed is multiplied by given value. If False movement speed is set to value.
	 */
	@Override
	public void setMovementSpeed(double mod, boolean multiply) {
		if(multiply) {
			getPlayer().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(mod * getMovementSpeed());
		} else {
			getPlayer().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(mod);
		}
	}

	/**Returns players max health attribute */
	@Override
	public double getMaxHealth() { return getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue(); }
	/**
	 * Sets players max health attribute
	 * @param multiply if True max health is multiplied by given value. If False max health is set to value.
	 */
	@Override
	public void setMaxHealth(double mod, boolean multiply) {
		if(multiply) {
			getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(mod * getMaxHealth());
		} else {
			getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(mod);
		}
	}

	/**Returns players attack speed attribute */
	@Override
	public double getAttackSpeed() { return getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue(); }
	/**
	 * Sets players attack speed attribute
	 * @param multiply if True attack speed is multiplied by given value. If False attack speed is set to value.
	 */
	@Override
	public void setAttackSpeed(double mod, boolean multiply) {
		if(multiply) {
			getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(mod * getAttackSpeed());
		} else {
			getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(mod);
		}
	}
	
	/**Returns players knockback resistance attribute */
	@Override
	public double getKnockResist() { return getPlayer().getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).getBaseValue(); }
	/**
	 * Sets players Knockback resistance attribute
	 * @param multiply if True knockback resist is multiplied by given value. If False knockback resist is set to value.
	 */
	@Override
	public void setKnockResist(double mod, boolean multiply) {
		if(multiply) {
			getPlayer().getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(mod * getKnockResist());
		} else {
			getPlayer().getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(mod);
		}
	}

	/**Returns players armor attribute */
	@Override
	public double getArmor() { return getPlayer().getAttribute(Attribute.GENERIC_ARMOR).getBaseValue(); }
	/**
	 * Sets players armor attribute
	 * @param multiply if True armor is multiplied by given value. If False armor is set to value.
	 */
	@Override
	public void setArmor(double mod, boolean multiply) {
		if(multiply) {
			getPlayer().getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(mod * getKnockResist());
		} else {
			getPlayer().getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(mod);
		}
	}

	/**Returns players armor toughness attribute */
	@Override
	public double getArmorToughness() { return getPlayer().getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).getBaseValue(); }
	/**
	 * Sets players armor toughness attribute
	 * @param multiply if True armor toughness is multiplied by given value. If False armor toughness is set to value
	 */
	@Override
	public void setArmorToughness(double mod, boolean multiply) {
		if(multiply) {
			getPlayer().getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(mod * getKnockResist());
		} else {
			getPlayer().getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(mod);
		}
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
