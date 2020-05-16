package net.peacefulcraft.sco.mythicmobs.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Snowman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;

public class MobOptions implements Listener {
    @EventHandler
    public void onCombust(EntityCombustEvent e) {
        Entity ent = e.getEntity();

        if(!(SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().keySet().contains(ent.getUniqueId()))) { return; }
        MythicMob mm = SwordCraftOnline.getPluginInstance().getMobManager().searchMMDisplay(ent.getName());

        if(mm.getPreventSunburn()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onTeleport(EntityTeleportEvent e) {
        Entity ent = e.getEntity();

        if(!(ent instanceof Enderman)) { return; }
        if(!(SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().keySet().contains(ent.getUniqueId()))) { return; }
        MythicMob mm = SwordCraftOnline.getPluginInstance().getMobManager().searchMMDisplay(ent.getName());

        if(mm.getPreventEndermanTeleport()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockInfect(EntityChangeBlockEvent e) {
        Entity ent = e.getEntity();
        
        if(!(ent instanceof Silverfish)) { return; }
        if(!(SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().keySet().contains(ent.getUniqueId()))) { return; }
        MythicMob mm = SwordCraftOnline.getPluginInstance().getMobManager().searchMMDisplay(ent.getName());

        if(mm.getPreventSilverfishInfection()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void snow(EntityBlockFormEvent e) {
        Entity ent = e.getEntity();

        if(!(ent instanceof Snowman)) { return; }
        if(!(SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().keySet().contains(ent.getUniqueId()))) { return; }
        MythicMob mm = SwordCraftOnline.getPluginInstance().getMobManager().searchMMDisplay(ent.getName());

        if(mm.getPreventSnowFormation()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void slimeSplit(SlimeSplitEvent e) {
        Entity ent = e.getEntity();

        if(!(SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().keySet().contains(ent.getUniqueId()))) { return; }
        MythicMob mm = SwordCraftOnline.getPluginInstance().getMobManager().searchMMDisplay(ent.getName());

        if(mm.getPreventSlimeSplit()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent e) {
        if(!(e.getRightClicked().getType() == EntityType.VILLAGER)) { return; }

        Entity ent = e.getRightClicked();
        if(!(SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().keySet().contains(ent.getUniqueId()))) { return; }
        MythicMob mm = SwordCraftOnline.getPluginInstance().getMobManager().getMMDisplay().get(ent.getCustomName());

        if(!mm.getIsInteractable()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onLeash(PlayerLeashEntityEvent e) {
        Entity ent = e.getEntity();

        if(!(SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().keySet().contains(ent.getUniqueId()))) { return; }
        MythicMob mm = SwordCraftOnline.getPluginInstance().getMobManager().getMMDisplay().get(ent.getCustomName());

        if(mm.getPreventLeashing()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        Entity ent = e.getRightClicked();

        if(!(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.NAME_TAG)) || e.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.NAME_TAG)) { return; }

        if(!(SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().keySet().contains(ent.getUniqueId()))) { return; }
        MythicMob mm = SwordCraftOnline.getPluginInstance().getMobManager().getMMDisplay().get(ent.getCustomName());

        if(mm.getPreventRename()) {
            e.setCancelled(true);
        }
    }
}