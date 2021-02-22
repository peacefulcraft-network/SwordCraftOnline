package net.peacefulcraft.sco.swordskills;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class SystemChainWitchDoctorSkill extends SwordSkill {

    public SystemChainWitchDoctorSkill(SwordSkillCaster c, SwordSkillProvider provider) {
        super(c, provider);
        
        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
        this.useModule(new Trigger(SwordSkillType.SWORD));
        this.useModule(new TimedCooldown(55000));
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

        int healthTotal = 0;
        for(Entity e : mu.getLivingEntity().getNearbyEntities(10, 10, 10)) {
            ModifierUser vic = ModifierUser.getModifierUser(e);
            if(vic == null) { continue; }

            vic.setHealth(vic.getHealth() - 20);
            healthTotal += 17;
        }
        mu.setHealth(mu.getHealth() + healthTotal);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
