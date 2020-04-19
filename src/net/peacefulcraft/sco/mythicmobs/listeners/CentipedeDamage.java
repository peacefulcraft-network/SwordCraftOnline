package net.peacefulcraft.sco.mythicmobs.listeners;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.MetadataValue;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.mythicmobs.mobs.Centipede;

public class CentipedeDamage implements Listener {
    @EventHandler
    public void centipedeDeath(EntityDeathEvent e) {
        Entity ent = e.getEntity();
        List<MetadataValue> meta = ent.getMetadata("centipede");
        if(meta.isEmpty()) { return; }

        
    }   

    @EventHandler
    public void centipedeDamage(EntityDamageByEntityEvent e) {
        Entity victim = e.getEntity();

        //If victim is not centipede quit.
        List<MetadataValue> meta = victim.getMetadata("centipede");
        if(meta.isEmpty()) { return; }
        //If victim is body segment quit
        if(meta.get(0).value().equals("0")) { return; }
        //If centipede is not registered quit
        Centipede c = SwordCraftOnline.getPluginInstance().getMobManager().searchCentipede(victim);
        if(c == null) { return; }
        
        c.segmentDamage(e.getDamage());
    }
}