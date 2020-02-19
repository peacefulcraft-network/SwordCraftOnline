package net.peacefulcraft.sco.swordskills;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.swordskills.modules.SwordSkillModule;

public abstract class SwordSkill {
	
	/**
	 * General ability properties
	 */
	protected SCOPlayer s;
		public final SCOPlayer getSCOPlayer() { return s; }

	protected SwordSkillCaster c;
		public final SwordSkillCaster getSwordSkillCaster() { return c; }
		
	protected SkillProvider provider;
		public final SkillProvider getProvider() { return provider; }
	
	protected ArrayList<SwordSkillModule> modules;
		public List<SwordSkillModule> getRegisteredModules() { return Collections.unmodifiableList(modules); }

	public SwordSkill(SwordSkillCaster c, SkillProvider provider) {
		this.c = c;
		this.provider = provider;
	}
	
	public final void useModule(SwordSkillModule module) {
		modules.add(module);
	}

	/**
	 * Method is called by SwordSkillManager to notify the SwordSkill's modules
	 * that the SwordSkill has been registered with the entities SwordSkillManager.
	 */
	public final void execSkillRegistration() {
		for(SwordSkillModule module : modules) {
			module.onSkillRegistered(this);
		}
	}



	/**
	 * Method is called by SwordSkillManager to check skill signature match
	 * and run any registered module hooks.
	 * @param ev The triggering event
	 * @return True/false can use ability
	 */
	public final boolean execSkillSignature(Event ev) {
		if(!this.skillSignature(ev)) { return false; }
		
		for(SwordSkillModule module : modules) {
			if(!module.onSignatureMatch(this, ev)) {
				return false;
			}
		}

		return true;
	}
	/**
	 * This should contain logic to check whether a Player is trying to activate one skill or another.
	 * Things like check item names, player location, etc. Once it is known that the player is definently
	 * trying to use this skill, return true and handle combo / timer thresholds in canUseSkill().
	 * @param Event The triggering event
	 * @return true/false player is trying to trigger this skill
	 */
	public abstract boolean skillSignature(Event ev);
	
	

	/**
	 * Method is called by SwordSkillManager to check if player
	 * can use the skill now that it has matched the signature
	 * @param ev The triggering event
	 * @return True/false can use ability
	 */
	public final boolean execCanUseSkill(Event ev) {
		if(!this.canUseSkill(ev)) { return false;}

		for(SwordSkillModule module : modules) {
			if(!module.onCanUseSkill(this, ev)) {
				return false;
			}
		}

		return true;
	}
	/**
	 * Any conditions that must be met for the ability to activate.
	 * @param ev The triggering event
	 * @return True/false can use ability
	 */
	public abstract boolean canUseSkill(Event ev);
	


	/**
	 * Methid is called by SwordSkillManager to trigger
	 * the SwordSkill's effect(s).
	 * @param ev The triggering event
	 */
	public final void execTriggerSkill(Event ev) {
		this.triggerSkill(ev);

		for(SwordSkillModule module : modules) {
			module.onTrigger(this, ev);
		}
	}
	/**
	 * Executes skill actions
	 * @param ev The triggering event
	 */
	public abstract void triggerSkill(Event ev);
	


	/*
	 * Any cleanup required once the skill is activated.
	 * Any applicable cooldowns & timers are automatically handled by SwordSkillManager
	 * Also a good place to handle things like adding exhaustion
	 */
	public abstract void markSkillUsed();



	/**
	 * Method is called by SwordSkillManager to notify the
	 * SwordSkill and its modules that they've been unregistered
	 */
	public final void execSkillUnregistration() {
		this.unregisterSkill();

		for(SwordSkillModule module : modules) {
			module.onUnregistration(this);
		}
	}
	/**
	 * Used in unregistering passive effects.
	 * Applicable to player data changes, potion effects, etc.
	 */
	public abstract void unregisterSkill();
} 
