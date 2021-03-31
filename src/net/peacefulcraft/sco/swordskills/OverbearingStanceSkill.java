package net.peacefulcraft.sco.swordskills;

import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class OverbearingStanceSkill extends SwordSkill {

    private int knockbackModifier;
    private UUID change1;
    private UUID change2;

    public OverbearingStanceSkill(SwordSkillCaster c, int knockbackModifier, SwordSkillProvider provider) {
        super(c, provider);
        this.knockbackModifier = knockbackModifier;

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
        change1 = mu.queueChange(
            Attribute.GENERIC_KNOCKBACK_RESISTANCE,
            4 + knockbackModifier,
            -1);
        change2 = mu.queueChange(
            Attribute.GENERIC_MOVEMENT_SPEED, 
            -ModifierUser.getBaseGenericMovement(mu) * 0.25, 
            -1);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {
        ModifierUser mu = (ModifierUser)c;
        mu.dequeueChange(change1);
        mu.dequeueChange(change2);
    }
    
}
