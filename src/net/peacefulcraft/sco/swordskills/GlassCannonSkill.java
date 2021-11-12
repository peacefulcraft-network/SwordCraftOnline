package net.peacefulcraft.sco.swordskills;

import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;

public class GlassCannonSkill extends SwordSkill {

    private int levelModifier;
    private UUID healthChange;
    private UUID damageChange;
    private UUID armorChange;

    public GlassCannonSkill(SwordSkillCaster c, int levelModifier, SwordSkillProvider provider) {
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

        double mult = mu.getMultiplier(ModifierType.ICE, false);

        damageChange = mu.queueChange(
            Attribute.GENERIC_ATTACK_DAMAGE, 
            (4 + levelModifier) * mult, 
            -1);
        healthChange = mu.queueChange(
            (int)(mu.getMaxHealth() * (0.3 + (0.05 * this.levelModifier)) * mult), 
            -1);
        armorChange = mu.queueChange(
            Attribute.GENERIC_ARMOR, 
            (-1 - levelModifier) * mult, 
            -1);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {
        ModifierUser mu = (ModifierUser)c;
        mu.dequeueChange(damageChange);
        mu.dequeueChange(healthChange);
        mu.dequeueChange(armorChange);
    }
    
}
