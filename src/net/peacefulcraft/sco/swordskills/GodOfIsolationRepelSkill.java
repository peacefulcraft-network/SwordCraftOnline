package net.peacefulcraft.sco.swordskills;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class GodOfIsolationRepelSkill extends SwordSkill {

    public GodOfIsolationRepelSkill(SwordSkillCaster c, SwordSkillProvider provider) {
        super(c, provider);
        
        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
        this.useModule(new Trigger(SwordSkillType.PRIMARY));
        this.useModule(new TimedCooldown(20000));
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
                t.multiply(2);

                liv.setVelocity(t.toVector());

                liv.addPotionEffect(new PotionEffect(
                    PotionEffectType.BLINDNESS, 
                    100, 
                    2));
            }
        }
        mu.getLivingEntity().addPotionEffect(new PotionEffect(
            PotionEffectType.BLINDNESS,
            60,
            2));
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
