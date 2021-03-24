package net.peacefulcraft.sco.swordskills;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;

import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.utilities.Pair;

public class ForwardLungeSkill extends SwordSkill {

    private Double increase;
    private ItemTier tier;

    public ForwardLungeSkill(SwordSkillCaster c, Double increase, Long delay, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        
        this.increase = increase;
        this.tier = tier;

        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT);
        this.useModule(new TimedCooldown(delay, (ModifierUser)c, "Forward Lunge", this.tier));
        this.useModule(new Trigger(SwordSkillType.SWORD));
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
        ModifierUser mu = (ModifierUser)c;

        LivingEntity p = mu.getLivingEntity();

        Vector v = new Vector(p.getVelocity().getX(), p.getVelocity().getY(), p.getVelocity().getZ());
        Vector forward = p.getLocation().getDirection().multiply(1.9);
        v.add(forward);

        p.setVelocity(v);

        Double amount = mu.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) * this.increase;
        mu.queueChange(
            Attribute.GENERIC_ATTACK_DAMAGE, 
            amount, 
            2);
        SkillAnnouncer.messageSkill(
            mu,  
            new Pair<String, Double>(Attribute.GENERIC_ATTACK_DAMAGE.toString(), amount),
            "Forward Lunge",
            tier);
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