package net.peacefulcraft.sco.swordskills.modules;

import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.swordskills.SwordSkill;

/**
 * TimedCombo
 */
public class TimedCombo extends BasicCombo {

    private long timeLimit;
    private boolean isCountingDown = false;

    private BukkitTask currentTimer;

    public TimedCombo(SwordSkillComboType comboType, double activationThreshold, long timeLimit) {
        super(comboType, activationThreshold);
        this.timeLimit = timeLimit;
    }

    @Override
    public boolean onCanUseSkill(SwordSkill ss, Event ev) {
        // Check for combo progression, will incriment accumulation as required
        if(executeComboLogic(ss, ev)) {

            // If progression happend and timer not running, start it
            if(!isCountingDown) {
               startObjectiveTimer();
           }

           // Check if activation threshold met and return t/f to trigger ability
           return hasMetActivationThreshold();
       }

       // Combo accumulation was cancled / failed
       resetObjectiveTimer();
       resetComboAccumulation();
       return false;
    }

    @Override
    public void onTrigger(SwordSkill ss, Event ev) {
        // Reset progress when skill is triggered
        super.onTrigger(ss, ev);
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