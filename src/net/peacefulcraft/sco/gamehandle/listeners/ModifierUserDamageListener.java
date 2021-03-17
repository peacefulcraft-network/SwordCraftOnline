package net.peacefulcraft.sco.gamehandle.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class ModifierUserDamageListener implements Listener {
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void modDamage(EntityDamageEvent e) {
        //TODO: REMOVE THIS CANCEL
        return;

        /*
        Entity vic = e.getEntity();

        ModifierUser mu = checkType(vic);
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
            
            ModifierUser damager = checkType(ev.getDamager());
            if(damager != null) {
                // Checking outgoing damage calculation
                damage = damager.checkModifier(ev.getEntity().getType().toString().toUpperCase(), damage, false);
            }

            // Checking incoming damage from entity
            damage = mu.checkModifier(ev.getDamager().getType().toString().toUpperCase(), damage, true);
        }

        // Converting health to appropriate amount
        mu.convertHealth(damage, true);     
        */     
    }

    /**
     * Helper function
     * Checks mob type against modifer users
     * @param e Entity we are checking
     * @return Modifier user if entity is valid. Null otherwise
     */
    private ModifierUser checkType(Entity e) {
        ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().get(e.getUniqueId());
        if(am == null && !(e instanceof Player)) {
            // Activemob is null and entity isn't player. Don't care
            return null;
        } else if(am != null && !(e instanceof Player)) {
            // Valid ActiveMob and not player
            return am;
        }

        // Entity is player. We check game manager
        return GameManager.findSCOPlayer((Player)e);
    }
}
