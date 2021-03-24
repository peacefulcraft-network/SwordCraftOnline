package net.peacefulcraft.sco.swordskills;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class DangerousGameExplosionTagSkill extends SwordSkill {

    private ArrayList<LivingEntity> tagList = new ArrayList<>();
    private boolean tracking = false;
        private void setTracking(boolean set) { this.tracking = set; }
    private ItemTier tier;

    public DangerousGameExplosionTagSkill(SwordSkillCaster c, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.tier = tier;
        
        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
        this.listenFor(SwordSkillTrigger.ENTITY_DAMAGE_ENTITY_GIVE);
        this.useModule(new Trigger(SwordSkillType.SWORD, true));
        this.useModule(new TimedCooldown(40000, (ModifierUser)c, "Dangerous Game: Explosion Tag", tier, null, "PlayerInteractEvent"));
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
        if(ev instanceof PlayerInteractEvent) {
            this.tracking = true;

            ModifierUser mu = (ModifierUser)c;
            SkillAnnouncer.messageSkill(
                mu, 
                "Rune activated.", 
                "Dangerous Game: Explosion Tag", 
                tier);

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), new Runnable(){
                public void run() {
                    for(LivingEntity liv : tagList) {
                        liv.getLocation().getWorld().createExplosion(
                            liv.getLocation(), 
                            2,
                            false,
                            false);
                    }
                    tagList.clear();
                    setTracking(false);
                }
            }, 200);
        } else if(ev instanceof EntityDamageByEntityEvent && this.tracking) {
            EntityDamageByEntityEvent evv = (EntityDamageByEntityEvent)ev;
            if(evv.getEntity() instanceof LivingEntity) {
                tagList.add((LivingEntity)evv.getEntity());
                
                ModifierUser vic = ModifierUser.getModifierUser(evv.getEntity());
                if(vic == null) { return; }
                SkillAnnouncer.messageSkill(
                    vic, 
                    "You have been marked.", 
                    "Dangerous Game: Explosion Tag", 
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
