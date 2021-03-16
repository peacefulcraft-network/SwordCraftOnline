package net.peacefulcraft.sco.swordskills;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.util.Vector;

import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class ForwardLungeSkill extends SwordSkill {

    private Double increase;

    public ForwardLungeSkill(SwordSkillCaster c, Double increase, Long delay, SwordSkillProvider provider) {
        super(c, provider);
        
        this.increase = increase;

        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT);
        this.useModule(new TimedCooldown(delay));
        this.useModule(new Trigger(SwordSkillType.SWORD));
    }

    @Override
    public boolean skillSignature(Event ev) {
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
        ModifierUser mu = (ModifierUser)c;

        LivingEntity p = mu.getLivingEntity();

        Vector v = new Vector(p.getVelocity().getX(), p.getVelocity().getY(), p.getVelocity().getZ());
        Vector forward = p.getLocation().getDirection().multiply(1.6);
        v.add(forward);

        p.setVelocity(v);
        mu.queueChange(Attribute.GENERIC_ATTACK_DAMAGE, mu.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) * this.increase, 2);
    }

    @Override
    public void skillUsed() {
        return;
    }

    @Override
    public void unregisterSkill() {
        return;
    }
    
}