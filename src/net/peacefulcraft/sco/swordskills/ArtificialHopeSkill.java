package net.peacefulcraft.sco.swordskills;

import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class ArtificialHopeSkill extends SwordSkill {

    private double valueModifier;
    private UUID change1;
    private UUID change2;
    private UUID change3;

    public ArtificialHopeSkill(SwordSkillCaster c, double valueModifier, SwordSkillProvider provider) {
        super(c, provider);
        this.valueModifier = valueModifier;

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
            Attribute.GENERIC_ATTACK_DAMAGE,
            1 + (0.2 + valueModifier), 
            -1);
        change2 = mu.queueChange(
            Attribute.GENERIC_MOVEMENT_SPEED, 
            ModifierUser.getBaseGenericMovement(mu) * (0.12 + valueModifier), 
            -1);
        change3 = mu.queueChange(
            Attribute.GENERIC_ARMOR, 
            -2, 
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
        mu.dequeueChange(change3);
    }
    
}
