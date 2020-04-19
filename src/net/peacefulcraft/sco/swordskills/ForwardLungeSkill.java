package net.peacefulcraft.sco.swordskills;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.util.Vector;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;

public class ForwardLungeSkill extends SwordSkill {

    private SCOPlayer s;
    private double increase;
    private long delay;

    public ForwardLungeSkill(SCOPlayer s, long delay, SkillProvider p, double increase) {
        super(s, p);
        
        this.s = s;
        this.increase = increase;
        this.delay = delay;

        this.listenFor(SwordSkillType.PLAYER_INTERACT);
        this.useModule(new TimedCooldown(delay));
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

        double base = s.getAttackDamage();
        s.setAttackDamage(this.increase, true);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), new Runnable() {
            public void run() {
                s.setAttackDamage(base, false);
            }
        }, 40);
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