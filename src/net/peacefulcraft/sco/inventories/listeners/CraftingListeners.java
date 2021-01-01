package net.peacefulcraft.sco.inventories.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.CraftingInventory;

public class CraftingListeners implements Listener {
    @EventHandler
    /**
     * Handles when player attempts to open vanilla crafting table
     */
    public void rightClickTable(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) { return; }

        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) { return; }
        
        Block b = e.getClickedBlock();
        if(!b.getType().equals(Material.CRAFTING_TABLE)) { return; }

        new CraftingInventory(s).openInventory(s);
        e.setCancelled(true);
    }
}