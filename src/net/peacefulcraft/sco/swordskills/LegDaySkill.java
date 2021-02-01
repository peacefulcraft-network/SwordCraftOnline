package net.peacefulcraft.sco.swordskills;

import org.bukkit.attribute.Attribute;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class LegDaySkill extends SwordSkill {

    private double movementModifier;

    public LegDaySkill(SwordSkillCaster c, double movementModifier, SwordSkillProvider provider) {
        super(c, provider);
        this.movementModifier = movementModifier;

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
        mu.addToAttribute(
            Attribute.GENERIC_MOVEMENT_SPEED, 
            ModifierUser.getBaseGenericMovement(mu) * (0.2 + movementModifier), 
            -1);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {
        ModifierUser mu = (ModifierUser)c;
        mu.addToAttribute(
            Attribute.GENERIC_MOVEMENT_SPEED, 
            -(ModifierUser.getBaseGenericMovement(mu) * (0.2 + movementModifier)), 
            -1);
    }
    
}
