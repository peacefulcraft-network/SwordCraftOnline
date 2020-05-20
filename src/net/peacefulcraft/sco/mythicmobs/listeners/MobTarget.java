package net.peacefulcraft.sco.mythicmobs.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;

/**
 * Handles mob targeting for factions and pets.
 */
public class MobTarget implements Listener {
    @EventHandler
    public void MythicMobTarget(EntityTargetEvent ev) {
        Entity e = ev.getEntity();
        Entity target = ev.getTarget();

        //Checking if entity is registered Mythic Mob
        if(SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().containsKey(e.getUniqueId())) {
            MythicMob mm = SwordCraftOnline.getPluginInstance().getMobManager().searchMMDisplay(e.getCustomName()); 
            
            //Checking if target is registered Mythic Mob
            if(SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().containsKey(target.getUniqueId())) {
                MythicMob mmTarget = SwordCraftOnline.getPluginInstance().getMobManager().searchMMDisplay(target.getCustomName()); 
                //Checking if factions align
                if(mm.hasFactionTargets() && mmTarget.hasFaction()) {
                    //If target list does not contain faction cancel.
                    if(!mm.getFactionTargets().contains(mmTarget.getFaction())) {
                        ev.setCancelled(true);
                    }
                }
            }  
            
            //Checking if mob targets players
            if(mm.getFactionTargets().contains("Player")) {
                if(!(target instanceof Player)) {
                    ev.setCancelled(true);
                } else {
                    ev.setCancelled(false);
                }
            }
        }
    }
}