package net.peacefulcraft.sco.swordskills.modules;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;

import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillTrigger;

/**
 * BasicCombo
 */
public class BasicCombo implements SwordSkillModule {
    private SwordSkillComboType comboType;
    private double activationThreshold;
        public boolean hasMetActivationThreshold() { return comboAccumulation > activationThreshold; }

    protected double comboAccumulation = 0.0;
        public void resetComboAccumulation() { this.comboAccumulation = 0.0; }
        public void comboAccumulate(double value) { comboAccumulation += value; }

    public BasicCombo(SwordSkill ss, SwordSkillComboType comboType, double activationThreshold) {
        this.comboType = comboType;
        this.activationThreshold = activationThreshold;

        // Register the combo to reset on damage receive events
        if(
            comboType == SwordSkillComboType.CONSECUTIVE_HITS_WITHOUT_TAKING_DAMAGE
            || comboType == SwordSkillComboType.CUMULATIVE_DAMAGE_WITHOUT_TAKING_DAMAGE
        ) {
            ss.listenFor(SwordSkillTrigger.ENTITY_DAMAGE_ENTITY_RECIEVE, this);
        }
    }

    /*
    
        Support Lifecycle Steps
    
    */
    @Override
    public void executeSupportLifecycle(SwordSkillTrigger type, SwordSkill ss, Event ev) {
        resetComboAccumulation();
    }

    /*

        Primary Lifecycle Steps

    */
    @Override
    public boolean beforeSkillPreconditions(SwordSkill ss, Event ev) {
        executeComboAccumulation(ev);
        return hasMetActivationThreshold();
    }

    @Override
    public boolean beforeTriggerSkill(SwordSkill ss, Event ev) { return true; }
    /* No pre-trigger hook */

    @Override
    public void afterTriggerSkill(SwordSkill ss, Event ev) {
        resetComboAccumulation();
    }

    @Override
    public void onUnregistration(SwordSkill ss) {}
    /* No Unequip Steps*/

    /*

        Internal util functions

    */
    /**
     * Accumulate combo progressed based on the combo type
     * @param ev The triggering event
     */
    protected void executeComboAccumulation(Event ev) {
        if(
            comboType == SwordSkillComboType.CONSECUTIVE_HITS_IGNORE_TAKEN_DAMAGE
            || comboType == SwordSkillComboType.CONSECUTIVE_HITS_WITHOUT_TAKING_DAMAGE
        ) {
            comboAccumulate(1.0);

        }else if(
            comboType == SwordSkillComboType.CUMULATIVE_DAMAGE_IGNORE_TAKEN_DAMAGE
            || comboType == SwordSkillComboType.CUMULATIVE_DAMAGE_WITHOUT_TAKING_DAMAGE
        ) {
            comboAccumulate(((EntityDamageEvent) ev).getDamage());
        }
    }

    public enum SwordSkillComboType {
        CONSECUTIVE_HITS_WITHOUT_TAKING_DAMAGE, CUMULATIVE_DAMAGE_WITHOUT_TAKING_DAMAGE,
        CONSECUTIVE_HITS_IGNORE_TAKEN_DAMAGE, CUMULATIVE_DAMAGE_IGNORE_TAKEN_DAMAGE
    }    
}