package net.peacefulcraft.sco.swordskills;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.utilities.ModifierUser.CombatModifier;

/**
 * Skill increases player critical hit chance by level.
 */
public class SerratedBladeSkill extends SwordSkill {

    private SwordSkillCaster c;
    private int increase;

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
        s.addToCombatModifier(CombatModifier.CRITICAL_CHANCE, this.increase, -1);
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
        s.addToCombatModifier(CombatModifier.CRITICAL_CHANCE, -this.increase, -1);
    }
}