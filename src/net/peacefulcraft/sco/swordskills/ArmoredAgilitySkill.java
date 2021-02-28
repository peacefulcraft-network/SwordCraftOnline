package net.peacefulcraft.sco.swordskills;

import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class ArmoredAgilitySkill extends SwordSkill {

    private int levelModifier;
    private UUID speedChange;
    private UUID armorChange;

    public ArmoredAgilitySkill(SwordSkillCaster c, int levelModifier, SwordSkillProvider provider) {
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
        ModifierUser mu = (ModifierUser)c;

        SwordCraftOnline.logDebug("Player data static: " + mu.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
            + ", " + mu.getAttribute(Attribute.GENERIC_ARMOR));
        speedChange = mu.queueChange(
            Attribute.GENERIC_MOVEMENT_SPEED, 
            2 + levelModifier, 
            -1);
        armorChange = mu.queueChange(
            Attribute.GENERIC_ARMOR, 
            2 + levelModifier, 
            -1);
        SwordCraftOnline.logDebug("Player data modified: " + mu.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
            + ", " + mu.getAttribute(Attribute.GENERIC_ARMOR));
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {
        ModifierUser mu = (ModifierUser)c;

        mu.dequeueChange(speedChange);
        mu.dequeueChange(armorChange);
    }
    
}
