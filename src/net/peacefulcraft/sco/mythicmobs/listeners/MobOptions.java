package net.peacefulcraft.sco.mythicmobs.listeners;

import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Snowman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.SlimeSplitEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;

public class MobOptions implements Listener {
    @EventHandler
    public void onCombust(EntityCombustEvent e) {
        Entity ent = e.getEntity();

        if(!(SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().keySet().contains(ent.getUniqueId()))) { return; }
        MythicMob mm = SwordCraftOnline.getPluginInstance().getMobManager().getMMDisplay().get(ent.getCustomName());

        if(mm.getPreventSunburn()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onTeleport(EntityTeleportEvent e) {
        Entity ent = e.getEntity();

        if(!(ent instanceof Enderman)) { return; }
        if(!(SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().keySet().contains(ent.getUniqueId()))) { return; }
        MythicMob mm = SwordCraftOnline.getPluginInstance().getMobManager().getMMDisplay().get(ent.getCustomName());

        if(mm.getPreventEndermanTeleport()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockInfect(EntityChangeBlockEvent e) {
        Entity ent = e.getEntity();
        
        if(!(ent instanceof Silverfish)) { return; }
        if(!(SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().keySet().contains(ent.getUniqueId()))) { return; }
        MythicMob mm = SwordCraftOnline.getPluginInstance().getMobManager().getMMDisplay().get(ent.getCustomName());

        if(mm.getPreventSilverfishInfection()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void snow(EntityBlockFormEvent e) {
        Entity ent = e.getEntity();

        if(!(ent instanceof Snowman)) { return; }
        if(!(SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().keySet().contains(ent.getUniqueId()))) { return; }
        MythicMob mm = SwordCraftOnline.getPluginInstance().getMobManager().getMMDisplay().get(ent.getCustomName());

        if(mm.getPreventSnowFormation()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void slimeSplit(SlimeSplitEvent e) {
        Entity ent = e.getEntity();

        if(!(SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().keySet().contains(ent.getUniqueId()))) { return; }
        MythicMob mm = SwordCraftOnline.getPluginInstance().getMobManager().getMMDisplay().get(ent.getCustomName());

        if(mm.getPreventSlimeSplit()) {
            e.setCancelled(true);
        }
    }
}