package net.peacefulcraft.sco.inventories;

import java.util.HashMap;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.crafting.Recipe;
import net.peacefulcraft.sco.items.CraftingBlockedSlotItem;
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

    /**
     * Initializes the blocked slots in inventory
     */
    private void setBlockers() {
        CraftingBlockedSlotItem blocker = new CraftingBlockedSlotItem(ItemTier.COMMON, 1);
        for (int row = 0; row <= 5; row++) {
            for (int col = 0; col <= 8; col++) {
                if (row == 0 || row >= 4 || ((row < 4 && row > 0) && (col == 0 || col == 8 || col == 4))) {
                    this.setItem((row + col), blocker);
                }
            }
        }
        this.setItem(28, blocker);
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

    @Override
    public boolean isInventory(Inventory inventory) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void openInventory(SCOPlayer s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void closeInventory() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClickThisInventory(InventoryClickEvent ev, ItemIdentifier cursorItem, ItemIdentifier clickedItem) {
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub

    }
    
}
