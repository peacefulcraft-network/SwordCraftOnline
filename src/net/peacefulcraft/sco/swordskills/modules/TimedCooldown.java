package net.peacefulcraft.sco.swordskills.modules;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillTrigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

/**
 * TimedCooldown
 */
public class TimedCooldown implements SwordSkillModule {

    private long cooldownDelay;
    
    private long cooldownEnd = 0L;
        private boolean isCoolingDown() { return this.cooldownEnd > System.currentTimeMillis(); }
        private long getTimeRemaining() { return this.cooldownEnd - System.currentTimeMillis(); }
        private long getCooldownSeconds() { return this.getTimeRemaining() / 1000; }

    private String eventName = null;
    private String deniedEvent = null;

    private ModifierUser mu;
    private String skillName;
    private ItemTier tier;

    /**
     * Basic Constructor
     * @param cooldownDelay
     */
    public TimedCooldown(long cooldownDelay) {
        this(cooldownDelay, null, "", ItemTier.COMMON);
    }

    /**
     * Constructor for ModifierUser wielding primary or secondary
     * 
     * @param cooldonwDelay
     * @param mu
     * @param type
     */
    public TimedCooldown(long cooldonwDelay, ModifierUser mu, String skillName, ItemTier tier) {
        this.cooldownDelay = cooldonwDelay;
        this.mu = mu;
        this.skillName = skillName;
        this.tier = tier;
    }

    /**
     * Modified constructor to detect specific events
     * that trigger timer
     * @param eventName of Event
     * @param deniedEvent Events we want to stop from altering cooldown
     */
    public TimedCooldown(long cooldownDelay, ModifierUser mu, String skillName, ItemTier tier, String eventName, String deniedEvent) {
        this.cooldownDelay = cooldownDelay;
        this.mu = mu;

        this.eventName = eventName;
        this.deniedEvent = deniedEvent;
        this.skillName = skillName;
        this.tier = tier;
    }

    @Override
    public void executeSupportLifecycle(SwordSkillTrigger type, SwordSkill ss, Event ev) {}
    /* No support life cycle steps needed */

    @Override
    public boolean beforeSkillPreconditions(SwordSkill ss, Event ev) { 
        if(!isCoolingDown()) {
            return true;
        }

        // Skill is cooling down. Send message
        if(mu != null && mu instanceof SCOPlayer) {
            SCOPlayer s = (SCOPlayer)mu;

            SkillAnnouncer.messageSkillCooldown(
                s, 
                skillName, 
                tier, 
                getCooldownSeconds());
        }

        return false;
    }
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
        if(( this.eventName != null && ev.getEventName().equalsIgnoreCase(this.eventName) )
            || ( this.deniedEvent != null && !ev.getEventName().equalsIgnoreCase(this.deniedEvent) )
            || ( this.deniedEvent == null && this.eventName == null )) {
            
            this.cooldownEnd = System.currentTimeMillis() + cooldownDelay;
        } 
    }

    @Override
    public void onUnregistration(SwordSkill ss) {}
    /* No Dequip Actions */


}