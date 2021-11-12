package net.peacefulcraft.sco.swordskills;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;

public class ThunderstruckSkill extends SwordSkill {

    public ThunderstruckSkill(SwordSkillCaster c, int cooldown, SwordSkillProvider provider) {
        super(c, provider);
        
        this.useModule(new TimedCooldown(cooldown));
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
        EntityDamageByEntityEvent evv = (EntityDamageByEntityEvent)ev;
        Entity entity = evv.getEntity();

        ModifierUser mu = (ModifierUser)c;
        double mult = mu.getMultiplier(ModifierType.LIGHTNING, false);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), new Runnable(){
            public void run() {
                for (double i = 0.0; i < mult; i++) {
                    entity.getWorld().strikeLightning(entity.getLocation());
                }
            }
        }, 20);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
