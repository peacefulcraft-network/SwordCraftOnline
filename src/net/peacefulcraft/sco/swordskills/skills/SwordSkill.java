package net.peacefulcraft.sco.swordskills.skills;

import org.bukkit.event.Event;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

public abstract class SwordSkill
{
	
	protected SCOPlayer s;
		public SCOPlayer getSCOPlayer() { return s; }
	/**Time allowed between activation */
	private long cooldowndelay;
		
	/**Next time ability can be used*/
	private long cooldown = 0L;
		public long getCooldown() { return cooldown; }
		public long getCooldownSeconds() { return cooldown / 1000;}
		public void setCooldown(Long delay) { cooldown = delay; }	
	
	private String name;
		public String getName() { return name; }
		public void setName(String s) { name = s; }

	protected int level = 0;
		public final int getLevel() { return level; }

	protected final int MAX_LEVEL = 5;
		public int getMaxLevel() { return MAX_LEVEL; }

	/**
	 * Set the cooldown time, in ms, between clicks
	 * @param delay
	 */
	public SwordSkill(SCOPlayer s, long delay, String name) {
		this.s = s;
		cooldowndelay = delay;
		this.name = name;
	}
	
	public boolean canUseSkill() {
		if(canUseSkillSilent()) {
			return true;
		}
		BaseComponent base = new TextComponent(skillCooldownMessage((cooldown - System.currentTimeMillis())/1000));
		base.setColor(ChatColor.RED);
		s.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, base);
		
		return false;
	}
	
	/**
	 * Check if skill is on cooldown without alerting player
	 * @return boolean can use
	 */
	public boolean canUseSkillSilent() {
		if(cooldown < System.currentTimeMillis()) {
			return true;
		}
		return false;
	}
	
	/*
	 * Trigger cooldown for skill
	 */
	public void markSkillUsed() {
		cooldown = System.currentTimeMillis() + cooldowndelay;
	}
	
	/**
	 * @param sourceEvent
	 */
	public void execute(Event ev) {
		markSkillUsed();
		triggerSkill(ev);
	}
	
	public String skillCooldownMessage(long timeRemaining) {
		return getClass() + " is on cooldown for " + timeRemaining;
	}
	
	/**
	 * Game conditions that must be met to trigger.
	 * @param Event
	 * @return boolean if can trigger
	 */
	public abstract boolean skillSignature(Event ev);
	
	/**
	 * Executes skill actions
	 * @param ev
	 */
	public abstract void triggerSkill(Event ev);

	/**Returns true if player can level up skill. */
	protected boolean canIncreaseLevel(SCOPlayer p, int targetLevel) {
		return ((level + 1) == targetLevel && targetLevel <= getMaxLevel());
	}
	
} 
