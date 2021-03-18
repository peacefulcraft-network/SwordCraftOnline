package net.peacefulcraft.sco.swordskills;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.util.Vector;

import net.peacefulcraft.sco.items.ForwardLungeItem;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;

public class ForwardLungeSkill extends SwordSkill {

    private Double increase;
    private Long delay;

    public ForwardLungeSkill(SwordSkillCaster c, Double increase, Long delay, ForwardLungeItem provider) {
        super(c, provider);
        
        this.increase = increase;
        this.delay = delay;

        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT);
        this.useModule(new TimedCooldown(provider, delay));
    }

    @Override
    public boolean skillSignature(Event ev) {
        //TODO: Check item in hand
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
        Player p = s.getPlayer();

        Vector v = new Vector(p.getVelocity().getX(), p.getVelocity().getY(), p.getVelocity().getZ());
        Vector forward = p.getLocation().getDirection().multiply(1.6);
        v.add(forward);

        p.setVelocity(v);

        s.multiplyAttribute(Attribute.GENERIC_ATTACK_DAMAGE, this.increase, 2);
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