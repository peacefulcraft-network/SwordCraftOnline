package net.peacefulcraft.sco.swordskills;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
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
		
	protected SwordSkillProvider provider;
		public final SwordSkillProvider getProvider() { return provider; }

	protected SwordSkillManager manager;
		public final SwordSkillManager getSwordSkillManager() { return manager; }

	// List of event loops which the SkillLogic is listening on
	private final ArrayList<SwordSkillTrigger> primaryListeners = new ArrayList<SwordSkillTrigger>();

	// List of modules on which lifecycle hooks need triggered
	private final ArrayList<SwordSkillModule> modules = new ArrayList<SwordSkillModule>();

	// Map of event loops and the corisponding SwordSkillModules which need to run when those loops are triggered
	private final HashMap<SwordSkillTrigger, ArrayList<SwordSkillModule>> supportListeners = new HashMap<SwordSkillTrigger, ArrayList<SwordSkillModule>>();

	public SwordSkill(SwordSkillCaster c, SwordSkillProvider provider) {
		this.c = c;
		LivingEntity caster = c.getSwordSkillManager().getSCOPlayer().getPlayer();
		if (caster instanceof Player) {
			this.s = GameManager.findSCOPlayer((Player) caster);
		}
		this.manager = c.getSwordSkillManager();
		this.provider = provider;
	}

	/**
	 * Register a module seeking to be notified of SwordSkill lifecyle events
	 * @param module The module to notify
	 */
	public final void useModule(SwordSkillModule module) {
		if(modules.contains(module)) {
			SwordCraftOnline.logWarning("Attempted to register module " + module.getClass().getSimpleName() + " more than once.");
			return;
		}

		modules.add(module);
	}

	/**
	 * Unregisters a module and its listeners from the SwordSkill and the SwordSkillManager.
	 * @param module The module to unregister
	 */
	public final void unregisterModule(SwordSkillModule module) {
		// TODO: Implement
	}

	/**
	 * Used by SwordSkills to register their primary listeners
	 * @param type The event loop to listen on
	 */
	protected final void listenFor(SwordSkillTrigger type) {
		primaryListeners.add(type);
		this.manager.registerListener(type, this);
	}

	/**
	 * Called by SwordSkill modules to inform the SwordSkill and its manager that a module
	 * needs to be notified of actions on the given event domain (type).
	 * @param type The event loop to listen on
	 * @param module The module which needs to be notified
	 */
	public final void listenFor(SwordSkillTrigger type, SwordSkillModule module) {
		if (primaryListeners.contains(type)) {
			/*
			 * A warning to indicate a module has registered itself to receive events for the same 
			 * event type that the primary sword skill logic listens on.
			 * This is not bad, but can lead to strange behavior as a 
			 * result of execution order. The SwordSkill support 
			 * lifecycle (modules) execute before primary skill logic.
			 */
			SwordCraftOnline.logWarning("Registered module " + module.getClass().getSimpleName() + " onto loop " + type + " for support lifecycle which primary loop. Can module hooks be used instead?");
		}

		ArrayList<SwordSkillModule> listeners = supportListeners.get(type);
		if(listeners == null) {
			listeners = new ArrayList<SwordSkillModule>();
			supportListeners.put(type, listeners);

			// Tell SwordSkillManager we need to know about these events
			manager.registerListener(type, this);
		}

		listeners.add(module);
	}

	public final List<SwordSkillModule> getModules() {
		ArrayList<SwordSkillModule> modules = new ArrayList<SwordSkillModule>();
		for(SwordSkillTrigger type : supportListeners.keySet()) {
			modules.addAll((Collection) supportListeners.get(type));
		}

		return Collections.unmodifiableList(modules);
	}

	/**
	 * Notify modules listening on event loop type of an applicable event which has occured
	 * @param type The event loop on which the even has occured
	 * @param ev The triggering event
	 */
	public final void execSkillSupportLifecycle(SwordSkillTrigger type, Event ev) {
		ArrayList<SwordSkillModule> listeners = supportListeners.get(type);
		if(listeners == null) {
			return;
		}

		for(SwordSkillModule module : listeners) {
			module.executeSupportLifecycle(type, this, ev);
		}
	}

	/**
	 * Execute SwordSkill logic and interface with supporting modules through life cycle hooks
	 * @param type The event loop on which the event occured
	 * @param ev The triggering event
	 */
	public final void execPrimaryLifecycle(SwordSkillTrigger type, Event ev) {
		if(primaryListeners.contains(type)) {

			// Check if this skill needs to know about this event
			if(!this.skillSignature(ev)) { return; }
			
			// Notify modules signature has matched and preconditions are about to be checked
			for(SwordSkillModule module : modules) {
				if(!module.beforeSkillPreconditions(this, ev)) {
					return;
				}
			}

			// Check all skill preconditions before triggering the skill
			if(!this.skillPreconditions(ev)) { return;}

			// Notify modules preconditions have passed and skill is about to be triggered
			for(SwordSkillModule module : modules) {
				if(!module.beforeTriggerSkill(this, ev)) {
					return;
				}
			}
			
			// Preconditions passed, trigger the skill and notify support modules
			this.triggerSkill(ev);

			// Notify modules of skill trigger
			for(SwordSkillModule module : modules) {
				module.afterTriggerSkill(this, ev);
			}
			
			// Skill execution complete, 
			skillUsed();
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
		for(SwordSkillTrigger type : supportListeners.keySet()) {
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
