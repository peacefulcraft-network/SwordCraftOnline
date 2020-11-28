package net.peacefulcraft.sco.inventories.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.tr7zw.nbtapi.NBTItem;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.InfusionInventory;

public class InfusionListeners implements Listener {
    
    /**
     * Main inventory logic
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player)e.getWhoClicked();
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) { return; }

        if(e.getCurrentItem() == null) { return; }
        if(!e.getView().getTitle().equalsIgnoreCase("Infusion Table")) { return; }

        // Cancelling if they hit a blocked slot
        NBTItem nbti = new NBTItem(e.getCurrentItem());
        if(nbti.hasKey("movable") && nbti.getBoolean("movable") == false) {
            e.setCancelled(true);
        }
    }

    /**
     * Handling when player right clicks enchanting table
     */
    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) { return; }

        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) { return; }

        Block b = e.getClickedBlock();
        if(!b.getType().equals(Material.ENCHANTING_TABLE)) { return; }

        // All checks pass. We create and open inventory
        new InfusionInventory(s).openInventory();
        e.setCancelled(true);
    }
}
