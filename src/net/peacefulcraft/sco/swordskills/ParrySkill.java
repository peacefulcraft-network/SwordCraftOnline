package net.peacefulcraft.sco.swordskills;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser.CombatModifier;

public class ParrySkill extends SwordSkill {

    private Integer increase;
    private Long delay;
    private SwordSkillProvider provider;

    public ParrySkill(SwordSkillCaster c, Integer increase, Long delay, SwordSkillProvider provider) {
        super(c, provider);
        this.increase = increase;
        this.delay = delay;

        this.listenFor(SwordSkillTrigger.PASSIVE);
        this.useModule(new TimedCooldown(delay));
    }

    @Override
    public boolean skillSignature(Event ev) {
        //Skill only requires being equipped
        return true;        
    }

    @Override
    public boolean skillPreconditions(Event ev) {
        //No extra checks required
        return true;
    }

    @Override
    public void triggerSkill(Event ev) {
        if(this.c instanceof ModifierUser) {
            ModifierUser mu = (ModifierUser)c;
            mu.addToCombatModifier(CombatModifier.PARRY_CHANCE, this.increase, -1);
        }
    }

    @Override
    public void skillUsed() {
        //No need to mark
    }

    @Override
    public void unregisterSkill() {
        if(this.c instanceof ModifierUser) {
            ModifierUser mu = (ModifierUser)c;
            mu.addToCombatModifier(CombatModifier.PARRY_CHANCE, -this.increase, -1);
        }
    }
    
}