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
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;

public class Recipe {
    
    private String internalName;

    private String name;
        public String getName() { return name; }

    /**
     * Actual recipe of craft
     */
    private HashMap<Integer, ItemIdentifier> recipe;
        public Map<Integer, ItemIdentifier> getRecipe() { return Collections.unmodifiableMap(this.recipe); }

    /**
     * Immovable recipe used to display
     */
    private HashMap<Integer, ItemIdentifier> displayRecipe;
        public HashMap<Integer, ItemIdentifier> getDisplayRecipe() { return this.displayRecipe; }

    /**
     * Resulting craft if recipe matches
     */
    private HashMap<Integer, ItemIdentifier> result;
        public Map<Integer, ItemIdentifier> getResult() { return Collections.unmodifiableMap(this.result); }

    /**
     * Immovable result used to display
     */
    private HashMap<Integer, ItemIdentifier> displayResult;
        public HashMap<Integer, ItemIdentifier> getDisplayResult() { return this.displayResult; }

    private Boolean isInvalid = false;

    /**
     * Constructor
     * @param name Name of recipe
     * @param mc Config
     */
    public Recipe(String name, MythicConfig mc) {
        this.recipe = new HashMap<Integer, ItemIdentifier>();
        this.result = new HashMap<Integer, ItemIdentifier>();
        this.displayRecipe = new HashMap<Integer, ItemIdentifier>();
        this.displayResult = new HashMap<Integer, ItemIdentifier>();

        this.internalName = name;

        this.name = mc.getString("Name", "");

        // Main recipe loading
        List<String> lis = mc.getStringList("Recipe");
        for(String s : lis) {
            ItemIdentifier item = parseString(s, true);
            if(item.getMaterial().equals(Material.FIRE) && item.getName().equalsIgnoreCase("the server")) {
                SwordCraftOnline.logInfo("[Recipe] Invalid item in recipe for: " + name);
                this.isInvalid = true;
            } else {
                String sSlot = StringUtils.substringBetween(s, "slot{", "}");
                Integer slot = Integer.valueOf(sSlot);
                //SwordCraftOnline.logInfo("[Recipe] Loaded Item: " + item.getItemMeta().getDisplayName() + ", Slot: " + slot);
                this.recipe.put(slot, item);

                // Adding to display
                ItemIdentifier dis = parseString(s, false);
                this.displayRecipe.put(slot, dis);
            }
        }

        // Loading resulting craft
        List<String> lis2 = mc.getStringList("Result");
        for(String s : lis2) {
            ItemIdentifier item = parseString(s, true);
            if(item.getMaterial() == Material.FIRE && item.getName().equalsIgnoreCase("the server")) {
                SwordCraftOnline.logInfo("[Recipe] Invalid item in result for: " + name);
                this.isInvalid = true;
            } else {
                String sSlot = StringUtils.substringBetween(s, "slot{", "}");
                Integer slot = Integer.valueOf(sSlot);
                this.result.put(slot, item);

                // Adding to display
                ItemIdentifier dis = parseString(s, false);
                this.displayResult.put(slot, dis);
            }
        }
    }

    private ItemIdentifier parseString(String s, boolean movable) {
        String sItem = StringUtils.substringBetween(s, "item{", "}");
        String sAmount = StringUtils.substringBetween(s, "amount{", "}");
        Integer amount = Integer.valueOf(sAmount);

        return ItemIdentifier.generateIdentifier(sItem, ItemTier.COMMON, amount);
    }

    /**
     * Validates recipe against input hashmap
     */
    public boolean checkRecipe(HashMap<Integer, ItemIdentifier> input) {
        if(this.isInvalid || input == null || input.isEmpty()) { 
            return false; 
        }

        for(Integer i : recipe.keySet()) {
            ItemIdentifier recipeItem = this.recipe.get(i);
            ItemIdentifier inputItem = input.get(i);

            // Catching any null variables
            if((recipeItem == null && inputItem != null) || recipeItem != null && inputItem == null) {
                return false;
            } 
            // Both items null, we skip
            if(recipeItem == null && inputItem == null) {
                continue;
            }

            if(!recipeItem.getMaterial().equals(inputItem.getMaterial())) { 
                SwordCraftOnline.logInfo("[Recipe] types don't match in slot: " + i);
                return false; 
            }
            if(recipeItem.getQuantity() > inputItem.getQuantity()) {
                SwordCraftOnline.logInfo("[Recipe] Not enough item in slot: " + i);
                return false; 
            }
            if(!recipeItem.getName().equalsIgnoreCase(inputItem.getName())) { 
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
