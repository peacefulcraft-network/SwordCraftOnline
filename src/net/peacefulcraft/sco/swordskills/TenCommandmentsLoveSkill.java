package net.peacefulcraft.sco.swordskills;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.peacefulcraft.sco.gamehandle.announcer.Announcer;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class TenCommandmentsLoveSkill extends SwordSkill {

    public TenCommandmentsLoveSkill(SwordSkillCaster c, SwordSkillProvider provider) {
        super(c, provider);
        
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
        if(s.getPlayerKills() > 0) { return; }

        if(mu.getLivingEntity().getNearbyEntities(15, 15, 15).contains(evv.getDamager())) {
            evv.setCancelled(true);
            Announcer.messagePlayer(
                (Player)evv.getDamager(), 
                "[Love] Do not strike another player near my conduit with your heart full of hate.", 
                0);
        }
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {
        
    }
    
}
