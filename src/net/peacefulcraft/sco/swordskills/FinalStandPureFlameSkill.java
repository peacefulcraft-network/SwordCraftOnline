package net.peacefulcraft.sco.swordskills;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class FinalStandPureFlameSkill extends SwordSkill {

    private ItemTier tier;

    public FinalStandPureFlameSkill(SwordSkillCaster c, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.tier = tier;
        
        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
        this.useModule(new TimedCooldown(35000, (ModifierUser)c, "Final Stand: Pure Flame", tier));
        this.useModule(new Trigger(SwordSkillType.PRIMARY));
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
        
        for(Entity e : mu.getLivingEntity().getNearbyEntities(5, 5, 5)) {
            if(e instanceof LivingEntity) {
                LivingEntity liv = (LivingEntity)e;
                liv.setFireTicks(200);
            }
            ModifierUser vic = ModifierUser.getModifierUser(e);
            if(vic == null) { continue; }
            SkillAnnouncer.messageSkill(
                vic, 
                "Burn in the pure flame!", 
                "Final Stand: Pure Flame", 
                tier);
        }

        mu.queueChange(
            -(int)(mu.getHealth() * 0.2), 
            10);
        SkillAnnouncer.messageSkill(
            mu, 
            "Fizzled", 
            "Final Stand: Pure Flame", 
            tier);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
