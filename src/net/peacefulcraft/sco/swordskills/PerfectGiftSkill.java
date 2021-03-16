package net.peacefulcraft.sco.swordskills;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class PerfectGiftSkill extends SwordSkill {

    private ItemTier tier;

    public PerfectGiftSkill(SwordSkillCaster c, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.tier = tier;
        
        this.useModule(new TimedCooldown(900000));
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
        EntityDamageEvent evv = (EntityDamageEvent)ev;
        double damage = evv.getFinalDamage();

        ModifierUser mu = ModifierUser.getModifierUser(evv.getEntity());
        if(mu.getHealth() - damage <= 0) {
            mu.setHealth(mu.getMaxHealth());
            mu.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 5, 3));             
            evv.setCancelled(true);

            SkillAnnouncer.messageSkill(mu, "Death prevented.", "Perfect Gift", tier);
        }
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
