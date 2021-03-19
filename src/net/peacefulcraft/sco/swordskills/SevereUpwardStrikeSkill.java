package net.peacefulcraft.sco.swordskills;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;

public class SevereUpwardStrikeSkill extends SwordSkill {

    private int cooldown;
    private double vectorMultiplier;

    public SevereUpwardStrikeSkill(SwordSkillCaster c, double vectorMultiplier, int cooldown, SwordSkillProvider provider) {
        super(c, provider);
        this.vectorMultiplier = vectorMultiplier;
        this.cooldown = cooldown;

        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
        this.listenFor(SwordSkillTrigger.ENTITY_DAMAGE_ENTITY_GIVE);

        this.useModule(new Trigger(SwordSkillType.SWORD));
        this.useModule(new TimedCooldown(this.cooldown, null, "PlayerInteractEvent"));
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
            EntityDamageByEntityEvent evv = (EntityDamageByEntityEvent)ev;
            if(evv.getEntity() instanceof LivingEntity) {
                LivingEntity liv = (LivingEntity)evv.getEntity();
    
                liv.setVelocity(
                    liv.getLocation().getDirection()
                        .multiply(-1 * (2 + this.vectorMultiplier)).add(new Vector(0, 5, 0))
                );
            }
        }
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
