package net.peacefulcraft.sco.swordskills;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class SupremeLockdownSkill extends SwordSkill {

    private ItemTier tier;

    public SupremeLockdownSkill(SwordSkillCaster c, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.tier = tier;
        
        this.useModule(new TimedCooldown(25000, (ModifierUser)c, "Supreme Lockdown", tier));
        this.useModule(new Trigger(SwordSkillType.SECONDARY, (ModifierUser)c));
        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
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
        for(Entity e : mu.getLivingEntity().getNearbyEntities(6, 6, 6)) {
            if(e instanceof LivingEntity) {
                ModifierUser vic = ModifierUser.getModifierUser(e);
                if(vic == null) { continue; }
                vic.getLivingEntity().addPotionEffect(
                    new PotionEffect(PotionEffectType.SLOW, 140, 999)
                );
                SkillAnnouncer.messageSkill(
                    vic, 
                    "Locked down for 7 seconds.", 
                    "Supreme Lockdown", 
                    tier);
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
