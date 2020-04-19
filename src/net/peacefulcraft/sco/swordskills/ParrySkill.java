package net.peacefulcraft.sco.swordskills;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;

public class ParrySkill extends SwordSkill {

    private SCOPlayer s;
    private int increase;
    private long delay;

    public ParrySkill(SCOPlayer s, long delay, SkillProvider p, int increase) {
        super(s, p);

        this.s = s;
        this.increase = increase;
        this.delay = delay;

        this.listenFor(SwordSkillType.PASSIVE);
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
        if(this.c instanceof SCOPlayer) {
            SCOPlayer s = (SCOPlayer)c;
            s.setParryChance(s.getCriticalChance() + this.increase);
        }
    }

    @Override
    public void skillUsed() {
        //No need to mark
    }

    @Override
    public void unregisterSkill() {
        if(this.c instanceof SCOPlayer) {
            SCOPlayer s = (SCOPlayer)c;
            s.setParryChance(s.getParryChance() - this.increase);
        }
    }
    
}