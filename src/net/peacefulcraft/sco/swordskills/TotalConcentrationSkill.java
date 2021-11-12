package net.peacefulcraft.sco.swordskills;

import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;

public class TotalConcentrationSkill extends SwordSkill {

    private int levelModifier;
    private UUID atkSpeedChange;
    private UUID movSpeedChange;

    public TotalConcentrationSkill(SwordSkillCaster c, int levelModifier, SwordSkillProvider provider) {
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

        double mult = mu.getMultiplier(ModifierType.MENTAL, false);

        atkSpeedChange = mu.queueChange(
            Attribute.GENERIC_ATTACK_SPEED, 
            (2 + levelModifier) * mult, 
            -1);
        movSpeedChange = mu.queueChange(
            Attribute.GENERIC_MOVEMENT_SPEED, 
            -ModifierUser.getBaseGenericMovement(mu) * 0.3 * mult, 
            -1);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {
        ModifierUser mu = (ModifierUser)c;
        mu.dequeueChange(atkSpeedChange);
        mu.dequeueChange(movSpeedChange);
    }
    
}
