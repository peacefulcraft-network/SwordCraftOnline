package net.peacefulcraft.sco.inventories.crafting;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.mythicmobs.io.IOHandler;
import net.peacefulcraft.sco.mythicmobs.io.IOLoader;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;

public class CraftingManager {
    
    private HashMap<String, Recipe> recipes = new HashMap<>();

    /**
     * Constructor
     * Initializes .yml load
     */
    public CraftingManager() {
        load();
    }

    public void load() {
        this.recipes.clear();

        IOLoader<SwordCraftOnline> defaultRecipes = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "ExampleRecipes.yml", "Recipes");
        defaultRecipes  = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "ExampleRecipes.yml", "Recipes");
        List<File> recipeFiles = IOHandler.getAllFiles(defaultRecipes.getFile().getParent());
        List<IOLoader<SwordCraftOnline>> recipeLoaders = IOHandler.getSaveLoad(SwordCraftOnline.getPluginInstance(), recipeFiles, "Recipes");
    
        for(IOLoader<SwordCraftOnline> sl : recipeLoaders) {
            for(String name : sl.getCustomConfig().getConfigurationSection("").getKeys(false)) {
                try {
                    MythicConfig mc = new MythicConfig(name, sl.getFile(), sl.getCustomConfig());

                    Recipe r = new Recipe(name, mc);
                    if(name == null || name.equalsIgnoreCase("") || name.isEmpty() || r.getName().equalsIgnoreCase("") || r.getName().isEmpty()) {
                        continue;
                    }
                    this.recipes.put(r.getName(), r);

                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        SwordCraftOnline.logInfo("[CraftingManager] Loading complete!");
    }

    /**
     * Checks recipe against all loaded recipes
     * @param recipe slotted items in crafting table
     * @return Map of resulting craft, null if invalid craft
     */
    public Recipe checkRecipe(HashMap<Integer, ItemStack> recipe) {
        for(Recipe r : this.recipes.values()) {
            boolean result = r.checkRecipe(recipe);
            if(!result) { continue; }
            return r;
        }
        return null;
    }

    /**
     * Gets a recipe from name
     * @param name Name of recipe
     * @return Recipe or null
     */
    public Recipe getRecipe(String name) {
        return this.recipes.get(name);
    }
}
