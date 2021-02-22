package net.peacefulcraft.sco.swordskills;

import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

public class RedDawnSkill extends SwordSkill {

    private int levelModifier;
    private UUID healthChange;

    public RedDawnSkill(SwordSkillCaster c, int levelModifier, SwordSkillProvider provider) {
        super(c, provider);
        this.levelModifier = levelModifier;

        this.listenFor(SwordSkillTrigger.PASSIVE);
    }

    @Override
    public boolean skillSignature(Event ev) {
        return true;
    }

    @Override
    public boolean skillPreconditions(Event ev) {
        return true;
    }

    @Override
    public void triggerSkill(Event ev) {
        SCOPlayer s = (SCOPlayer)c;

        healthChange = s.queueChange(
            (int)(s.getMaxHealth() * (1.2 + (0.1 * this.levelModifier) * s.getPlayerKills())), 
            -1);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {
        s.dequeueChange(healthChange);
    }
    
}
