package net.peacefulcraft.sco.inventories.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

public class CraftingListeners implements Listener {
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player)e.getWhoClicked();
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) { return; }

        if(e.getCurrentItem() == null) { return; }
        if(!e.getView().getTitle().equalsIgnoreCase("Crafting Table")) { return; }

        // Cancelling if they hit a blocked slot
        NBTItem nbti = new NBTItem(e.getCurrentItem());
        if(nbti.hasKey("movable") && nbti.getBoolean("movable") == false) {
            e.setCancelled(true);
        }

        // Checking if they clicked on any result slot
        boolean hitResult = checkResultSlot(e.getSlot());

        // Checking for a valid recipe and placing result in inventory
        HashMap<Integer, ItemStack> recipe = getRecipe(e.getInventory());
        HashMap<Integer, ItemStack> result = SwordCraftOnline.getPluginInstance().getCraftingManager().checkRecipe(recipe);
        if(result == null || result.isEmpty()) { return; }

        // Player hit result
        // Clear out recipe table and place items into inventory/floor
        if(hitResult) {
            // Cancel event to prevent item pickup
            e.setCancelled(true);
            giveResult(p, result);
            clearRecipe(e.getInventory());
        } else {
            setResult(e.getInventory(), result);
        }
    }

    /**
     * Helper method
     * Checks if player hit result slot
     */
    private boolean checkResultSlot(int index) {
        switch(index){
            case 14: return true;
            case 15: return true;
            case 16: return true;
            case 23: return true;
            case 24: return true;
            case 25: return true;
            case 32: return true;
            case 33: return true;
            case 34: return true;
            default: return false;
        }
    }

    /**
     * Helper method
     * Fetches crafting slot items
     */
    private HashMap<Integer, ItemStack> getRecipe(Inventory inv) {
        int i = 0;
        HashMap<Integer, ItemStack> out = new HashMap<>();

        for(int row = 1; row <= 3; row++) {
            for(int col = 1; col <= 3; col++) {
                ItemStack item = inv.getItem(row * 9 + col);
                if(item == null || item.getType().equals(Material.AIR)) { 
                    i++;
                    continue; 
                }
                out.put(i, item);
                i++;
            }
        }
        return out;
    }

    /**
     * Clears recipe slots of inventory
     */
    private void clearRecipe(Inventory inv) {
        for(int row = 1; row <= 3; row++) {
            for(int col = 1; col <= 3; col++) {
                inv.clear(row * 9 + col);
            } 
        }
    }

    /**
     * Helper method
     * Sets resulting crafting in inventory
     */
    private void setResult(Inventory inv, HashMap<Integer, ItemStack> result) {
        int i = 0;
        for(int row = 1; row <= 3; row++) {
            for(int col = 5; col <= 7; col++) {
                ItemStack item = result.get(i);
                if(item == null) { 
                    i++;
                    continue;
                }
                inv.setItem(row * 9 + col, item);
                i++;
            }
        }
    }

    /**
     * Gives player result or drops it on the ground
     */
    private void giveResult(Player p, HashMap<Integer, ItemStack> result) {
        List<ItemStack> leftovers = new ArrayList<>();
        for(ItemStack item : result.values()) {
            HashMap<Integer, ItemStack> temp = p.getInventory().addItem(item);
            for(ItemStack i : temp.values()) {
                leftovers.add(i);
            }
        }

        for(ItemStack item : leftovers) {
            p.getLocation().getWorld().dropItemNaturally(p.getLocation(), item);
        }
    }
}
