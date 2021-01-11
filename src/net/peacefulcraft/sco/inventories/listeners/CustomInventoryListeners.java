package net.peacefulcraft.sco.inventories.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.CraftingInventory;
import net.peacefulcraft.sco.inventories.ForgeInventory;
import net.peacefulcraft.sco.inventories.InfusionInventory;

public class CustomInventoryListeners implements Listener {
    
    /**
     * Handling when player rich clicks custom inventory blocks
     * 
     * @param e interact event
     */
    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        SCOPlayer s = GameManager.findSCOPlayer(e.getPlayer());
        if(s == null) { return; }

        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) { return; }

        Block b = e.getClickedBlock();
        if(b.getType().equals(Material.ENCHANTING_TABLE)) {
            (new InfusionInventory(s)).openInventory(s);
            e.setCancelled(true);
        } else if(b.getType().equals(Material.CRAFTING_TABLE)) {
            (new CraftingInventory(s)).openInventory(s);
            e.setCancelled(true);
        } else if(b.getType().equals(Material.ANVIL)) {
            (new ForgeInventory(s)).openInventory(s);
            e.setCancelled(true);
        }
    }

}
