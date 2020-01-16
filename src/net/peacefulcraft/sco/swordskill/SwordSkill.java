package net.peacefulcraft.sco.swordskill;

import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

public abstract class SwordSkill
{
	
	/**
	 * General ability properties
	 */
	protected SCOPlayer s;
		public SCOPlayer getSCOPlayer() { return s; }
		
	protected SkillProvider provider;
		public SkillProvider getProvider() { return provider; }
	
		
	/**
	 * Ability cooldown properties
	 */
	/**Time allowed between activation */
	private long cooldownDelay;
	/**Next time ability can be used*/
	private long cooldown = 0L;
		public long getCooldown() { return cooldown; }
		public long getCooldownSeconds() { return cooldown / 1000;}
		public void setCooldown(Long delay) { cooldown = delay; }	
		public void triggerCooldown() { this.cooldown = System.currentTimeMillis() + cooldownDelay; }

	/**
	 * Set the cooldown time, in ms, between clicks
	 * @param delay
	 */
	public SwordSkill(SCOPlayer s, long cooldownDelay, SkillProvider provider) {
		this.s = s;
		this.cooldownDelay = cooldownDelay;
		this.provider = provider;
	}
	
	/**
	 * Called when the SwordSkill is being unregistered
	 */
	public void destroy() {
		if(isUsingTimer && isCountingDown) { resetObjectiveTimer(); }
	}
	
	/**
	 * @return True, cooldown in effect. False, not in effect.
	 */
	public boolean isCoolingDown() {
		// Cooldown greater than now, we're still cooling down.
		// Cooldown less than now, cooldown time has passed.
		return cooldown > System.currentTimeMillis();
	}
	
	public String skillCooldownMessage(long timeRemaining) {
		return getClass() + " is on cooldown for " + timeRemaining;
	}
	
	/**
	 * Physical conditions required for skill to activate.
	 * IE: Items being held, player location. NOT combo logic or timers
	 * @param Event
	 * @return boolean if can trigger
	 */
	public abstract boolean skillSignature(Event ev);
	
	/**
	 * Any conditions that must be met for the ability to activate.
	 * Cooldown and combo threshold / timer are checked automatically.
	 * If this function is being called, that means the ability is not cooling down
	 * and the player has reached any requisite combos to trigger the ability.
	 * @return boolean can use
	 */
	public abstract boolean canUseSkill();
	
	/**
	 * Executes skill actions
	 * @param ev
	 */
	public abstract void triggerSkill(Event ev);
	
	/*
	 * Any cleanup required once the skill is activated.
	 * Any applicable cooldowns & timers are automatically handled by SwordSkillManager
	 * Also a good place to handle things like adding exhaustion
	 */
	public abstract void markSkillUsed();

	
/**************************************************************************
 * 
 * 
 * Ability Combo
 * 
 * 
 **************************************************************************/
	
	private double comboAccumulation = 0;
	private double comboActivationThreshold;
	private boolean isTrackingCombos;
		public boolean isTrackingCombos() { return isTrackingCombos; }
	private SwordSkillComboType comboType;	
		public SwordSkillComboType getComboType() { return comboType; }
	
	/**
	 * Method for setting up combo tracking if another SwordSkill constructor was used to instantiate
	 * @param activationThreshold: Value that must be reached for the ability to trigger
	 * @param thresholdType: What action(s) need to be tracked for this cobmo
	 */
	public void setupComboTracking(SwordSkillComboType comboType, double activationThreshold) {
		this.comboType = comboType;
		this.comboActivationThreshold = activationThreshold;
		isTrackingCombos = true;
	}
	
	/**
	 * Accumulate progress towards activating the combo ability
	 * @param value: Amount of (X) to add towards the combo (IE, 1 more hit scored)
	 */
	public void comboAccumulate(double value) {
		comboAccumulation += value;
	}
	
	/**
	 * Reset progress towards activating the combo
	 */
	public void comboResetAccumulation() {
		comboAccumulation = 0.0;
	}
	
	/**
	 * Check if the combo is ready to be activated ( necessary combo has been achieved )
	 * @return
	 */
	public boolean isComboThresholdMet() {
		return comboAccumulation > comboActivationThreshold;
	}
	
	/**
	 * Don't run combo logic during sword skill ability loop
	 */
	public void comboDisableTracking() {
		this.isTrackingCombos = false;
	}
	
	/**
	 * Run combo logic ( do track ) during the sword skill loop
	 */
	public void comboEnableTracking() {
		this.isTrackingCombos = true;
	}
	
/**************************************************************************
 * 
 * 
 * Combo Timer. Player must acheive combo before time expires and the combo resets
 * 
 * 
 **************************************************************************/

	/**
	 * Ability threshold accumulation timer limit
	 */
	private boolean isUsingTimer = false;
		public boolean isUsingTimer() { return isUsingTimer; }
		public void enableTimer() { isUsingTimer = true; }
		public void disableTimer() { isUsingTimer= false; }
	private long timerTime = 0L;
	private boolean isCountingDown = false;
	private BukkitTask currentTimer;
	
	public void setupComboTimer(long timerTime) {
		this.timerTime = timerTime;
		isUsingTimer = true;
	}
	
	/**
	 * @return True / False the timer is currently running.
	 */
	public boolean isCountingDown() { return isCountingDown; }
	
	/**
	 * Set the time the player has to complete the ability objective,
	 * as defined in hasAcheivedTriggerObjective()
	 * @param time
	 */
	public void setTimerTime(long timerTime) { this.timerTime = timerTime; }
	
	/**
	 * Start the timer. Once the specified amount of time has passed, 
	 * This will reset the combo counter for this ability.
	 * @param overrideCooldown: True, starts time regardless of cooldown.
	 * 							False, will do nothing if cooldown is in effect.
	 */
	@SuppressWarnings("static-access")
	public void startObjectiveTimer() {
		if(isCountingDown) {
			throw new IllegalStateException("Attempted to start objective timer while objective timer was already running. "
										  + "Call resetObjectiveTimer first.");
		}
		
		isCountingDown = true;
		currentTimer = new TimerBoundSkillTimer().runTaskLater(
			SwordCraftOnline.getPluginInstance().getPluginInstance(),  
			timerTime
		);
	}
	
	/**
	 * Called to reset the objective timer. This will not reset objective progress.
	 * Example; If a player needs to achieve 5 consecutive hits without taking damage,
	 * 			but each hit must occur within 3 seconds of the previous hit, set the
	 * 			Timer to 3 seconds and call resetObjectiveTimer() after each hit. 
	 * resetObjectiveTimer() does not start the timer again. startObjectiveTimer() must
	 * be called to start the timer again.
	 */
	public void resetObjectiveTimer() {
		currentTimer.cancel();
		isCountingDown = false;
	}

	private class TimerBoundSkillTimer extends BukkitRunnable{

		@Override
		public void run() {
			comboResetAccumulation();
			isCountingDown = false;
		}
		
	}
} 
