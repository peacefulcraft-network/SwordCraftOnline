package net.peacefulcraft.sco.gamehandle.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import net.peacefulcraft.sco.swordskills.utilities.CriticalHit;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.Parry;

/**
 * Holds relevant event handling for modifier user
 * damage and health regeneration
 */
public class ModifierUserDamageListener implements Listener {
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void modDamage(EntityDamageEvent e) {
        ModifierUser vicMu = ModifierUser.getModifierUser(e.getEntity());
        if(vicMu == null) { return; }

        // Getting damage from event then setting to 0
        // 0 set might be redudant
        double damage = e.getFinalDamage();
        e.setDamage(0);

        // Calculating parry chance of MU
        if(Parry.parryCalc(null, vicMu, 0)) {
            e.setCancelled(true);
            return;
        }
        
        String cause = e.getCause().toString();
        // Checking incoming damage from cause
        damage = vicMu.checkModifier(cause, damage, true);
        if(e instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent)e;
            
            ModifierUser damager = ModifierUser.getModifierUser(ev.getDamager());
            if(damager != null) {
                // Checking outgoing damage calculation
                damage = damager.checkModifier(ev.getEntity().getType().toString().toUpperCase(), damage, false);

                // Calculating critical damage of MU
                damage = CriticalHit.damageCalc(damager, damage, 0, 0);
            }

            // Checking incoming damage from entity
            damage = vicMu.checkModifier(ev.getDamager().getType().toString().toUpperCase(), damage, true);
        }

        // Converting health to appropriate amount
        vicMu.convertHealth(damage, true);     
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void healthRegen(EntityRegainHealthEvent e) {
        Entity ent = e.getEntity();

        ModifierUser mu = ModifierUser.getModifierUser(ent);
        if(mu == null) { return; }

        mu.convertHealth(-e.getAmount(), true);
    }
}
