package net.peacefulcraft.sco.swordskills;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class ResolutionWindSkill extends SwordSkill {

    private double vectorModifier;

    public ResolutionWindSkill(SwordSkillCaster c, double vectorModifier, SwordSkillProvider provider) {
        super(c, provider);
        
        this.vectorModifier = vectorModifier;
        this.useModule(new Trigger(SwordSkillType.SECONDARY));
        this.useModule(new TimedCooldown(15000));
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
        Location center = mu.getLivingEntity().getLocation();

        for(Entity e : mu.getLivingEntity().getNearbyEntities(5, 5, 5)) {
            if(e instanceof LivingEntity) {
                LivingEntity liv = (LivingEntity)e;
                Location t = liv.getLocation().subtract(center);
                double distance = liv.getLocation().distance(center);

                t.getDirection().normalize().multiply(-1);
                t.multiply(distance / 1.2);
                t.multiply(2 + vectorModifier);

                liv.setVelocity(t.toVector());
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
