package net.peacefulcraft.sco.swordskills;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class FullCounterSkill extends SwordSkill {

    private ItemTier tier;

    public FullCounterSkill(SwordSkillCaster c, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.tier = tier;
        
        this.useModule(new Trigger(SwordSkillType.SWORD));
        this.useModule(new TimedCooldown(45000, (ModifierUser)c, "Full Counter", tier, null, "PlayerInteractEvent"));

        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
        this.listenFor(SwordSkillTrigger.ENTITY_DAMAGE_ENTITY_RECIEVE);
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
            double damage = evv.getDamage();
            evv.setCancelled(true);

            ModifierUser mu = ModifierUser.getModifierUser(evv.getDamager());
            if(mu == null) { return; }

            SwordCraftOnline.logDebug("[Full Counter] Health: " + mu.getHealth() + ", Ret Damage: " + (damage * 2));
            //mu.setHealth((int)(mu.getHealth() - (damage * 2)));
            mu.convertHealth(damage * 2, true);

            SkillAnnouncer.messageSkill(
                mu, 
                damage*2,
                "Damage returned twofold.", 
                "Full Counter", 
                tier);
            SkillAnnouncer.messageSkill(
                (ModifierUser)c, 
                "Damage returned", 
                "Full Counter", 
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
