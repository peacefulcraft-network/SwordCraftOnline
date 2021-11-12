package net.peacefulcraft.sco.swordskills;

import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;

public class PristineGrindstoneSkill extends SwordSkill {

    private Double increase;
    private UUID change1;

    public PristineGrindstoneSkill(SwordSkillCaster c, Double increase, SwordSkillProvider provider) {
        super(c, provider);
        this.increase = increase;

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
        if(this.c instanceof ModifierUser) {
            ModifierUser mu = (ModifierUser)c;
            double mult = mu.getMultiplier(ModifierType.EARTH, false);

            change1 = mu.queueChange(
                Attribute.GENERIC_ATTACK_DAMAGE, 
                this.increase * mult, 
                -1
            );
        }
    }

    @Override
    public void skillUsed() {
        // No need to mark
    }

    @Override
    public void unregisterSkill() {
        if(this.c instanceof ModifierUser) {
            ModifierUser mu = (ModifierUser)c;
            mu.dequeueChange(change1);
        }
    }
    
}
