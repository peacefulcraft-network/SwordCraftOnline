package net.peacefulcraft.sco.swordskills.listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillTrigger;

public class AbilityEntityDamageEntityListener implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDamageEntity(EntityDamageByEntityEvent ev) {        
        SwordSkillCaster attacker = null;
        SwordSkillCaster victim = null;

        // Resolve the attacker
        if(ev.getDamager() instanceof Player){
            attacker = (SwordSkillCaster) GameManager.findSCOPlayer((Player) ev.getDamager());
        } else if(ev.getDamager() instanceof LivingEntity) {
            attacker = (SwordSkillCaster) SwordCraftOnline.getPluginInstance().getMobManager().getMythicMobInstance(ev.getDamager());
        }

        // Resolve the victim
        if(ev.getEntity() instanceof Player) {
            victim = (SwordSkillCaster) GameManager.findSCOPlayer((Player) ev.getEntity());
        } else if(ev.getEntity() instanceof LivingEntity) {
            victim = (SwordSkillCaster) SwordCraftOnline.getPluginInstance().getMobManager().getMythicMobInstance(ev.getDamager());
        }
        
        // Execute attacker's ability event loop
        if(attacker != null) {
            attacker.getSwordSkillManager().abilityExecuteLoop(SwordSkillTrigger.ENTITY_DAMAGE_ENTITY_GIVE, ev);
        }

        // Execute the victims ability event loop
        if(victim != null) {
            victim.getSwordSkillManager().abilityExecuteLoop(SwordSkillTrigger.ENTITY_DAMAGE_ENTITY_RECIEVE, ev);
        }
		
	}
	
}