package net.peacefulcraft.sco.inventories.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.CraftingInventory;
import net.peacefulcraft.sco.inventories.crafting.Recipe;

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