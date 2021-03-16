package net.peacefulcraft.sco.swordskills;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class FinalStandVenomSkill extends SwordSkill {

    private ItemTier tier;

    public FinalStandVenomSkill(SwordSkillCaster c, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.tier = tier;
        
        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
        this.useModule(new TimedCooldown(35000));
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
            if(e instanceof Player) {
                SCOPlayer s = GameManager.findSCOPlayer((Player)e);
                if(s == null) { continue; }
                SkillAnnouncer.messageSkill(s, "Beguiled.", "Final Stand: Venom", tier);
            }
        }

        mu.queueChange(
            -(int)(mu.getHealth() * 0.2), 
            10);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
