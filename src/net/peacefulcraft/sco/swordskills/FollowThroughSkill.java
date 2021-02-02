package net.peacefulcraft.sco.swordskills;

import java.util.UUID;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser.CombatModifier;

public class FollowThroughSkill extends SwordSkill {

    private double critModifier;
    private UUID change1;

    public FollowThroughSkill(SwordSkillCaster c, double critModifier, SwordSkillProvider provider) {
        super(c, provider);
        this.critModifier = critModifier;
        
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
        ModifierUser mu = (ModifierUser)c;
        change1 = mu.queueChange(CombatModifier.CRITICAL_MULTIPLIER, 0.5 + critModifier, -1);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {
        ModifierUser mu = (ModifierUser)c;
        mu.dequeueChange(change1);
    }
    
}
