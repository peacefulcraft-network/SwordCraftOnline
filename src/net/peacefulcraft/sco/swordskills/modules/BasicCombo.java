package net.peacefulcraft.sco.swordskills.modules;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import net.peacefulcraft.sco.swordskills.SwordSkill;

/**
 * BasicCombo
 */
public class BasicCombo implements SwordSkillModule {

    private SwordSkillComboType comboType;
    private double activationThreshold;
        public boolean hasMetActivationThreshold() { return comboAccumulation > activationThreshold; }

    private double comboAccumulation = 0.0;
        public void resetComboAccumulation() { this.comboAccumulation = 0.0; }
        public void comboAccumulate(double value) { comboAccumulation += value; }

    public BasicCombo(SwordSkillComboType comboType, double activationThreshold) {
        this.comboType = comboType;
        this.activationThreshold = activationThreshold;
    }

    @Override
    public void onSkillRegistered(SwordSkill ss) {/* No Equip Steps */}

    @Override
    public boolean onSignatureMatch(SwordSkill ss, Event ev) {
        // No signature steps
        return true;
    }

    @Override
    public boolean onCanUseSkill(SwordSkill ss, Event ev) {
        executeComboLogic(ss, ev);
        return hasMetActivationThreshold();
    }

    @Override
    public void onTrigger(SwordSkill ss, Event ev) {
        resetComboAccumulation();
    }

    @Override
    public void onUnregistration(SwordSkill ss) {/* No Unequip Steps*/}

    /**
     * Determines whether player progressed towards combo goal
     * or if combo failed and the count should be reset
     * @return True for progression, false for reset
     */
    protected boolean executeComboLogic(SwordSkill ss, Event ev) {
        // Player Hit / Damage Combos
        if(ev instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent ede = (EntityDamageByEntityEvent) ev;
            // If our player was the one dealing damage
            if(ede.getDamager() == ss.getSwordSkillCaster().getSwordSkillManager().getLivingEntity()){
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
            return true;
        
        // If our player was the one receiving damage
        } else if(ev instanceof EntityDamageEvent) {
            EntityDamageEvent ede = (EntityDamageEvent) ev;
            if(ede.getEntity() == ss.getSwordSkillCaster().getSwordSkillManager().getLivingEntity()) {
                if( comboType == SwordSkillComboType.CUMULATIVE_DAMAGE_WITHOUT_TAKING_DAMAGE
                    || comboType == SwordSkillComboType.CONSECUTIVE_HITS_WITHOUT_TAKING_DAMAGE
                ) {
                    resetComboAccumulation();
                } else {
                    // Don't care about received damage
                    return true;
                }
            }
        }

        return false;
    }

    public enum SwordSkillComboType {
        CONSECUTIVE_HITS_WITHOUT_TAKING_DAMAGE, CUMULATIVE_DAMAGE_WITHOUT_TAKING_DAMAGE,
        CONSECUTIVE_HITS_IGNORE_TAKEN_DAMAGE, CUMULATIVE_DAMAGE_IGNORE_TAKEN_DAMAGE
    }    
}