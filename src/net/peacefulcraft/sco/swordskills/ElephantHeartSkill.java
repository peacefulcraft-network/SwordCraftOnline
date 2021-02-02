package net.peacefulcraft.sco.swordskills;

import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class ElephantHeartSkill extends SwordSkill {

    private int maxHealth;
    private UUID change1;
    private UUID change2;

    public ElephantHeartSkill(SwordSkillCaster c, SwordSkillProvider provider) {
        super(c, provider);
        
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
        if(c instanceof ModifierUser) {
            ModifierUser mu = (ModifierUser)c;
            this.maxHealth = mu.getMaxHealth();
            change1 = mu.queueChange((int) (this.maxHealth * 0.5), -1);
            change2 = mu.queueChange(Attribute.GENERIC_MOVEMENT_SPEED, -2, -1);
        }
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {
        if(c instanceof ModifierUser) {
            ModifierUser mu = (ModifierUser)c;

            mu.dequeueChange(change1);
            mu.dequeueChange(change2);
        }        
    }
    
}
