package net.peacefulcraft.sco.swordskills;

import java.util.UUID;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser.CombatModifier;

public class ParrySkill extends SwordSkill {

    private Integer increase;
    private UUID change1;

    public ParrySkill(SwordSkillCaster c, Integer increase, Long delay, SwordSkillProvider provider) {
        super(c, provider);
        this.increase = increase;

        this.listenFor(SwordSkillTrigger.PASSIVE);
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

            double mult = mu.getMultiplier(ModifierType.PHYSICAL, false);

            change1 = mu.queueChange(
                CombatModifier.PARRY_CHANCE, 
                this.increase * mult, 
                -1
            );
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
            mu.dequeueChange(change1);
        }
    }
    
}