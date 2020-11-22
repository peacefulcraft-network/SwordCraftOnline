package net.peacefulcraft.sco.swordskills;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser.CombatModifier;

/**
 * Skill increases player critical hit chance by level.
 */
public class SerratedBladeSkill extends SwordSkill {

    private SCOPlayer s;
    private long delay;
    private int increase;

    public SerratedBladeSkill(SCOPlayer s, long delay, SkillProvider provider, int increase) {
        super(s, provider);

        this.s = s;
        this.delay = delay;
        this.increase = increase;

        this.listenFor(SwordSkillType.PASSIVE);
        this.useModule(new TimedCooldown(delay));
    }

    @Override
    public boolean skillSignature(Event ev) {
        //Skill only requires being equipped.
        return true;
    }

    @Override
    public void triggerSkill(Event ev) {
        s.addCombatModifier(CombatModifier.CRITICAL_CHANCE, this.increase, -1);
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
        s.addCombatModifier(CombatModifier.CRITICAL_CHANCE, -this.increase, -1);
    }
}