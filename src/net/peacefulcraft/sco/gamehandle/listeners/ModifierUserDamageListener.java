package net.peacefulcraft.sco.gamehandle.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

/**
 * Holds relevant event handling for modifier user
 * damage and health regeneration
 */
public class ModifierUserDamageListener implements Listener {
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void modDamage(EntityDamageEvent e) {
        Entity vic = e.getEntity();

        ModifierUser mu = ModifierUser.getModifierUser(vic);
        if(mu == null) { return; }

        // Getting damage from event then setting to 0
        // 0 set might be redudant
        double damage = e.getFinalDamage();
        e.setDamage(0);
        
        String cause = e.getCause().toString();
        // Checking incoming damage from cause
        damage = mu.checkModifier(cause, damage, true);
        if(e instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent)e;
            
            ModifierUser damager = ModifierUser.getModifierUser(ev.getDamager());
            if(damager != null) {
                // Checking outgoing damage calculation
                damage = damager.checkModifier(ev.getEntity().getType().toString().toUpperCase(), damage, false);
            }

            // Checking incoming damage from entity
            damage = mu.checkModifier(ev.getDamager().getType().toString().toUpperCase(), damage, true);
        }

        // Converting health to appropriate amount
        mu.convertHealth(damage, true);     
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void healthRegen(EntityRegainHealthEvent e) {
        Entity ent = e.getEntity();

        ModifierUser mu = ModifierUser.getModifierUser(ent);
        if(mu == null) { return; }

        mu.convertHealth(-e.getAmount(), true);
    }
}
