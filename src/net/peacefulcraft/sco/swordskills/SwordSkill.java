package net.peacefulcraft.sco.swordskills;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.event.Event;

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

	// List of event loops which the SkillLogic is listening on
	private ArrayList<SwordSkillType> primaryListeners;

	// Map of event loops and the corisponding SwordSkillModules which need to run when those loops are triggered
	private HashMap<SwordSkillType, ArrayList<SwordSkillModule>> supportListeners;

	public SwordSkill(SwordSkillCaster c, SkillProvider provider) {
		this.c = c;
		this.provider = provider;
	}
	
	public final void useModule(SwordSkillModule module) {
		module.onModuleRegistered(this);
	}

	/**
	 * Called by SwordSkill modules to inform the SwordSkill and its manager that a module
	 * needs to be notified of actions on the given event domain (type).
	 * @param type The event loop to listen on
	 * @param module The module which needs to be notified
	 */
	public final void listenFor(SwordSkillType type, SwordSkillModule module) {
		ArrayList<SwordSkillModule> listeners = supportListeners.get(type);
		if(listeners == null) {
			listeners = new ArrayList<SwordSkillModule>();
			supportListeners.put(type, listeners);

			// Tell SwordSkillManager we need to know about these events
			c.getSwordSkillManager().registerListener(type, this);
		}

		listeners.add(module);
	}

	/**
	 * Unregisters a module and its listeners from the SwordSkill and the SwordSkillManager.
	 * @param module The module to unregister
	 */
	public final void unregisterModule(SwordSkillModule module) {
		for(SwordSkillType type : supportListeners.keySet()) {
			ArrayList<SwordSkillModule> listeners = supportListeners.get(type);
			listeners.remove(module);

			// Prune this event loop if there are no more listeners on it
			if(listeners.size() == 0) {
				supportListeners.remove(type);
				c.getSwordSkillManager().unregisterListener(type, this);
			}
		}
	}

	/**
	 * Notify modules listening on event loop type of an applicable event which has occured
	 * @param type The event loop on which the even has occured
	 * @param ev The triggering event
	 */
	public final void execSkillSupportLifecycle(SwordSkillType type, Event ev) {
		ArrayList<SwordSkillModule> listeners = supportListeners.get(type);
		if(listeners == null) {
			return;
		}

		for(SwordSkillModule module : listeners) {
			if(module.beforeSupportLifecycle(type, this, ev)) {
				module.executeSupportLifecycle(type, this, ev);
			}
		}
	}

	/**
	 * Execute SwordSkill logic and interface with supporting modules through life cycle hooks
	 * @param type The event loop on which the event occured
	 * @param ev The triggering event
	 */
	public final void execPrimaryLifecycle(SwordSkillType type, Event ev) {
		if(primaryListeners.contains(type)) {
			ArrayList<SwordSkillModule> hooks = supportListeners.get(type);

			// Notify modules skill execution is about to begin
			for(SwordSkillModule module : hooks) {
				module.beforeSkillSignature(this, ev);
			}

			// Check if this skill needs to know about this event
			if(!this.skillSignature(ev)) { return; }
			
			// Notify modules signature has matched and preconditions are about to be checked
			for(SwordSkillModule module : hooks) {
				if(!module.beforeSkillPreconditions(this, ev)) {
					return;
				}
			}
			
			// Check all skill preconditions before triggering the skill
			if(!this.skillPreconditions(ev)) { return;}

			// Notify modules preconditions have passed and skill is about to be triggered
			for(SwordSkillModule module : hooks) {
				if(!module.beforeTriggerSkill(this, ev)) {
					return;
				}
			}
			
			// Preconditions passed, trigger the skill and notify support modules
			this.triggerSkill(ev);

			// Notify modules of skill trigger
			for(SwordSkillModule module : hooks) {
				module.afterTriggerSkill(this, ev);
			}
			
			// Skill execution complete, 
			skillUsed();

			// Notify modules skill execution has concluded
			for(SwordSkillModule module : hooks) {
				module.afterSkillUsed(this, ev);
			}
		}
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
	 * Any conditions that must be met for the ability to activate.
	 * @param ev The triggering event
	 * @return True/false can use ability
	 */
	public abstract boolean skillPreconditions(Event ev);
	
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
	public abstract void skillUsed();

	/**
	 * Method is called by SwordSkillManager to notify the
	 * SwordSkill and its modules that they've been unregistered
	 */
	public final void execSkillUnregistration() {
		for(SwordSkillType type : supportListeners.keySet()) {
			for(SwordSkillModule module : supportListeners.get(type)) {
				module.onUnregistration(this);
			}
		}

		this.unregisterSkill();
	}
	/**
	 * Used in unregistering passive effects.
	 * Applicable to player data changes, potion effects, etc.
	 */
	public abstract void unregisterSkill();
} 
