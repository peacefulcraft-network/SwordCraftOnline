package net.peacefulcraft.sco.inventories;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.crafting.Recipe;
import net.peacefulcraft.sco.items.utilityitems.CraftingBlockedSlot;
import net.peacefulcraft.sco.items.utilityitems.CraftingInvalidSlot;

public class CraftingInventory extends InventoryBase {

    /**
     * Constructor
     * Creates inventory instance for player
     * @param s SCOPlayer who owns this instance
     */
    public CraftingInventory(SCOPlayer s) {
        super(s.getPlayer(), InventoryType.CRAFTING);
        this.inventory = SwordCraftOnline.getPluginInstance().getServer().createInventory(null, 54, "Crafting Table");
        setBlockers();
    }

    /**
     * Constructor
     * Creates inventory instance for player and displays recipe
     * Recipes displayed are not movable
     * @param s SCOPlayer who owns this instance
     * @param recipe Name of recipe we want to display
     */
    public CraftingInventory(SCOPlayer s, String recipeName) {
        this(s);

        Recipe r = SwordCraftOnline.getPluginInstance().getCraftingManager().getRecipe(recipeName);
        if(r == null) { 
            SwordCraftOnline.logInfo("[Crafting Inventory] Attempted to load invalid recipe: " + recipeName);
        } else {
            HashMap<Integer, ItemStack> recipe = r.getDisplayRecipe();
            HashMap<Integer, ItemStack> result = r.getDisplayResult();
    
            setDisplayRecipe(recipe);
            setDisplayResult(result);
            SwordCraftOnline.logInfo("[CraftingInventory] Loaded: " + recipeName);
        }
    }

    @Override
    public void initializeDefaultLoadout() {
        return;
    }

    @Override
    public void resizeInventory(int size) {
        return;
    }

    @Override
    public void saveInventory() {
        return;
    }

    @Override
    public void openInventory() {
        super.openInventory();
    }

    @Override
    public void closeInventory() {
        super.closeInventory();
    }

    /**
     * Initializes the blocked slots in inventory
     */
    private void setBlockers() {
        for(int row = 0; row <= 5; row++) {
            for(int col = 0; col <= 8; col++) {
                if(row == 0 || row >= 4 || ((row < 4 && row > 0) && (col == 0 || col == 8 || col == 4))) {
                    this.addButton(row, col, (new CraftingBlockedSlot()).create(1, false, false));
                }
            }
        }
        this.addButton(2, 4, (new CraftingInvalidSlot()).create(1, false, false));
    }

    /**
     * Initializes display of result
     * @param result Result of craft
     */
    private void setDisplayResult(HashMap<Integer, ItemStack> result) {
        int i = 0;
        for(int row = 1; row <= 3; row++) {
            for(int col = 5; col <= 7; col++) {
                ItemStack item = result.get(i);
                if(item == null) { 
                    i++;
                    continue;
                }
                this.addButton(row, col, item);
                i++;
            }
        }
    }

    /**
     * Initializes display of recipe
     * @param recipe Recipe of craft
     */
    private void setDisplayRecipe(HashMap<Integer, ItemStack> recipe) {
        int i = 0;
        for(int row = 1; row <= 3; row++) {
            for(int col = 1; col <= 3; col++) {
                ItemStack item = recipe.get(i);
                if(item == null) {
                    i++;
                    continue;
                }
                this.addButton(row, col, item);
                i++;
            }
        }
    }
    
}
