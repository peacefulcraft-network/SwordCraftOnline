package net.peacefulcraft.sco.swordskills;

import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class SpeedOverrideSkill extends SwordSkill {

    private UUID atkSpeedChange;
    private UUID movSpeedChange;

    public SpeedOverrideSkill(SwordSkillCaster c, SwordSkillProvider provider) {
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

        atkSpeedChange = mu.queueChange(
            Attribute.GENERIC_ATTACK_SPEED, 
            4, 
            -1);
        movSpeedChange = mu.queueChange(
            Attribute.GENERIC_MOVEMENT_SPEED,
            3, 
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
