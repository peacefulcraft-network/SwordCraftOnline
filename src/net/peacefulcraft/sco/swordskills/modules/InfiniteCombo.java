package net.peacefulcraft.sco.swordskills.modules;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityInteractEvent;

import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class InfiniteCombo extends BasicCombo {

    private int health;
    private double damageModifier;
    private double damageIncrement;

    public InfiniteCombo(SwordSkill ss, double damageModifier, double damageIncrement) {
        super(ss, SwordSkillComboType.CONSECUTIVE_HITS_WITHOUT_TAKING_DAMAGE, 0);
        this.damageModifier = damageModifier;
        this.damageIncrement = damageIncrement;
    }

    @Override
    public boolean beforeTriggerSkill(SwordSkill ss, Event ev) {
        if(ev instanceof EntityInteractEvent) {
            health = ((ModifierUser)ss.getSwordSkillCaster()).getHealth();
        } else if(ev instanceof EntityDamageEvent) {
            EntityDamageEvent evv = (EntityDamageEvent)ev;
            if(health > ((ModifierUser)ss.getSwordSkillCaster()).getHealth()) {
                this.comboAccumulation = 0.0;
                return false;
            } else {
                executeComboAccumulation(ev);
                evv.setDamage(this.damageModifier + (this.damageIncrement * comboAccumulation));
            }
        }
        return true;
    }

    @Override
    public boolean beforeSkillPreconditions(SwordSkill ss, Event ev) {
        return this.comboAccumulation == 0.0 ? true : false;
    }
    
}
