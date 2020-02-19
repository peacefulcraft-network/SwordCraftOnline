package net.peacefulcraft.sco.swordskills;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;

/**
 * Skill increases player critical hit chance by level.
 */
public class SerratedBladeSkill extends SwordSkill {

    private SCOPlayer s;
    private int increase;

    public SerratedBladeSkill(SCOPlayer s, long delay, SkillProvider provider, int increase) {
        super(s, provider);
        useModule(new TimedCooldown(delay));

        this.s = s;
        this.increase = increase;
    }

    @Override
    public boolean skillSignature(Event ev) {
        //Skill only requires being equipped.
        return true;
    }

    @Override
    public void triggerSkill(Event ev) {
        s.setCriticalChance(s.getCriticalChance() + increase);
    }

    @Override
    public boolean canUseSkill(Event ev) {
        //No extra checks required
        return true;
    }

    @Override
    public void markSkillUsed() {
        //No need to mark, passive.
    }

    @Override
    public void unregisterSkill() {
        s.setCriticalChance(s.getCriticalChance() - increase);
    }
}