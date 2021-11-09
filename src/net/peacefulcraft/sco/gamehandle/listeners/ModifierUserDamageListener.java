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

        vicMu.handleDamageByEvent(e);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void healthRegen(EntityRegainHealthEvent e) {
        Entity ent = e.getEntity();

        ModifierUser mu = ModifierUser.getModifierUser(ent);
        if(mu == null) { return; }

        mu.convertHealth(-e.getAmount(), true);
    }
}
