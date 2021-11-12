package net.peacefulcraft.sco.swordskills;

import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser.CombatModifier;

public class HighGearSkill extends SwordSkill {

    private int levelModifier;
    private UUID change1;
    private UUID change2;
    private UUID change3;

    public HighGearSkill(SwordSkillCaster c, int levelModifier, SwordSkillProvider provider) {
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

        double mult = mu.getMultiplier(ModifierType.EARTH, false);

        change1 = mu.queueChange(
            Attribute.GENERIC_ATTACK_DAMAGE, 
            (3 + (1 * levelModifier)) * mult, 
            -1);
        change2 = mu.queueChange(
            Attribute.GENERIC_MOVEMENT_SPEED, 
            ModifierUser.getBaseGenericMovement(mu) * (0.2 + (0.02 * levelModifier)) * mult, 
            -1);
        change3 = mu.queueChange(
            CombatModifier.PARRY_CHANCE, 
            (-0.2 - (0.02 * levelModifier)) * mult,
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
