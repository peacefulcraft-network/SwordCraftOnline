package net.peacefulcraft.sco.swordskills;

import org.bukkit.attribute.Attribute;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class ElephantHeartSkill extends SwordSkill {

    private int maxHealth;
    private double speed;

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
            mu.setMaxHealth((int) (this.maxHealth * 1.5), -1);

            this.speed = mu.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
            mu.multiplyAttribute(Attribute.GENERIC_MOVEMENT_SPEED, 0.8, -1);
        }
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {
        if(c instanceof ModifierUser) {
            ModifierUser mu = (ModifierUser)c;

            mu.setMaxHealth(this.maxHealth, -1);
            mu.setAttribute(Attribute.GENERIC_MOVEMENT_SPEED, this.speed, -1);
        }        
    }
    
}
