package net.peacefulcraft.sco.swordskills.modules;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.SwordSkill;

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
    public void onSkillRegistered(SwordSkill ss) {/* No Equip Actions */}

    /**
     * Once the Skill signature has been matched, check if cooldown is
     * still in effect. If it is, return false and stop exection.  
     */
    @Override
    public boolean onSignatureMatch(SwordSkill ss, Event ev) {
        return isCoolingDown();
    }

    @Override
    public boolean onCanUseSkill(SwordSkill ss, Event ev) {
        // No actions to take, allow Skill execution to continue
        return true;
    }

    /**
     * After the skill has been triggered, add the cooldown delay to
     * prevent future Skill activation until such time that the cooldown
     * time has passed.
     */
    @Override
    public void onTrigger(SwordSkill ss, Event ev) {
        this.cooldownEnd += this.cooldownDelay;
    }

    @Override
    public void onUnregistration(SwordSkill ss) {/* No Dequip Actions */}
}