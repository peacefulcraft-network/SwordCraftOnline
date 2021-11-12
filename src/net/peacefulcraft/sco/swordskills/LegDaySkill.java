package net.peacefulcraft.sco.swordskills;

import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;

public class LegDaySkill extends SwordSkill {

    private double movementModifier;
    private UUID change1;

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

        double mult = mu.getMultiplier(ModifierType.PHYSICAL, false);

        mu.queueChange(
            Attribute.GENERIC_MOVEMENT_SPEED, 
            ModifierUser.getBaseGenericMovement(mu) * (0.2 + movementModifier) * mult, 
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
