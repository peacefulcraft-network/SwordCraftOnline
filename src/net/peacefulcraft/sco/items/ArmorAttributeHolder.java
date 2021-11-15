package net.peacefulcraft.sco.items;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.items.utilities.DispositionHandler;
import net.peacefulcraft.sco.swordskills.armorskills.ArmorModifier;
import net.peacefulcraft.sco.swordskills.armorskills.ArmorModifierList;
import net.peacefulcraft.sco.swordskills.weaponskills.WeaponModifier;
import net.peacefulcraft.sco.utilities.ChatColorUtil;
import net.peacefulcraft.sco.utilities.RomanNumber;

/**
 * Sub category of ItemIdentifier for armor that 
 * has lore parsed into applicable effects
 */
public interface ArmorAttributeHolder {
    
    /**
     * Contains armors armor skill data
     * 
     * @return
     */
    public abstract JsonObject getArmorData();

    /**
     * This armors roll disposition. 1+x/x
     * 
     * @return
     */
    public abstract Integer getDisposition();

    /**
     * This armors max reforge count
     * @return
     */
    public abstract Integer getMaxReforge();

    /**
     * Parse armor lore data into json object
     * 
     * @param item Item stack we are parsing
     * @param identifier Supporting identifier
     * @return All accurate armor data from lore of item
     */
    public static JsonObject parseArmorData(ItemStack item, ItemIdentifier identifier) {

        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();

        /**Returning final object */
        JsonObject retObj = new JsonObject();
        /**Skills that can be reforged */
        JsonArray skillReObj = new JsonArray();
        /**Skills that are permanent to armor */
        JsonObject skillObj = new JsonObject();


        Integer reforgeCount = 0;

        for(int i = 0; i <lore.size(); i++) {
            String s = lore.get(i);

            // Capture reforge count on armor
            if(ChatColor.stripColor(s).contains("Total Armor Reforges:")) {
                String[] split = s.split(" ");
                reforgeCount = Integer.valueOf(split[3]);
            }

            // Capture armor skills on armor
            if(ChatColor.stripColor(s).contains("Armor Skills:")) {
                int j = i + 1;
                armorLoop:
                while(j < lore.size() && !lore.get(j).isEmpty()) {
                    try {
                        ArmorModifier am = ArmorAttributeHolder.parseArmorModifier(lore, j);
                        if (am == null) { continue; }
                        
                        // Adding to respective skill json objects
                        if(!ChatColorUtil.getColor(lore.get(j)).contains(ChatColor.GOLD)) {
                            skillReObj.add(am.getName() + ": " + RomanNumber.romanToDecimal(am.getLevel()));
                        } else if(ChatColorUtil.getColor(lore.get(j)).contains(ChatColor.GOLD)) {
                            skillObj.addProperty(am.getName(), RomanNumber.romanToDecimal(am.getLevel()));
                        }
                    } catch(RuntimeException ex) {
                        ex.printStackTrace();
                        break armorLoop;
                    }
                    j++;
                }

                retObj.add("reforgeSkills", skillReObj);
                retObj.add("skills", skillObj);
                i = j - 1;
            }
        }
        retObj.addProperty("reforgeCount", reforgeCount);
        if(identifier != null) {
            retObj.addProperty("maxReforge", ((ArmorAttributeHolder)identifier).getMaxReforge());
        }

        return retObj;
    }

    /**
     * Parses item lore into list of armor modifiers
     * with proper level
     * 
     * @param item Item we are parsing
     * @return List of armor modifiers
     */
    public static ArrayList<ArmorModifier> parseLore(ItemStack item) {
        JsonObject obj = parseArmorData(item, null);
        ArrayList<ArmorModifier> out = new ArrayList<>();

        JsonArray reforgeObject = obj.getAsJsonArray("reforgeSkills");
        Iterator<JsonElement> iter = reforgeObject.iterator();
        while(iter.hasNext()) {
            String s = iter.next().getAsString();
            String[] split = s.split(": ");

            ArmorModifier am = ArmorModifier.generateArmorSkill(
                split[0], 
                RomanNumber.toRoman(Integer.valueOf(split[1]))
            );
            if (am == null) { continue; }

            out.add(am);
        }

        JsonObject skillsObject = obj.getAsJsonObject("skills");
        for (Entry<String, JsonElement> entry : skillsObject.entrySet()) {
            ArmorModifier am = ArmorModifier.generateArmorSkill(
                entry.getKey(), 
                RomanNumber.toRoman(entry.getValue().getAsInt())
            );
            if (am == null) { continue; }

            out.add(am);
        }

        return out;
    }

    public static ItemStack applyLore(ItemStack item, JsonObject armorData) {

        JsonObject skills = armorData.getAsJsonObject("skills");
        JsonArray reforgeSkills = armorData.getAsJsonArray("reforgeSkills");

        Integer reforgeCount = armorData.get("reforgeCount").getAsInt();
        Integer maxReforge = armorData.get("maxReforge").getAsInt();

        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();

        if (skills != null && !skills.entrySet().isEmpty()) {
            if(!ChatColorUtil.getColor(lore.get(lore.size() - 1)).contains(ChatColor.DARK_RED)) { lore.add(""); }

            lore.add(ChatColor.LIGHT_PURPLE + "Armor skills:");
            for(Entry<String, JsonElement> entry : skills.entrySet()) {
                lore.add(ChatColor.GOLD + " -" + entry.getKey() + " " + RomanNumber.toRoman(entry.getValue().getAsInt()));
            }
            if (reforgeSkills != null) {
                Iterator<JsonElement> iter = reforgeSkills.iterator();
                while(iter.hasNext()) {
                    String s = iter.next().getAsString();
                    String[] split = s.split(": ");
                    lore.add(ChatColor.AQUA + " -" + split[0] + " " + RomanNumber.toRoman(Integer.valueOf(split[1])));
                }
            }
        }

        if (maxReforge != null) {
            int currReforge = reforgeSkills == null ? 0 : reforgeSkills.size();

            lore.add("");
            lore.add(ChatColor.DARK_RED + "Remaining Armor Slots: " + (maxReforge - currReforge));
        }

        if (reforgeCount != null && reforgeCount > 0) {
            lore.add(ChatColor.DARK_RED + "Total Armor Reforges: " + reforgeCount);
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    /**
     * Parses armor modifier from lore data
     * 
     * @param lore Lore of an item
     * @param j Current index we want to parse
     * 
     * @return Armor modifier
     * @throws RuntimeException on invalid parsing
     */
    private static ArmorModifier parseArmorModifier(List<String> lore, int j) {
        String[] split = ChatColor.stripColor(lore.get(j)).replace(" -", "").split(" ");
        String level = "";
        String skillName = "";
        List<String> skillNameLis = new ArrayList<>();

        int k = 0;
        for(; k < split.length; k++) {
            Integer validityTest = RomanNumber.romanToDecimal(split[k]);
            if (validityTest == null) {
                skillNameLis.add(split[k]);
            } else {
                level = split[k];
                skillName = String.join(" ", skillNameLis);
            }
        }

        if (level.isEmpty() || skillName.isEmpty()) {
            SwordCraftOnline.logDebug("Error parsing armor modifiers.");
            return null;
        }

        return ArmorModifier.generateArmorSkill(skillName, level);
    }

    public static JsonObject rollArmorData(JsonObject armorData) {
        Integer maxReforge = armorData.get("maxReforge").getAsInt();

        // Case 1: This armor has no reforge data
        // or cannot reforge
        if (maxReforge == 0 || maxReforge == null) { return armorData; }

        int disposition = armorData.get("disposition").getAsInt();

        JsonArray newReforgeSkills = new JsonArray();
        for (int i = 0; i < maxReforge; i++) {
            // Rolling disposition / 10 chance for reforge
            if (DispositionHandler.rollReforgeChance(disposition)) {
                String newSkill = ArmorModifierList.getRandomModifier(true);
                int roll = DispositionHandler.rollReforgeLevel(disposition);

                int maxLevel = WeaponModifier.getWeaponMaxLevel(newSkill);
                int segment = 100 / maxLevel;
                int level = roll == 100 ? maxLevel : roll / segment;

                newReforgeSkills.add(newSkill + ": " + level);
            }
        }
        armorData.add("reforgeSkills", newReforgeSkills);
        
        int reforgeCount = armorData.get("reforgeCount").getAsInt() + 1;
        armorData.addProperty("reforgeCount", reforgeCount);

        return armorData;
    }

}
