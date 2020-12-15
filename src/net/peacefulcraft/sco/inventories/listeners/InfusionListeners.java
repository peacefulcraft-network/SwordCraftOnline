package net.peacefulcraft.sco.inventories.listeners;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
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
import org.bukkit.scheduler.BukkitRunnable;

import de.tr7zw.nbtapi.NBTItem;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.InfusionInventory;
import net.peacefulcraft.sco.items.utilityitems.BlueSlot;
import net.peacefulcraft.sco.items.utilityitems.GreenSlot;
import net.peacefulcraft.sco.items.utilityitems.PurpleSlot;
import net.peacefulcraft.sco.items.utilityitems.RedSlot;

public class InfusionListeners implements Listener {
    
    /**
     * n/10 chance the infusion fails
     */
    private final int FAIL_CHANCE = 2;

    /**
     * Main inventory logic
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player)e.getWhoClicked();
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) { return; }

        ItemStack clicked = e.getCurrentItem();
        Inventory inv = e.getInventory();

        if(clicked == null) { return; }
        if(!e.getView().getTitle().equalsIgnoreCase("Infusion Table")) { return; }

        // Cancelling if they hit a blocked slot
        NBTItem nbti = new NBTItem(clicked);
        if(nbti.hasKey("movable") && nbti.getBoolean("movable") == false) {
            e.setCancelled(true);
        }

        // Cancelling if they hit an input slot w/o sword skill
        if(hitIngredientSlot(e.getSlot()) && !nbti.hasKey("sword_skill")) {
            e.setCancelled(true);
        }

        // Verifying there are skills in input slots
        // and setting indicators
        boolean checkedIng = checkIngredientSlots(inv);
        setValidInfusionSlots(inv, checkedIng);

        // Checking if clicked on start button
        if(checkedIng && clicked.getType().equals(Material.PURPLE_STAINED_GLASS_PANE) 
            && clicked.getItemMeta().getDisplayName().equalsIgnoreCase("Click to begin infusion")) {
            
            beginInfusion(inv);
        }
    }

    /**
     * Handles player closing their inventory
     */
    @EventHandler
    public void closeInventory(InventoryCloseEvent e) {
        Player p = (Player)e.getPlayer();
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) { return; }

        if(!e.getView().getTitle().equalsIgnoreCase("Infusion Table")) { return; }

        clearIngredients(e.getInventory(), p, true);
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

    /**
     * Verifies there are more than 1 sword skill item in slots
     * @param inv Inventory we check
     * @return True if valid recipe, false otherwise
     */
    private boolean checkIngredientSlots(Inventory inv) {
        int skillCount = 0;
        for(int row = 1; row <= 3; row++) {
            for(int col = 1; col <= 3; col++) {
                ItemStack item = inv.getItem(row * 9 + col);
                if(item == null || item.getType().equals(Material.AIR)) { continue; }

                // Checking if item is sword skill
                NBTItem nbti = new NBTItem(item);
                if(nbti.hasKey("sword_skill") && nbti.getBoolean("sword_skill")) {
                    skillCount += item.getAmount();
                }
            }
        }

        return skillCount >= 2;
    }

    /**
     * Clears the input slots and returns items to player
     * If player inventory is full, drops items at feet
     * @param inv Inventory we are clearing
     * @param p Player instance
     */
    private void clearIngredients(Inventory inv, Player p, boolean returnItems) {
        ArrayList<ItemStack> leftovers = new ArrayList<>();
        for(int row = 1; row <= 3; row++) {
            for(int col = 1; col <= 3; col++) {
                ItemStack item = inv.getItem(row * 9 + col);
                if(item == null || item.getType().equals(Material.AIR)) { continue; }

                // Adding items to inv or leftover
                if(returnItems) {
                    HashMap<Integer,ItemStack> temp = p.getInventory().addItem(item);
                    for(ItemStack i : temp.values()) {
                        leftovers.add(i);
                    }
                }
            } 
        }

        // Dropping item at player
        if(returnItems) {
            for(ItemStack item : leftovers) {
                p.getLocation().getWorld().dropItemNaturally(p.getLocation(), item);
            }
        }
    }

    /**
     * Gets input slot items
     */
    private HashMap<Integer, ItemStack> getIngredients(Inventory inv) {
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
     * Sets the validity slots in inventory
     * @param inv Inventory we are changing
     * @param isValid True if we set colors to valid
     */
    private void setValidInfusionSlots(Inventory inv, boolean isValid) {
        if(isValid) {
            inv.setItem(13, (new GreenSlot()).create(1, false, false));
            inv.setItem(31, (new GreenSlot()).create(1, false, false));
        } else {
            inv.setItem(13, (new RedSlot()).create(1, false, false));
            inv.setItem(31, (new RedSlot()).create(1, false, false));
        }
    }

    /**
     * Main infusion logic.
     * @param inv Inventory we are changing
     */
    private void beginInfusion(Inventory inv) {
        // Determine if infusion failed right away
        boolean failed = SwordCraftOnline.r.nextInt(9) < FAIL_CHANCE;
        int fail_col = SwordCraftOnline.r.nextInt(8);         

        int col = 0;
        new BukkitRunnable(){
            @Override
            public void run() {
                if(failed && col == fail_col) {
                    resetProgressBar(inv, true);
                    this.cancel();
                }
                inv.setItem(45 + col, (new PurpleSlot()).create(1, false, false));
            }
        }.runTaskTimer(SwordCraftOnline.getPluginInstance(), 0, 20);

        // Failed infusion. We consume items and quit
        if(failed) { 
            clearIngredients(inv, null, false);
            return; 
        }

        // Beginning of main infusion logic
        HashMap<Integer, ItemStack> skills = getIngredients(inv);
        
    }

    /**
     * Resets progress bar to all blue
     * @param inv Inventory we are modifying
     */
    private void resetProgressBar(Inventory inv, boolean isFail) {
        if(isFail) {
            // Setting progress bar to red
            for(int col = 0; col <= 8; col++) {
                inv.setItem(45 + col, (new RedSlot()).create(1, false, false));
            }
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), new Runnable() {
                @Override
                public void run() {
                    resetProgressBar(inv, false);
                }
            }, 40);
        } else {
            for(int col = 0; col <= 8; col ++) {
                inv.setItem(45 + col, (new BlueSlot()).create(1, false, false));
            }
        }
    }

    /**
     * Checks if player clicked an input slot
     * @param index Index of slot
     * @return True if hit input slot
     */
    private boolean hitIngredientSlot(int index) {
        // This is ugly. I hate this
        switch(index){
            case 10: return true;
            case 11: return true;
            case 12: return true;
            case 19: return true;
            case 20: return true;
            case 21: return true;
            case 28: return true;
            case 29: return true;
            case 30: return true;
            default: return false;
        }
    }
}
