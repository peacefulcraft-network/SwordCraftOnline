package net.peacefulcraft.sco.swordskills;

import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;

public class YouthfulIdiocracySkill extends SwordSkill {

    private int levelModifier;
    private UUID healthChange;
    private UUID speedChange;
    private UUID armorChange;

    public YouthfulIdiocracySkill(SwordSkillCaster c, int levelModifier, SwordSkillProvider provider) {
        super(c, provider);
        
        this.levelModifier = levelModifier;
        
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

        healthChange = mu.queueChange(
            (int)(mu.getHealth() * (0.2 + (0.05 * this.levelModifier) * mult)), 
            -1);
        speedChange = mu.queueChange(
            Attribute.GENERIC_MOVEMENT_SPEED, 
            ModifierUser.getBaseGenericMovement(mu) * (0.05 + (0.02 * this.levelModifier) * mult), 
            -1);
        armorChange = mu.queueChange(
            Attribute.GENERIC_ARMOR, 
            (-1 - this.levelModifier) * mult, 
            -1);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {
        ModifierUser mu = (ModifierUser)c;

        mu.dequeueChange(healthChange);
        mu.dequeueChange(speedChange);
        mu.dequeueChange(armorChange);
    }
    
}
