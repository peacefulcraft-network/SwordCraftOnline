package net.peacefulcraft.sco.swordskills;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class SystemChainWitchDoctorSkill extends SwordSkill {

    private ItemTier tier;

    public SystemChainWitchDoctorSkill(SwordSkillCaster c, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.tier = tier;
        
        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
        this.useModule(new Trigger(SwordSkillType.SWORD, (ModifierUser)c));
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

            vic.convertHealth(15, true);
            healthTotal += 12;

            SkillAnnouncer.messageSkill(
                mu, 
                "Health drained by 15!", 
                "System Chain: Witch Doctor", 
                tier);
        }
        mu.convertHealth(-healthTotal, true);
        SkillAnnouncer.messageSkill(
            mu, 
            "Healed by " + healthTotal + "health", 
            "System Chain: Witch Doctor", 
            tier);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
