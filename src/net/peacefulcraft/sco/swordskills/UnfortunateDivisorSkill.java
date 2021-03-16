package net.peacefulcraft.sco.swordskills;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;

public class UnfortunateDivisorSkill extends SwordSkill {

    private ItemTier tier;

    public UnfortunateDivisorSkill(SwordSkillCaster c, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.tier = tier;
        
        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
        this.listenFor(SwordSkillTrigger.ENTITY_DAMAGE_ENTITY_GIVE);

        this.useModule(new TimedCooldown(30000));
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
            if(vic instanceof Player) {
                SCOPlayer s = GameManager.findSCOPlayer((Player)vic);
                if(s == null) { return; }
                s.setHealth(s.getHealth() / 2);

                SkillAnnouncer.messageSkill(
                    s, 
                    "Health divided by two.", 
                    "Unfortunate Divisor",
                    tier);
            } else {
                ActiveMob am = SwordCraftOnline.getPluginInstance()
                    .getMobManager()
                    .getMobRegistry()
                    .get(vic.getUniqueId());
                if(am == null) { return; }
                am.setHealth(s.getHealth() / 2);
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
