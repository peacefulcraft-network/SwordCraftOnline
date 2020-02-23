package net.peacefulcraft.sco.swordskills.modules;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

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

    public TimedCooldown(long cooldonwDelay) {
        this.cooldownDelay = cooldonwDelay;
    }

    public TimedCooldown(long cooldownDelay, String cooldonwMessage) {
        this.cooldownDelay = cooldownDelay;
        this.cooldownMessage = cooldonwMessage;
    }

    @Override
    public void onModuleRegistered(SwordSkill ss) {}
    /* No extra event listeners needed */

    /**
     * If skill is cooling down, don't bother doing any work
     */
    @Override
    public boolean beforeSupportLifecycle(SwordSkillType type, SwordSkill ss, Event ev) {
        return isCoolingDown();
    }
    /* No pre-support lifecycle hooks */

    @Override
    public void executeSupportLifecycle(SwordSkillType type, SwordSkill ss, Event ev) {}
    /* No support lifecycle steps needed */

    @Override
    public boolean beforeSkillSignature(SwordSkill ss, Event ev) { return true; }
    /* No pre-signature hook */

    @Override
    public boolean beforeSkillPreconditions(SwordSkill ss, Event ev) { return true; }
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
        this.cooldownEnd += this.cooldownDelay;
    }

    @Override
    public void afterSkillUsed(SwordSkill ss, Event ev) {}
    /* No post-execution hook */

    @Override
    public void onUnregistration(SwordSkill ss) {}
    /* No Dequip Actions */
}