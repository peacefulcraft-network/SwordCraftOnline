package net.peacefulcraft.sco.swordskills;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;

public class TenCommandmentsLoveSkill extends SwordSkill {

    private ItemTier tier;

    public TenCommandmentsLoveSkill(SwordSkillCaster c, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.tier = tier;
        
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
        if(!(ev instanceof EntityDamageByEntityEvent)) { return; }
        EntityDamageByEntityEvent evv = (EntityDamageByEntityEvent)ev;
        if(!(evv.getDamager() instanceof LivingEntity)) { return; }

        ModifierUser mu = (ModifierUser)c;
        ModifierUser damMu = ModifierUser.getModifierUser(evv.getDamager());
        if(damMu == null || !(damMu instanceof SCOPlayer)) { return; }
        SCOPlayer s = (SCOPlayer)damMu;

        double mult = mu.getMultiplier(ModifierType.SPIRITUAL, false);

        if(mult < 2.0 && s.getPlayerKills() == 0) { return; }

        if(mu.getLivingEntity().getNearbyEntities(15, 15, 15).contains(evv.getDamager())) {
            evv.setCancelled(true);

            SkillAnnouncer.messageSkill(
                s, 
                "Do not strike another player near my conduit with your heart full of hate.", 
                "Ten Commandments: Love", 
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
