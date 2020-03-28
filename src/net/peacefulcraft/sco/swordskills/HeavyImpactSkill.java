package net.peacefulcraft.sco.swordskills;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.particles.Effect;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;

public class HeavyImpactSkill extends SwordSkill {

    private long delay;
    private SwordSkillCaster c;
    private int damage;

    public HeavyImpactSkill(SwordSkillCaster c, long delay, SkillProvider provider, int damage) {
        super(c, provider);
        this.delay = delay;
        this.c = c;
        this.damage = damage;

        this.listenFor(SwordSkillType.PLAYER_INTERACT);
        this.listenFor(SwordSkillType.ENTITY_DAMAGE_ENTITY_RECIEVE);
        this.useModule(new TimedCooldown(delay));
    }

    @Override
    public boolean skillSignature(Event ev) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean skillPreconditions(Event ev) {
        EntityInteractEvent e = (EntityInteractEvent)ev;
        if(!(e.getEntity().isOnGround())) { return false; }
        return true;
    }

    @Override
    public void triggerSkill(Event ev) {
        if(ev instanceof EntityDamageEvent) {
            EntityDamageEvent e = (EntityDamageEvent)ev;
            if(e.getCause().equals(DamageCause.FALL)) {
                for(Entity temp: e.getEntity().getNearbyEntities(3, 3, 3)) {
                    LivingEntity asLiving = (LivingEntity)temp;
                    //If health goes below zero we just kill them.
                    if(asLiving.getHealth() - damage < 0) {
                        asLiving.setHealth(0);
                        continue;
                    } 
                    //Playing custom bleeding effect.
                    Effect effect = SwordCraftOnline.getEffectManager().getEffectByClassName("Bleed");
                    effect.setEntity(temp);
                    effect.iterations = 1;
                    effect.start();
                    //Damaging entity;
                    asLiving.setHealth(asLiving.getHealth() - damage);
                }
            }
            return;
        }
        LivingEntity e = c.getSwordSkillManager().getLivingEntity();

        Vector v = new Vector(e.getVelocity().getX(), e.getVelocity().getY(), e.getVelocity().getZ());
        Vector up = e.getLocation().getDirection().multiply(0.05).setY(1);
        v.add(up);

        e.setVelocity(v);
    }

    @Override
    public void skillUsed() {
        
    }

    @Override
    public void unregisterSkill() {
        
    }
    
}