package net.peacefulcraft.sco.swordskills;

import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser.CombatModifier;

public class QuickHandsSkill extends SwordSkill {

    private int levelModifier;
    private UUID atkSpeedChange;
    private UUID ccChange;

    public QuickHandsSkill(SwordSkillCaster c, int levelModifier, SwordSkillProvider provider) {
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

        atkSpeedChange = mu.queueChange(
            Attribute.GENERIC_ATTACK_SPEED, 
            2 + levelModifier, 
            -1);
        ccChange = mu.queueChange(
            CombatModifier.CRITICAL_CHANCE, 
            -0.2 - (0.05 * levelModifier), 
            -1);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {
        ModifierUser mu = (ModifierUser)c;
        mu.dequeueChange(atkSpeedChange);
        mu.dequeueChange(ccChange);
    }
    
}
