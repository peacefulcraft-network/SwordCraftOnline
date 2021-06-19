package net.peacefulcraft.sco.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.peacefulcraft.sco.items.utilities.Glow;

/**
 * Quest item that displays and holds
 * relevant quest data
 */
public class QuestItem implements ItemIdentifier, CustomDataHolder, EphemeralAttributeHolder {

    ItemTier tier;
    Integer quantity;

    //
    // Dynamic quest data
    //
    String displayName = "";
    ArrayList<String> lore = new ArrayList<>();
    JsonObject questObj = new JsonObject();

    public QuestItem(ItemTier tier, Integer quantity) {
        this.tier = ItemTier.COMMON;
        this.quantity = quantity;
    }

    @Override
    public String getName() {
        return "Quest";
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public ArrayList<String> getLore() {
        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.PAPER;
    }

    @Override
    public ItemTier[] getAllowedTiers() {
        return new ItemTier[] { ItemTier.COMMON };
    }

    @Override
    public ItemTier getTier() {
        return tier;
    }

    @Override
    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;        
    }

    @Override
    public boolean isDroppable() {
        return true;
    }

    @Override
    public boolean isMovable() {
        return true;
    }

    @Override
    public JsonObject getCustomData() {
        return this.questObj;
    }

    @Override
    public void setCustomData(JsonObject data) {
        this.questObj = data;
    }

    @Override
    public void parseCustomItemData(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        
        String display = ChatColor.stripColor(meta.getDisplayName());
        List<String> lore = meta.getLore();
        String sLore = String.join("\n", lore);
        int step = 0;
        boolean activated = false;

        // Processing current step
        for(String s : lore) {
            if(s.contains("[") && s.contains("]")) {
                step = (int)s.chars().filter(ch -> ch == (char)0x25A0).count();
            }
        }

        // Processing if step is activated
        // 
        // Changed to "not contain" talk to phrases
        if(!(lore.get(1).contains("Talk to") && lore.get(2).contains("to start quest"))) {
            activated = true;
        }

        // Processing step relevant data
        // TODO: Finalize this statment for all quest steps
        // Currently only process kill steps
        JsonObject stepData = new JsonObject();
        for(int i = 0; i < lore.size(); i++) {
            String s = lore.get(i);
            if(s.equals("Kill Progress:")) {
                stepData.addProperty("stepType", "kill");

                // Parsing strings
                JsonObject killObj = new JsonObject();
                String ss = lore.get(i + 1);
                while(!ss.isEmpty() || !ss.contains("Rewards:")) {
                    int killed = Integer.valueOf(StringUtils.substringBetween(ss, " ", "/"));
                    //int goal = Integer.valueOf(StringUtils.substringBetween(ss, "/", " "));
                    String target = StringUtils.substringBetween(ss, " ", "(s)");

                    killObj.addProperty(target, killed);
                }

                stepData.add("killData", killObj);
            }
        }

        this.questObj.addProperty("questName", display);
        this.questObj.addProperty("description", sLore);
        this.questObj.addProperty("step", step);
        this.questObj.add("stepData", stepData);
        this.questObj.addProperty("activated", activated);

        // Setting dynamic display name from json
        this.displayName = display;
    }

    @Override
    public ItemStack applyCustomItemData(ItemStack item, JsonObject data) {
        ItemMeta meta = item.getItemMeta();

        String display = data.get("questName").getAsString();
        String desc = data.get("description") == JsonNull.INSTANCE ? "ERROR: NO ENTERED DESC\n" : data.get("description").getAsString();
        List<String> lore = Arrays.asList(desc.split("\n"));

        // Applying chatcolors to quest lore
        for(int i = 0; i < lore.size(); i++) {
            String s = lore.get(i);
            if(s.contains("Rewards:")) {
                lore.set(i, ChatColor.GREEN + s);
            } else if(s.contains("[")) {
                lore.set(i, ChatColor.GOLD + s);
            } else {
                lore.set(i, ChatColor.DARK_AQUA + s);
            }
        }

        meta.setDisplayName(StringUtils.capitalize(display));
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public ItemStack applyEphemeralAttributes(ItemStack item) {
        return Glow.addGlow(item);
    }

    @Override
    public void parseEphemeralAttributes(ItemStack item) {
        
    }
    
}
