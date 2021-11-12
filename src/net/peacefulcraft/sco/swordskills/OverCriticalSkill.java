package net.peacefulcraft.sco.swordskills;

import java.util.UUID;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser.CombatModifier;

public class OverCriticalSkill extends SwordSkill {

    private double criticalModifier;
    private UUID change1;

    public OverCriticalSkill(SwordSkillCaster c, double criticalModifier, SwordSkillProvider provider) {
        super(c, provider);
        
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

        double mult = mu.getMultiplier(new ModifierType[] { ModifierType.LIGHTNING, ModifierType.MENTAL}, false);

        change1 = mu.queueChange(
            CombatModifier.CRITICAL_CHANCE, 
            (0.2 + criticalModifier) * mult, 
            -1);
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
