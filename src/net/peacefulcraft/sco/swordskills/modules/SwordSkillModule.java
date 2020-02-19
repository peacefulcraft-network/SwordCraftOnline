package net.peacefulcraft.sco.swordskills.modules;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.SwordSkill;

/**
 * Interface defining the hooks a class must implement to used by a SwordSkill as a module.
 */
public interface SwordSkillModule {

    /**
     * Called after the skill has been registered with
     * the entities SwordSkillManager
     * @param ss The associated SwordSkill instance 
     */
    public void onSkillRegistered(SwordSkill ss);

    /**
     * Called after the skill signature has been matched
     * @param ss The associated SwordSkill instance
     * @param ev Triggering event
     * @return Should skill trigger execution continue? 
     *         True to continue exection, false to terminate execution.
     */
    public boolean onSignatureMatch(SwordSkill ss, Event ev);

    /**
     * Called after the canUseSkill method has returned true
     * @param ss The Associated SwordSkill instance
     * @param ev Triggering event
     * @return Should skill trigger execution continue?
     *         True to continue execution, false to terminate execution.
     */
    public boolean onCanUseSkill(SwordSkill ss, Event ev);

    /**
     * Called after the SwordSkill has been triggered
     * @param ss The associated SwordSKill instance
     * @param ev Triggering event
     */
    public void onTrigger(SwordSkill ss, Event ev);

    /**
     * Called afer the SwordSkill has been unregistered
     * from the Entities SwordSkillManager
     * @param ss The associated SwordSkill
     */
    public void onUnregistration(SwordSkill ss);
}