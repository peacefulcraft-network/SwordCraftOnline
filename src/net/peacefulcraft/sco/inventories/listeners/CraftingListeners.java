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
import net.peacefulcraft.sco.items.utilityitems.CraftingInvalidSlot;
import net.peacefulcraft.sco.items.utilityitems.CraftingValidSlot;

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
        Recipe result = SwordCraftOnline.getPluginInstance().getCraftingManager().checkRecipe(recipe);
        if(result == null) { 
            clearResult(e.getInventory());
            setValidCraftSlot(e.getInventory(), false);
            return; 
        }

        // Player hit result
        // Clear out recipe table and place items into inventory/floor
        if(hitResult) {
            // Cancel event to prevent item pickup
            e.setCancelled(true);
            giveResult(p, result);
            craftRecipe(e.getInventory(), result);
            clearResult(e.getInventory());
            setValidCraftSlot(e.getInventory(), false);
        } else {
            setResult(e.getInventory(), result);
            setValidCraftSlot(e.getInventory(), true);
        }
    }

    @EventHandler
    /**
     * Handles player closing inventory and returns their items
     */
    public void closeInventory(InventoryCloseEvent e) {
        Player p = (Player)e.getPlayer();
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) { return; }

        if(!e.getView().getTitle().equalsIgnoreCase("Crafting Table")) { return; }

        clearRecipe(e.getInventory(), p);
    }

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

        new CraftingInventory(s).openInventory();
        e.setCancelled(true);
    }

    private void setValidCraftSlot(Inventory inv, boolean valid) {
        if(valid) {
            inv.setItem(22, (new CraftingValidSlot()).create(1, false, false));
        } else {
            inv.setItem(22, (new CraftingInvalidSlot()).create(1, false, false));
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
     * dumps items into players inventory or ground
     */
    private void clearRecipe(Inventory inv, Player p) {
        ArrayList<ItemStack> leftovers = new ArrayList<>();
        for(int row = 1; row <= 3; row++) {
            for(int col = 1; col <= 3; col++) {
                ItemStack item = inv.getItem(row * 9 + col);
                if(item == null || item.getType().equals(Material.AIR)) { continue; }

                // Adding items to inv or leftover
                HashMap<Integer,ItemStack> temp = p.getInventory().addItem(item);
                for(ItemStack i : temp.values()) {
                    leftovers.add(i);
                }
            } 
        }

        // Dropping item at player
        for(ItemStack item : leftovers) {
            p.getLocation().getWorld().dropItemNaturally(p.getLocation(), item);
        }
    }

    /**
     * Removes necessary recipe components from recipe slots
     * Subtracting from total amount per slot or clearing slot
     */
    private void craftRecipe(Inventory inv, Recipe r) {
        int i = 0;
        Map<Integer, ItemStack> recipe = r.getRecipe();

        for(int row = 1; row <= 3; row++) {
            for(int col = 1; col <= 3; col++) {
                ItemStack item = inv.getItem(row * 9 + col);
                if(item == null || item.getType().equals(Material.AIR)) {
                    i++;
                    continue;
                }

                // Subtract / remove logic
                int amount = recipe.get(i).getAmount();
                if(item.getAmount() - amount == 0) {
                    inv.clear(row * 9 + col);
                } else {
                    item.setAmount(item.getAmount() - amount);
                }
                i++;
            }
        }
    }

    /**
     * Helper method
     * Sets resulting crafting in inventory
     */
    private void setResult(Inventory inv, Recipe recipe) {
        Map<Integer, ItemStack> result = recipe.getResult();
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
     * Clears result slots
     */
    private void clearResult(Inventory inv) {
        for(int row = 1; row <= 3; row++) {
            for(int col = 5; col <= 7; col++) {
                inv.clear(row * 9 + col);
            }
        }
    }

    /**
     * Gives player result or drops it on the ground
     */
    private void giveResult(Player p, Recipe recipe) {
        Map<Integer, ItemStack> result = recipe.getResult();
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
