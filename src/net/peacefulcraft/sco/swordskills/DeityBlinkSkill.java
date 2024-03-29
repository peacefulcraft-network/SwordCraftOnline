package net.peacefulcraft.sco.swordskills;

import org.bukkit.attribute.Attribute;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class DeityBlinkSkill extends SwordSkill {

    public DeityBlinkSkill(SwordSkillCaster c, SwordSkillProvider provider) {
        super(c, provider);
        
        this.listenFor(SwordSkillTrigger.ENTITY_TELEPORT);
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
        mu.queueChange(
            Attribute.GENERIC_MOVEMENT_SPEED, 
            mu.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) * 0.5,
            2);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
