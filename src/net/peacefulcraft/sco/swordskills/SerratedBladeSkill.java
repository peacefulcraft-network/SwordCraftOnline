package net.peacefulcraft.sco.swordskills;

import java.util.UUID;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser.CombatModifier;

/**
 * Skill increases player critical hit chance by level.
 */
public class SerratedBladeSkill extends SwordSkill {

    private SwordSkillCaster c;
    private int increase;
    private UUID change1;

    public SerratedBladeSkill(SwordSkillCaster c, SwordSkillProvider provider, int increase) {
        super(c, provider);

        this.c = c;
        this.increase = increase;

        this.listenFor(SwordSkillTrigger.PASSIVE);
    }

    @Override
    public boolean skillSignature(Event ev) {
        //Skill only requires being equipped.
        return true;
    }

    @Override
    public void triggerSkill(Event ev) {
        ModifierUser mu = (ModifierUser)c;
        change1 = mu.queueChange(
            CombatModifier.CRITICAL_CHANCE, 
            this.increase, 
            -1
        );       
    }

    @Override
    public boolean skillPreconditions(Event ev) {
        //No extra checks required
        return true;
    }

    @Override
    public void skillUsed() {
        //No need to mark, passive.
    }

    @Override
    public void unregisterSkill() {
        ModifierUser mu = (ModifierUser)c;
        mu.dequeueChange(change1);
    }
}