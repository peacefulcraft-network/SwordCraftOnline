package net.peacefulcraft.sco.mythicmobs.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.mythicmobs.mobs.Centipede;

public class CentipedeDamage implements Listener {
    @EventHandler
    public void damageInternal(EntityDamageByEntityEvent e) {
        Entity damager = e.getDamager();
        Entity victim = e.getEntity();

        if(damager.hasMetadata("centipede") && victim.hasMetadata("centipede")) {
            e.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void target(EntityTargetEvent e) {
        if(!(e.getEntity().hasMetadata("centipede"))) { return; }
        
        Centipede c = null;
        for(Centipede temp : SwordCraftOnline.getPluginInstance().getMobManager().getCentipedeList().values()) {
            if(temp.contains(e.getEntity())) {
                c = temp;
                break;
            }
        }
        if(c == null) { return; }

        c.update();
    }
}