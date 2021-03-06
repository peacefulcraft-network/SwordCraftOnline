package net.peacefulcraft.sco.swordskills.modules;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillTrigger;

/**
 * Interface defining the hooks a class must implement to used by a SwordSkill as a module.
 */
public interface SwordSkillModule {

    /**
     * Called by the sword skill for executing the module's support life cycle.
     * This life cycle is intended for handling 'support' events that the module
     * needs to know about in order to function, but that the mainline SwordSkill may
     * not need to listen for. These are usually reset events like recieving damge
     * resetting a combo
     * @param type The event type which triggered this execution cycle
     * @param ss The associated SwordSkill instance
     * @param ev The event which triggered exeuction
     */
    public void executeSupportLifecycle(SwordSkillTrigger type, SwordSkill ss, Event ev);

    /**
     * Called after the skill signature has been matched
     * @param ss The associated SwordSkill instance
     * @param ev Triggering event
     * @return Should skill trigger execution continue? 
     *         True to continue exection, false to terminate execution.
     */
    public boolean beforeSkillPreconditions(SwordSkill ss, Event ev);

    /**
     * Called after the canUseSkill method has returned true
     * @param ss The Associated SwordSkill instance
     * @param ev Triggering event
     * @return Should skill trigger execution continue?
     *         True to continue execution, false to terminate execution.
     */
    public boolean beforeTriggerSkill(SwordSkill ss, Event ev);

    /**
     * Called after the SwordSkill has been triggered
     * @param ss The associated SwordSKill instance
     * @param ev The triggering event
     */
    public void afterTriggerSkill(SwordSkill ss, Event ev);

    /**
     * Called afer the SwordSkill has been unregistered
     * from the Entities SwordSkillManager
     * @param ss The associated SwordSkill
     */
    public void onUnregistration(SwordSkill ss);
}