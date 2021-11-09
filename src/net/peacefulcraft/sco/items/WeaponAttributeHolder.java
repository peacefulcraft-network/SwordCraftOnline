package net.peacefulcraft.sco.items;

import java.util.ArrayList;
import java.util.HashMap;
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
import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;
import net.peacefulcraft.sco.swordskills.weaponskills.WeaponModifier;
import net.peacefulcraft.sco.swordskills.weaponskills.WeaponModifierList;
import net.peacefulcraft.sco.swordskills.weaponskills.WeaponModifier.WeaponModifierType;
import net.peacefulcraft.sco.utilities.ChatColorUtil;
import net.peacefulcraft.sco.utilities.RomanNumber;

/**
 * Sub category of ItemIdentifier For weapons that need lore parsed into
 * applicable effects
 */
public interface WeaponAttributeHolder {
    
    /**
     * Contains weapons passive weapon skill data
     * 
     * @return
     */
    public abstract JsonObject getWeaponData();

    /**
     * This weapons roll disposition. 1+x/x
     * 
     * @return
     */
    public abstract Integer getDisposition();

    /**
     * Thsi weapons max reforge slots in each category
     * 
     * @return
     */
    public abstract JsonObject getMaxReforge();

    /**
     * Returns elemental typing of weapon
     * 
     * @return
     */
    public abstract ModifierType[] getModifierTypes();

    /**
     * Parse weapons lore data into json object.
     * Returns ALL accurate weapon data from lore of item
     * 
     * @param item we are parsing
     */
    public static JsonObject parseWeaponData(ItemStack item, ItemIdentifier identifier) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();

        JsonObject obj = new JsonObject();
        JsonArray activeReObj = new JsonArray();
        JsonArray passiveReObj = new JsonArray();
        JsonObject reforgeObj = new JsonObject();

        JsonObject activeObj = new JsonObject();
        JsonObject passiveObj = new JsonObject();
        Integer reforgeCount = 0;

        for(int i = 0; i < lore.size(); i++) {
            String s = lore.get(i);
            if(ChatColor.stripColor(s).contains("Total Weapon Reforges:")) {
                String[] split = s.split(" ");
                reforgeCount = Integer.valueOf(split[3]);
            }
            if(ChatColor.stripColor(s).equalsIgnoreCase("Active Skill Bonuses:")) {
                int j = i + 1;
                passiveLoop:
                while(j < lore.size() && !lore.get(j).isEmpty()) {
                    try {
                        WeaponModifier wm = WeaponAttributeHolder.parseWeaponModifier(lore, j);
                        if(wm == null) { continue; }
                        if(!ChatColorUtil.getColor(lore.get(j)).contains(ChatColor.GOLD)) {
                            passiveReObj.add(wm.getName() + ": " + RomanNumber.romanToDecimal(wm.getLevel()));
                        } else if(ChatColorUtil.getColor(lore.get(j)).contains(ChatColor.GOLD)) {
                            passiveObj.addProperty(wm.getName(), RomanNumber.romanToDecimal(wm.getLevel()));
                        }
                    } catch(RuntimeException ex) {
                        ex.printStackTrace();
                        break passiveLoop;
                    }
                    j++;
                }
                reforgeObj.add(WeaponModifierType.ACTIVE.toString(), activeReObj);
                obj.add(WeaponModifierType.ACTIVE.toString(), activeObj);
                i = j - 1;
            }
            if(ChatColor.stripColor(s).equalsIgnoreCase("Passive Skill Bonuses:")) {
                int j = i + 1;
                activeLoop:
                while(j < lore.size() && !lore.get(j).isEmpty()) {
                    try {
                        WeaponModifier wm = WeaponAttributeHolder.parseWeaponModifier(lore, j);
                        if(wm == null) { continue; }
                        if(!ChatColorUtil.getColor(lore.get(j)).contains(ChatColor.GOLD)) {
                            activeReObj.add(wm.getName() + ": " + RomanNumber.romanToDecimal(wm.getLevel()));
                        } else if(ChatColorUtil.getColor(lore.get(j)).contains(ChatColor.GOLD)) {
                            activeObj.addProperty(wm.getName(), RomanNumber.romanToDecimal(wm.getLevel()));
                        }
                    } catch(RuntimeException ex) {
                        ex.printStackTrace();
                        break activeLoop;
                    }
                    j++;
                }
                reforgeObj.add(WeaponModifierType.PASSIVE.toString(), passiveReObj);
                obj.add(WeaponModifierType.PASSIVE.toString(), passiveObj);
                i = j - 1;
            }
        }
        obj.addProperty("Reforge Count", reforgeCount);
        obj.add("reforge", reforgeObj);
        if(identifier != null) {
            obj.add("Max Reforge", ((WeaponAttributeHolder)identifier).getMaxReforge());
        }
        return obj;
    }

    /**
     * Converts Weapon Attribute Holder data into strings
     * and applies onto lore of item
     * 
     * @param item we are modifying lore of
     * @param weaponData JsonObject of passive/active data
     * @return modified item stack
     */
    public static ItemStack applyLore(ItemStack item, JsonObject weaponData) {
        JsonObject active = weaponData.getAsJsonObject(WeaponModifierType.ACTIVE.toString());
        JsonObject passive = weaponData.getAsJsonObject(WeaponModifierType.PASSIVE.toString());
        JsonObject maxReforge = weaponData.getAsJsonObject("Max Reforge");
        
        JsonObject reforge = null;
        try {
            reforge = weaponData.getAsJsonObject("reforge");            
        } catch(Exception ex) {
            reforge = new JsonObject();
        }
        JsonArray activeReforge = reforge.getAsJsonArray(WeaponModifierType.ACTIVE.toString());
        JsonArray passiveReforge = reforge.getAsJsonArray(WeaponModifierType.PASSIVE.toString());

        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();

        if(active != null && !active.entrySet().isEmpty()) {
            if(!ChatColorUtil.getColor(lore.get(lore.size() - 1)).contains(ChatColor.DARK_RED)) { lore.add(""); }
            lore.add(ChatColor.LIGHT_PURPLE + "Active Skill Bonuses:");
            for(Entry<String, JsonElement> entry : active.entrySet()) {
                lore.add(ChatColor.GOLD + " -" + entry.getKey() + " " + RomanNumber.toRoman(entry.getValue().getAsInt()));
            }
            if(activeReforge != null) {
                Iterator<JsonElement> iter = activeReforge.iterator();
                while(iter.hasNext()) {
                    String s = iter.next().getAsString();
                    String[] split = s.split(": ");
                    lore.add(ChatColor.AQUA + " -" + split[0] + " " + RomanNumber.toRoman(Integer.valueOf(split[1])));
                }
            }
        }
        if(passive != null && !passive.entrySet().isEmpty()) {
            if(!ChatColorUtil.getColor(lore.get(lore.size() - 1)).contains(ChatColor.DARK_RED)) { lore.add(""); }
            lore.add(ChatColor.LIGHT_PURPLE + "Passive Skill Bonuses:");
            for(Entry<String, JsonElement> entry : passive.entrySet()) {
                lore.add(ChatColor.GOLD + " -" + entry.getKey() + " " + RomanNumber.toRoman(entry.getValue().getAsInt()));
            }
            if(passiveReforge != null) {
                Iterator<JsonElement> iter = passiveReforge.iterator();
                while(iter.hasNext()) {
                    String s = iter.next().getAsString();
                    String[] split = s.split(": ");
                    lore.add(ChatColor.AQUA + " -" + split[0] + " " + RomanNumber.toRoman(Integer.valueOf(split[1])));
                }
            }
        }

        if(maxReforge != null) {
            lore.add("");
            lore.add(ChatColor.DARK_RED + "Remaining Weapon Slots:");
            if(maxReforge.get(WeaponModifierType.ACTIVE.toString()) != null) {
                int maxAR = maxReforge.get(WeaponModifierType.ACTIVE.toString()).getAsInt();
                int curAR = activeReforge == null ? 0 : activeReforge.size();
                lore.add(ChatColor.DARK_RED + " -Active: " + (maxAR - curAR));
            }
            if(maxReforge.get(WeaponModifierType.PASSIVE.toString()) != null) {
                int maxPR = maxReforge.get(WeaponModifierType.PASSIVE.toString()).getAsInt();
                int curPR = passiveReforge == null ? 0 : passiveReforge.size();
                lore.add(ChatColor.DARK_RED + " -Passive: " + (maxPR - curPR));
            }
        }
        Integer reforgeCount = weaponData.get("Reforge Count").getAsInt();
        if(reforgeCount != null && reforgeCount > 0) {
            lore.add(ChatColor.DARK_RED + "Total Weapon Reforges: " + reforgeCount);
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Parses lore of item stack into appropriate weapon modifiers
     * organizing them by passive/active
     * 
     * @param item item stack we are parsing
     * @return map of passive,active modifiers
     */
    public static HashMap<WeaponModifierType, ArrayList<WeaponModifier>> parseLore(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();

        HashMap<WeaponModifierType, ArrayList<WeaponModifier>> out = new HashMap<>();
        ArrayList<WeaponModifier> activeModifiers = new ArrayList<>();
        ArrayList<WeaponModifier> passiveModifiers = new ArrayList<>();

        for(int i = 0; i < lore.size(); i++) {
            String s = lore.get(i);
            if(ChatColor.stripColor(s).equalsIgnoreCase("Active Skill Bonuses:")) {
                int j = i + 1;
                passiveLoop:
                while(j < lore.size() && !lore.get(j).isEmpty()) {
                    try {
                        WeaponModifier wm = WeaponAttributeHolder.parseWeaponModifier(lore, j);
                        if(wm == null) { continue; }
                        activeModifiers.add(wm);
                    } catch(RuntimeException ex) {
                        ex.printStackTrace();
                        break passiveLoop;
                    }
                    j++;
                }
                out.put(WeaponModifierType.ACTIVE, activeModifiers);
                i = j - 1;
            }
            if(ChatColor.stripColor(s).equalsIgnoreCase("Passive Skill Bonuses:")) {
                int j = i + 1;
                activeLoop:
                while(j < lore.size() && !lore.get(j).isEmpty()) {
                    try {
                        WeaponModifier wm = WeaponAttributeHolder.parseWeaponModifier(lore, j);
                        if(wm == null) { continue; }
                        passiveModifiers.add(wm);
                    } catch(RuntimeException ex) {
                        ex.printStackTrace();
                        break activeLoop;
                    }
                    j++;
                }
                out.put(WeaponModifierType.PASSIVE, passiveModifiers);
                i = j - 1;
            }
        }    
        return out;  
    }

    /**
     * Parses weapon modifier from lore data
     * SHOULD NOT be called individually. Only inside
     * other parse method
     * 
     * @param lore of item
     * @param j Current index we are parsing
     * @return Weaponmodifier
     * @throws RuntimeException on invalid parsing
     */
    public static WeaponModifier parseWeaponModifier(List<String> lore, int j) {
        String[] split = ChatColor.stripColor(lore.get(j)).replace(" -", "").split(" ");
        String level = "";
        String skillName = "";
        List<String> skillNameLis = new ArrayList<>();
        
        int k = 0;
        for(; k < split.length; k++) {
            Integer validityTest = RomanNumber.romanToDecimal(split[k]);
            if(validityTest == null) {
                skillNameLis.add(split[k]);
            } else {
                level = split[k];
                skillName = String.join(" ", skillNameLis);
            }
        }
        if(level.isEmpty() || skillName.isEmpty()) {
            SwordCraftOnline.logDebug("Error parsing weapon modifiers.");
            return null;
        }

        return WeaponModifier.generateWeaponSkill(skillName, level);
    }

    /**
     * Rolls reforge weapon skills into new skills
     * Only replaces reforge-able skills
     * 
     * @param weaponData raw Weapon Data from WAH
     * @return Modified json object
     */
    public static JsonObject rollWeaponData(JsonObject weaponData) {        
        JsonObject maxReforge = weaponData.getAsJsonObject("Max Reforge");
        // Case 1: This weapon has no reforge data
        if(maxReforge == null) { return weaponData; }
        
        // Case 2: This weapon has reforge data, but cannot reforge
        int maxAR = maxReforge.get(WeaponModifierType.ACTIVE.toString()).getAsInt();
        int maxPR = maxReforge.get(WeaponModifierType.PASSIVE.toString()).getAsInt();
        if(maxAR == 0 && maxPR == 0) { return weaponData; }

        int disposition = weaponData.get("Disposition").getAsInt();

        JsonObject reforge = weaponData.getAsJsonObject("reforge");
        if(reforge == null) { reforge = new JsonObject(); }

        JsonArray newActiveAr = new JsonArray();
        for(int i = 0; i < maxAR; i++) {
            // Rolling disposition / 10 chance for reforge
            if((SwordCraftOnline.r.nextInt(9) + 1) <= disposition * 2) {
                String newModifier = WeaponModifierList.getRandomModifier(true);
                int roll = (1 + disposition * 750) / (SwordCraftOnline.r.nextInt(99) + 1);
                if(roll > 100) { roll = 100; }

                int maxLevel = WeaponModifier.getWeaponMaxLevel(newModifier);
                int segment = 100 / maxLevel;
                int level = roll == 100 ? maxLevel : roll / segment;

                newActiveAr.add(newModifier + ": " + level);
            }
        }
        reforge.add(WeaponModifierType.ACTIVE.toString(), newActiveAr);

        JsonArray newPassiveAr = new JsonArray();
        for(int i = 0; i < maxPR; i++) {
            // Rolling disposition / 10 chance for reforge
            if((SwordCraftOnline.r.nextInt(9) + 1) <= disposition * 2) {
                String newModifier = WeaponModifierList.getRandomModifier(true);
                int roll = (1 + disposition * 750) / (SwordCraftOnline.r.nextInt(99) + 1);
                if(roll > 100) { roll = 100; }

                int maxLevel = WeaponModifier.getWeaponMaxLevel(newModifier);
                int segment = 100 / maxLevel;
                int level = roll == 100 ? maxLevel : roll / segment;

                newPassiveAr.add(newModifier + ": " + level);
            }
        }
        reforge.add(WeaponModifierType.PASSIVE.toString(), newPassiveAr);

        weaponData.add("reforge", reforge);
        int reforgeCount = weaponData.get("Reforge Count").getAsInt() + 1;
        weaponData.addProperty("Reforge Count", reforgeCount);
        return weaponData;
    }

}
