package net.peacefulcraft.sco.inventories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.crafting.Recipe;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;

public class CraftingInventory extends BukkitInventoryBase {

    private SCOPlayer crafter;
        public SCOPlayer getCrafter() { return crafter; }

    /**
     * Constructor Creates inventory instance for player
     * 
     * @param s SCOPlayer who owns this instance
     */
    public CraftingInventory(SCOPlayer s) {
        this.crafter = s;
        this.inventory = SwordCraftOnline.getPluginInstance().getServer().createInventory(null, 54, "Crafting Table");
        setBlockers();
    }

    /**
     * Constructor Creates inventory instance for player and displays recipe Recipes
     * displayed are not movable
     * 
     * @param s      SCOPlayer who owns this instance
     * @param recipe Name of recipe we want to display
     */
    public CraftingInventory(SCOPlayer s, String recipeName) {
        this(s);

        Recipe r = SwordCraftOnline.getPluginInstance().getCraftingManager().getRecipe(recipeName);
        if (r == null) {
            SwordCraftOnline.logInfo("[Crafting Inventory] Attempted to load invalid recipe: " + recipeName);
        } else {
            HashMap<Integer, ItemIdentifier> recipe = r.getDisplayRecipe();
            HashMap<Integer, ItemIdentifier> result = r.getDisplayResult();

            setDisplayRecipe(recipe);
            setDisplayResult(result);
            SwordCraftOnline.logInfo("[CraftingInventory] Loaded: " + recipeName);
        }
    }

    @Override
    public boolean isInventory(Inventory inventory) {
        return false;
    }

    @Override
    public void openInventory(SCOPlayer s) {
        SwordCraftOnline.getInventoryListeners().onInventoryOpen(crafter.getPlayer().openInventory(this.inventory), this);;
    }

    @Override
    public void closeInventory() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClickThisInventory(InventoryClickEvent ev, ItemIdentifier cursorItem, ItemIdentifier clickedItem) {
        if(!ev.getView().getTitle().equalsIgnoreCase("Crafting Table")) { return; }
        if(!clickedItem.isMovable()) { ev.setCancelled(true); }

        boolean hitResult = checkResultSlot(ev.getSlot());
        HashMap<Integer, ItemIdentifier> rec = getRecipe();
        Recipe result = SwordCraftOnline.getPluginInstance().getCraftingManager().checkRecipe(rec);
        if(result == null) {
            setResult(result);
            setValidCraftSlots(false);
            return;
        }

        if(hitResult) {
            ev.setCancelled(true);
            giveResult(result);
            setResult(null);
            setValidCraftSlots(false);
        } else {
            setResult(result);
            setValidCraftSlots(true);
        }
    }

    @Override
    public void onClickThatInventory(InventoryClickEvent ev, ItemIdentifier cursorItem, ItemIdentifier clickedItem) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onThisInventoryDrag(InventoryDragEvent ev, HashMap<Integer, ItemIdentifier> items) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onThatInventoryDrag(InventoryDragEvent ev, HashMap<Integer, ItemIdentifier> items) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInventoryClose(InventoryCloseEvent ev) {
        if(!ev.getView().getTitle().equalsIgnoreCase("Crafting Table")) { return; }
        clearRecipe();
    }

    
    /**
     * Initializes the blocked slots in inventory
     */
    private void setBlockers() {
        for(int row = 0; row <= 5; row++) {
            for(int col = 0; col <= 8; col++) {
                if(row == 0 || row >= 4 || ((row < 4 && row > 0) && (col == 0 || col == 8 || col == 4))) {
                    setItem(row * 9 + col, ItemIdentifier.generateIdentifier("BlackSlot", ItemTier.COMMON, 1));
                }
            }
        }
        setItem(22, ItemIdentifier.generateIdentifier("RedSlot", ItemTier.COMMON, 1));
    }

    /**
     * Initializes display of result
     * 
     * @param result Result of craft
     */
    private void setDisplayResult(HashMap<Integer, ItemIdentifier> result) {
        int i = 0;
        for (int row = 1; row <= 3; row++) {
            for (int col = 5; col <= 7; col++) {
                ItemIdentifier item = result.get(i);
                if (item == null) {
                    i++;
                    continue;
                }
                this.setItem((row + col), item);
                i++;
            }
        }
    }

    /**
     * Initializes display of recipe
     * 
     * @param recipe Recipe of craft
     */
    private void setDisplayRecipe(HashMap<Integer, ItemIdentifier> recipe) {
        int i = 0;
        for (int row = 1; row <= 3; row++) {
            for (int col = 1; col <= 3; col++) {
                ItemIdentifier item = recipe.get(i);
                if (item == null) {
                    i++;
                    continue;
                }
                this.setItem((row + col), item);
                i++;
            }
        }
    }

    /**
     * Fetches crafting slot items
     * @return Hashmap of items in recipe slots
     */
    private HashMap<Integer, ItemIdentifier> getRecipe() {
        int i = 0;
        HashMap<Integer, ItemIdentifier> out = new HashMap<>();

        for(int row = 1; row <= 3; row++) {
            for(int col = 1; col <= 3; col++) {
                ItemIdentifier item = getItem(row * 9 + col);
                if(item == null || item.getMaterial() == Material.AIR) { 
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
    private void clearRecipe() {
        ArrayList<ItemStack> leftovers = new ArrayList<>();
        for(int row = 1; row <= 3; row++) {
            for(int col = 1; col <= 3; col++) {
                ItemIdentifier item = getItem(row * 9 + col);
                if(item == null || item.getMaterial().equals(Material.AIR)) { continue; }

                // Adding items to inv or leftover
                HashMap<Integer,ItemStack> temp = this.crafter.getPlayer().getInventory().addItem(ItemIdentifier.generateItem(item));
                for(ItemStack i : temp.values()) {
                    leftovers.add(i);
                }
            } 
        }

        // Dropping item at player
        for(ItemStack item : leftovers) {
            this.crafter.getPlayer().getLocation().getWorld().dropItemNaturally(this.crafter.getPlayer().getLocation(), item);
        }
    }

    /**
     * Sets resulting craft in inventory
     * @param recipe Recipe we are checking. If null, we clear slots
     */
    private void setResult(Recipe recipe) {
        Map<Integer, ItemIdentifier> result = recipe == null ? new HashMap<>() : recipe.getResult();
        int i = 0;
        for(int row = 1; row <= 3; row++) {
            for(int col = 5; col <= 7; col++) {
                if(recipe == null) {
                    removeItem(row * 9 + col);
                    continue;
                }

                ItemIdentifier item = result.get(i);
                if(item == null || item.getMaterial().equals(Material.AIR)) {
                    i++;
                    continue;
                }
                setItem(row * 9 + col, item);
            }
        }
    }

    /**
     * Gives player the crafting or drops it on the ground
     * Removes necessary recipe components from recipe slots
     * @param recipe Recipe they crafted
     */
    private void giveResult(Recipe recipe) {
        int i = 0;
        Map<Integer, ItemIdentifier> rec = recipe.getRecipe();
        for(int row = 1; row <= 3; row++) {
            for(int col = 1; col <= 3; col++) {
                ItemIdentifier item = getItem(row * 9 + col);
                if(item == null || item.getMaterial().equals(Material.AIR)) {
                    i++;
                    continue;
                }

                // Subtract-Remove logic
                int amount = rec.get(i).getQuantity();
                if(item.getQuantity() - amount == 0) {
                    removeItem(row * 9 + col);
                } else {
                    item.setQuantity(item.getQuantity() - amount);
                }
                i++;
            }
        }

        Map<Integer, ItemIdentifier> result = recipe.getResult();
        List<ItemIdentifier> leftovers = new ArrayList<>();
        for(ItemIdentifier item : result.values()) {
            HashMap<Integer, ItemIdentifier> temp = this.crafter.getPlayerInventory().addItem(item);
            for(ItemIdentifier itemm : temp.values()) {
                leftovers.add(itemm);
            }
        }

        for(ItemIdentifier item : leftovers) {
            this.crafter.getPlayer().getLocation().getWorld().dropItemNaturally(crafter.getLocation(), ItemIdentifier.generateItem(item));
        }
    }
    
    /**
     * Checks if player hit result slot
     * @param index Slot we are checking
     * @return True if result slots was hit
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
     * Sets UI slot to red or green
     * @param valid Sets green if True, red if false
     */
    private void setValidCraftSlots(boolean valid) {
        if(valid) {
            setItem(22, ItemIdentifier.generateIdentifier("GreenSlot", ItemTier.COMMON, 1));
        } else {
            setItem(22, ItemIdentifier.generateIdentifier("RedSlot", ItemTier.COMMON, 1));
        }
    }
}
