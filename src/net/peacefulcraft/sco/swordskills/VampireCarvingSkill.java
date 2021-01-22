package net.peacefulcraft.sco.swordskills;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class VampireCarvingSkill extends SwordSkill {

    private double healAdd;

    public VampireCarvingSkill(SwordSkillCaster c, Double modifier, SwordSkillProvider provider) {
        super(c, provider);

        this.healAdd = modifier;
       
        this.listenFor(SwordSkillTrigger.PASSIVE);
        this.listenFor(SwordSkillTrigger.ENTITY_DAMAGE_ENTITY_GIVE);
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
        EntityDamageByEntityEvent evv = (EntityDamageByEntityEvent)ev;
        ModifierUser mu = (ModifierUser)c;
        
        int addHealth = evv.getDamage() * healAdd < 1 ? 1 : (int) (evv.getDamage() * healAdd);
        mu.setHealth(mu.getHealth() + addHealth);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
