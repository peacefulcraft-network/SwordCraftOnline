package net.peacefulcraft.sco.inventories.crafting;

import java.util.ArrayList;
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

    private HashMap<Integer, ItemStack> recipe;

    private HashMap<Integer, ItemStack> result;

    private Boolean isInvalid = false;

    /**
     * Constructor
     * @param name Name of recipe
     * @param mc Config
     */
    public Recipe(String name, MythicConfig mc) {
        this.recipe = new HashMap<>();
        this.result = new HashMap<>();

        this.internalName = name;

        this.name = mc.getString("Name", "");

        // Main recipe loading
        List<String> lis = mc.getStringList("Recipe");
        for(String s : lis) {
            ItemStack item = parseString(s);
            if(item.getType().equals(Material.FIRE) && item.getItemMeta().getDisplayName().equalsIgnoreCase("the server")) {
                SwordCraftOnline.logInfo("[Recipe] Invalid item in recipe for: " + name);
                this.isInvalid = true;
            } else {
                String sSlot = StringUtils.substringBetween(s, "slot{", "}");
                Integer slot = Integer.valueOf(sSlot);
                this.recipe.put(slot, item);
            }
        }

        // Loading resulting craft
        List<String> lis2 = mc.getStringList("Result");
        for(String s : lis2) {
            ItemStack item = parseString(s);
            if(item.getType().equals(Material.FIRE) && item.getItemMeta().getDisplayName().equalsIgnoreCase("the server")) {
                SwordCraftOnline.logInfo("[Recipe] Invalid item in result for: " + name);
                this.isInvalid = true;
            } else {
                String sSlot = StringUtils.substringBetween(s, "slot{", "}");
                Integer slot = Integer.valueOf(sSlot);
                this.recipe.put(slot, item);
            }
        }
    }

    private ItemStack parseString(String s) {
        String sItem = StringUtils.substringBetween(s, "item{", "}");
        String sAmount = StringUtils.substringBetween(s, "amount{", "}");
        Integer amount = Integer.valueOf(sAmount);

        ItemStack item = ItemIdentifier.generate(sItem, amount, false, true);
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
