package net.peacefulcraft.sco.swordskills;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class WildSliceSkill extends SwordSkill {

    private ItemTier tier;

    public WildSliceSkill(SwordSkillCaster c, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.tier = tier;
        
        this.useModule(new Trigger(SwordSkillType.SWORD, (ModifierUser)c));
        this.useModule(new TimedCooldown(32000, (ModifierUser)c, "Wild Slice", tier, null, "PlayerInteractEvent"));
        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
        this.listenFor(SwordSkillTrigger.ENTITY_DAMAGE_ENTITY_GIVE);
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
            ModifierUser mu = (ModifierUser)c;

            int roll = SwordCraftOnline.r.nextInt(2);
            double multiplier = 0.1;
            switch(roll) {
                case 0:
                    multiplier = 0.5;
                break; case 1:
                    multiplier = 2.0;
                break; case 2:
                    multiplier = 4.0;
            }
            evv.setDamage(evv.getDamage() * multiplier);
            SkillAnnouncer.messageSkill(
                mu, 
                "Damage multiplied: " + multiplier, 
                "Wild Slice", 
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
