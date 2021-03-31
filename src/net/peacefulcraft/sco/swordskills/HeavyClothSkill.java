package net.peacefulcraft.sco.swordskills;

import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class HeavyClothSkill extends SwordSkill {

    private int armorModifier;
    private UUID change1;
    private UUID change2;

    public HeavyClothSkill(SwordSkillCaster c, int armorModifier, SwordSkillProvider provider) {
        super(c, provider);
        this.armorModifier = armorModifier;

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
            Attribute.GENERIC_ARMOR, 
            2 + armorModifier, 
            -1);
        change2 = mu.queueChange(
            Attribute.GENERIC_ARMOR_TOUGHNESS, 
            2 + armorModifier, 
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
