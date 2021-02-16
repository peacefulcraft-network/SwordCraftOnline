package net.peacefulcraft.sco.swordskills.modules;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillTrigger;

/**
 * TimedCooldown
 */
public class TimedCooldown implements SwordSkillModule {

    private long cooldownDelay;
        private void setCooldownDelay(long delay) { this.cooldownDelay = delay; }
    
    private long cooldownEnd = 0L;
        private boolean isCoolingDown() { return this.cooldownEnd > System.currentTimeMillis(); }
        private long getTimeRemaining() { return this.cooldownEnd - System.currentTimeMillis(); }
        private long getCooldownSeconds() { return this.getTimeRemaining() / 1000; }
    
    private String cooldownMessage;
        private String getCooldownMessage() { return this.cooldownMessage; }

    private String eventName = null;

    public TimedCooldown(long cooldonwDelay) {
        this.cooldownDelay = cooldonwDelay;
    }

    public TimedCooldown(long cooldownDelay, String cooldonwMessage) {
        this.cooldownDelay = cooldownDelay;
        this.cooldownMessage = cooldonwMessage;
    }

    /**
     * Modified constructor to detect specific events
     * that trigger timer
     * @param eventName of Event
     */
    public TimedCooldown(long cooldownDelay, String cooldownMessage, String eventName) {
        this.cooldownDelay = cooldownDelay;
        this.cooldownMessage = cooldownMessage;
        this.eventName = eventName;
    }

    @Override
    public void executeSupportLifecycle(SwordSkillTrigger type, SwordSkill ss, Event ev) {}
    /* No support life cycle steps needed */

    @Override
    public boolean beforeSkillPreconditions(SwordSkill ss, Event ev) { return !isCoolingDown(); }
    /* No preconditions hook */

    @Override
    public boolean beforeTriggerSkill(SwordSkill ss, Event ev) { return true; }
    /* No pre-trigger hook */

    /**
     * After the skill has been triggered, add the cooldown delay to
     * prevent future Skill activation until such time that the cooldown
     * time has passed.
     */
    @Override
    public void afterTriggerSkill(SwordSkill ss, Event ev) {
        if(this.eventName == null || (this.eventName != null && ev.getEventName().equalsIgnoreCase(this.eventName))) {
            this.cooldownEnd = System.currentTimeMillis() + cooldownDelay;
        } 
    }

    @Override
    public void onUnregistration(SwordSkill ss) {}
    /* No Dequip Actions */
}