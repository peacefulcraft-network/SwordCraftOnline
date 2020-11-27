package net.peacefulcraft.sco.inventories.crafting;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        public Map<Integer, ItemStack> getRecipe() { return Collections.unmodifiableMap(this.recipe); }

    /**
     * Immovable recipe used to display
     */
    private HashMap<Integer, ItemStack> displayRecipe;
        public HashMap<Integer, ItemStack> getDisplayRecipe() { return this.displayRecipe; }

    /**
     * Resulting craft if recipe matches
     */
    private HashMap<Integer, ItemStack> result;
        public Map<Integer, ItemStack> getResult() { return Collections.unmodifiableMap(this.result); }

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
                //SwordCraftOnline.logInfo("[Recipe] Loaded Item: " + item.getItemMeta().getDisplayName() + ", Slot: " + slot);
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
                this.result.put(slot, item);

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
    public boolean checkRecipe(HashMap<Integer, ItemStack> input) {
        if(this.isInvalid || input == null || input.isEmpty()) { 
            return false; 
        }

        for(Integer i : recipe.keySet()) {
            ItemStack recipeItem = this.recipe.get(i);
            ItemStack inputItem = input.get(i);

            // Catching any null variables
            if((recipeItem == null && inputItem != null) || recipeItem != null && inputItem == null) {
                return false;
            } 
            // Both items null, we skip
            if(recipeItem == null && inputItem == null) {
                continue;
            }

            if(!recipeItem.getType().equals(inputItem.getType())) { 
                SwordCraftOnline.logInfo("[Recipe] types don't match in slot: " + i);
                return false; 
            }
            if(recipeItem.getAmount() > inputItem.getAmount()) {
                SwordCraftOnline.logInfo("[Recipe] Not enough item in slot: " + i);
                return false; 
            }
            if(!recipeItem.getItemMeta().getDisplayName().equalsIgnoreCase(inputItem.getItemMeta().getDisplayName())) { 
                SwordCraftOnline.logInfo("[Recipe] names don't match in slot: " + i);
                return false; 
            }
            //TODO: Compare rarities of custom items
        }

        return true;
    }

    public static void logRecipeInfo(HashMap<Integer, ItemStack> info) {
        String s = "";
        for(Integer i : info.keySet()) {
            ItemStack item = info.get(i);
            s += "Slot: " + i + ", Item: " + item.getItemMeta().getDisplayName() + ", Amount: " + item.getAmount() + "\n"; 
        }
        SwordCraftOnline.logInfo("[Recipe] Info: \n" + s);
    }

    public static void logRecipeInfo(Map<Integer, ItemStack> info) {
        String s = "";
        for(Integer i : info.keySet()) {
            ItemStack item = info.get(i);
            s += "Slot: " + i + ", Item: " + item.getItemMeta().getDisplayName() + ", Amount: " + item.getAmount() + "\n"; 
        }
        SwordCraftOnline.logInfo("[Recipe] Info: \n" + s);
    }

}
