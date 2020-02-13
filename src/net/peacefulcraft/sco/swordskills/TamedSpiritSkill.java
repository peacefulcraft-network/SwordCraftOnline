package net.peacefulcraft.sco.swordskills;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

public class TamedSpiritSkill extends SwordSkill {

    private SCOPlayer s;
    private int increase;

    public TamedSpiritSkill(SCOPlayer s, long delay, SkillProvider provider, int increase) {
        super(s, delay, provider);

        this.s = s;
        this.increase = increase;
    }

    @Override
    public boolean skillSignature(Event ev) {
        return true;
    }

    @Override
    public void triggerSkill(Event ev) {
        s.setCriticalChance(s.getCriticalChance() + increase);
    }

    @Override
    public boolean canUseSkill() {
        //Player must have pet active
        if(s.isPetActive()) { return true; }
        return false;
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