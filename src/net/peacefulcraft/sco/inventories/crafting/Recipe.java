package net.peacefulcraft.sco.inventories.crafting;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;

public class Recipe {
    
    private String internalName;

    private String name;
        public String getName() { return name; }

    /**
     * Actual recipe of craft
     */
    private HashMap<Integer, ItemStack> recipe;

    /**
     * Immovable recipe used to display
     */
    private HashMap<Integer, ItemStack> displayRecipe;
        public HashMap<Integer, ItemStack> getDisplayRecipe() { return this.displayRecipe; }

    /**
     * Resulting craft if recipe matches
     */
    private HashMap<Integer, ItemStack> result;

    /**
     * Immovable result used to display
     */
    private HashMap<Integer, ItemStack> displayResult;
        public HashMap<Integer, ItemStack> getDisplayResult() { return this.displayResult; }

    private Boolean isInvalid = false;

    /**
     * Constructor
     * @param name Name of recipe
     * @param mc Config
     */
    public Recipe(String name, MythicConfig mc) {
        this.recipe = new HashMap<>();
        this.result = new HashMap<>();
        this.displayRecipe = new HashMap<>();
        this.displayResult = new HashMap<>();

        this.internalName = name;

        this.name = mc.getString("Name", "");

        // Main recipe loading
        List<String> lis = mc.getStringList("Recipe");
        for(String s : lis) {
            ItemStack item = parseString(s, true);
            if(item.getType().equals(Material.FIRE) && item.getItemMeta().getDisplayName().equalsIgnoreCase("the server")) {
                SwordCraftOnline.logInfo("[Recipe] Invalid item in recipe for: " + name);
                this.isInvalid = true;
            } else {
                String sSlot = StringUtils.substringBetween(s, "slot{", "}");
                Integer slot = Integer.valueOf(sSlot);
                this.recipe.put(slot, item);

                // Adding to display
                ItemStack dis = parseString(s, false);
                this.displayRecipe.put(slot, dis);
            }
        }

        // Loading resulting craft
        List<String> lis2 = mc.getStringList("Result");
        for(String s : lis2) {
            ItemStack item = parseString(s, true);
            if(item.getType().equals(Material.FIRE) && item.getItemMeta().getDisplayName().equalsIgnoreCase("the server")) {
                SwordCraftOnline.logInfo("[Recipe] Invalid item in result for: " + name);
                this.isInvalid = true;
            } else {
                String sSlot = StringUtils.substringBetween(s, "slot{", "}");
                Integer slot = Integer.valueOf(sSlot);
                this.recipe.put(slot, item);

                // Adding to display
                ItemStack dis = parseString(s, false);
                this.displayResult.put(slot, dis);
            }
        }
    }

    private ItemStack parseString(String s, boolean movable) {
        String sItem = StringUtils.substringBetween(s, "item{", "}");
        String sAmount = StringUtils.substringBetween(s, "amount{", "}");
        Integer amount = Integer.valueOf(sAmount);

        ItemStack item = ItemIdentifier.generate(sItem, amount, Boolean.valueOf(false), Boolean.valueOf(movable));
        return item;
    }

    /**
     * Validates recipe against input hashmap
     */
    public HashMap<Integer, ItemStack> checkRecipe(HashMap<Integer, ItemStack> recipe) {
        if(this.isInvalid) { return null; }

        for(Integer i : recipe.keySet()) {
            ItemStack recipeItem = this.recipe.get(i);
            ItemStack inputItem = recipe.get(i);

            if(!recipeItem.getType().equals(inputItem.getType())) { return null; }
            if(recipeItem.getAmount() != inputItem.getAmount()) { return null; }
            //TODO: Compare rarities of custom items
        }

        return this.result;
    }

}
