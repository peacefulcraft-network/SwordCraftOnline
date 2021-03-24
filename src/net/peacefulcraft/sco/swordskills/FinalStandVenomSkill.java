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
import net.peacefulcraft.sco.utilities.Pair;

public class FinalStandVenomSkill extends SwordSkill {

    private ItemTier tier;

    public FinalStandVenomSkill(SwordSkillCaster c, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.tier = tier;
        
        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
        this.useModule(new TimedCooldown(35000, (ModifierUser)c, "Final Stand: Venom", tier));
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
                liv.addPotionEffect(new PotionEffect(
                    PotionEffectType.POISON, 
                    200, 
                    3));
            }
            ModifierUser vic = ModifierUser.getModifierUser(e);
            if(vic == null) { continue; }
            SkillAnnouncer.messageSkill(
                vic, 
                "Final Stand: Venom", 
                tier, 
                new Pair<String,Integer>(PotionEffectType.POISON.toString(), 3));
        }

        mu.queueChange(
            -(int)(mu.getHealth() * 0.2), 
            10);
        SkillAnnouncer.messageSkill(
            mu, 
            "Beguiled.", 
            "Final Stand: Venom", 
            tier);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
