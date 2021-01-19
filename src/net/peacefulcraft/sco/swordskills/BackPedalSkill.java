package net.peacefulcraft.sco.swordskills;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class BackPedalSkill extends SwordSkill {

    private Double vectorMultiplier;

    public BackPedalSkill(SwordSkillCaster c, Double vectorMultiplier, SwordSkillProvider provider) {
        super(c, provider);
        this.vectorMultiplier = vectorMultiplier;

        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
        this.listenFor(SwordSkillTrigger.ENTITY_DAMAGE_ENTITY_RECIEVE);
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
        if(ev instanceof EntityDamageByEntityEvent) {
            ModifierUser mu = (ModifierUser)c;
            mu.getLivingEntity().setVelocity(
                mu.getLivingEntity().getLocation().getDirection()
                    .multiply(-1 * vectorMultiplier).add(new Vector(0, 0.5, 0))
            );
        }
    }

    @Override
    public void skillUsed() {
        // No need to mark
    }

    @Override
    public void unregisterSkill() {

    }
    
}
