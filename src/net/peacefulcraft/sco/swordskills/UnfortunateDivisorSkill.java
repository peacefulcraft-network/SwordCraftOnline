package net.peacefulcraft.sco.swordskills;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class UnfortunateDivisorSkill extends SwordSkill {

    private ItemTier tier;

    public UnfortunateDivisorSkill(SwordSkillCaster c, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.tier = tier;
        
        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
        this.listenFor(SwordSkillTrigger.ENTITY_DAMAGE_ENTITY_GIVE);

        this.useModule(new TimedCooldown(30000, (ModifierUser)c, "Unfortunate Divisor", tier, null, "PlayerInteractEvent"));
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
        if(ev instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent evv = (EntityDamageByEntityEvent)ev;
            Entity vic = evv.getEntity();

            ModifierUser mu = ModifierUser.getModifierUser(vic);
            if(mu == null) { return; }

            mu.convertHealth(
                mu.getHealth() / 2,
                true);
            SkillAnnouncer.messageSkill(
                mu, 
                "Health Divided.", 
                "Unfortunate Divisor", 
                tier);
        }
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
