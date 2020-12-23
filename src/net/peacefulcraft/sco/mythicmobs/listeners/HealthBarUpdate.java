package net.peacefulcraft.sco.mythicmobs.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;

public class HealthBarUpdate implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageEvent e) {
        Entity ent = e.getEntity();

        if(!(SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().keySet().contains(ent.getUniqueId()))) { return; }
        ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().get(ent.getUniqueId());

        am.updateHealthBar();
    }
}