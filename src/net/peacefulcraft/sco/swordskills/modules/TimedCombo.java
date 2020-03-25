package net.peacefulcraft.sco.swordskills.modules;

import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

/**
 * TimedCombo
 * This class relys on many default behaviors from BasicCombo
 */
public class TimedCombo extends BasicCombo {

    private long timeLimit;
    private boolean isCountingDown = false;

    private BukkitTask currentTimer;

    public TimedCombo(SwordSkill ss, SwordSkillComboType comboType, double activationThreshold, long timeLimit) {
        super(ss, comboType, activationThreshold);
        this.timeLimit = timeLimit;
    }

    @Override
    public void executeSupportLifecycle(SwordSkillType type, SwordSkill ss, Event ev) {
        resetComboAccumulation();
        resetObjectiveTimer();
    }

    @Override
    public boolean beforeSkillPreconditions(SwordSkill ss, Event ev) {
        executeComboAccumulation(ev);

        if(isCountingDown) {
            return hasMetActivationThreshold();
        }

        startObjectiveTimer();
        return false;
    }

    @Override
    public void afterTriggerSkill(SwordSkill ss, Event ev) {
        // Reset progress when skill is triggered
        super.afterTriggerSkill(ss, ev);
        resetComboAccumulation();
        resetObjectiveTimer();
    }

    @Override
    public void onUnregistration(SwordSkill ss) {
        super.onUnregistration(ss);
        resetObjectiveTimer();
    }

    /**
	 * Start the timer. Once the specified amount of time has passed, 
	 * This will reset the combo counter for this ability.
	 */
    public void startObjectiveTimer() {
        if(isCountingDown) {
			throw new IllegalStateException("Attempted to start objective timer while objective timer was already running. "
										  + "Call resetObjectiveTimer first.");
        }
        
        isCountingDown = true;
        currentTimer = new TimedComboTimer().runTaskLater(
            SwordCraftOnline.getPluginInstance(), 
            timeLimit
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
        if(!currentTimer.isCancelled()) {
            currentTimer.cancel();
        }

        isCountingDown = false;
    }

    private class TimedComboTimer extends BukkitRunnable {

        @Override
        public void run() {
            isCountingDown = false;
            resetComboAccumulation();
        }
    }
}