package net.peacefulcraft.sco.swordskills.modules;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

/**
 * Interface defining the hooks a class must implement to used by a SwordSkill as a module.
 */
public interface SwordSkillModule {

    /**
     * Called by the SwordSkill which this module has been registered too.
     * Passes an instnace of the SwordSkill so the module can register the appropriate
     * listeners with the SwordSkill and its Manager.
     * @param ss The associated SwordSkill instance.
     */
    public void onModuleRegistered(SwordSkill ss);

    /**
     * Called by the SwordSkill before teh SwordSkill's support lifecycle is executed.
     * Useful for canceling SwordSkill execution early to avoid unecessary work.
     * @param type The event type which triggered this execution cycle
     * @param ss The associated SwordSkill instance
     * @param ev The eventw hich triggered execution
     */
    public boolean beforeSupportLifecycle(SwordSkillType type, SwordSkill ss, Event ev);

    /**
     * Called by the sword skill for executing the module's support lifecycle.
     * This lifecycle is intended for handling 'support' events that the module
     * needs to know about in order to function, but that the mainline SwordSkill may
     * not need to listen for. These are usually reset events like recieving damge
     * resetting a combo
     * @param type The event type which triggered this execution cycle
     * @param ss The associated SwordSkill instance
     * @param ev The event which triggered exeuction
     */
    public void executeSupportLifecycle(SwordSkillType type, SwordSkill ss, Event ev);

    /**
     * Called by the SwordSkill before executing SwordSkill signature matching.
     * @param ss The associated SwordSkill instance
     * @param ev The event which triggered execution
     * @return True: Continue execution, false: skill execution should terminate
     */
    public boolean beforeSkillSignature(SwordSkill ss, Event ev);

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
     * Called after the SwordSkill has finished post-execution cleanup (markSkillUsed())
     * @param ss The associated SwordSKillInstance
     * @param ev The triggering event
     */
    public void afterSkillUsed(SwordSkill ss, Event ev);

    /**
     * Called afer the SwordSkill has been unregistered
     * from the Entities SwordSkillManager
     * @param ss The associated SwordSkill
     */
    public void onUnregistration(SwordSkill ss);
}