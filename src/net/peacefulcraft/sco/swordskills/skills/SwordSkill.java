package net.peacefulcraft.sco.swordskills.skills;

import org.bukkit.event.Event;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

public abstract class SwordSkill
{
	
	//Time between allowed clicks
	protected SCOPlayer s;
		public SCOPlayer getSCOPlayer() { return s; }
	private long cooldowndelay;
		
		//Next time ability can be used
	private long cooldown = 0L;
		public long getCooldown() { return cooldown; }
		public long getCooldownSeconds() { return cooldown / 1000;}
		public void setCooldown(Long delay) { cooldown = delay; }
	
	/**
	 * Set the cooldown time, in ms, between clicks
	 * @param delay
	 */
	public SwordSkill(SCOPlayer s, long delay) {
		this.s = s;
		cooldowndelay = delay;
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
	
} 
