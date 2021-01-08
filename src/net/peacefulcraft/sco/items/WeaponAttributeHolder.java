package net.peacefulcraft.sco.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.peacefulcraft.sco.SwordCraftOnline;
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
     * Parse weapons lore data into json object.
     * Returns ONLY weapon modifiers that are not automatically
     * applied to weapon.
     * 
     * @param item we are parsing
     */
    public static JsonObject parseWeaponData(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();

        JsonObject obj = new JsonObject();
        JsonObject activeObj = new JsonObject();
        JsonObject passiveObj = new JsonObject();

        for(int i = 0; i < lore.size(); i++) {
            String s = lore.get(i);
            if(ChatColor.stripColor(s).equalsIgnoreCase("Active Skill Bonuses:")) {
                int j = i + 1;
                passiveLoop:
                while(j < lore.size() && !lore.get(j).isEmpty()) {
                    if(!ChatColorUtil.getColor(lore.get(j)).contains(ChatColor.GOLD)) {
                        try {
                            WeaponModifier wm = WeaponAttributeHolder.parseWeaponModifier(lore, j);
                            if(wm == null) { continue; }
                            activeObj.addProperty(wm.getName(), RomanNumber.romanToDecimal(wm.getLevel()));
                        } catch(RuntimeException ex) {
                            ex.printStackTrace();
                            break passiveLoop;
                        }
                    }
                    j++;
                }
                obj.add(WeaponModifierType.ACTIVE.toString(), activeObj);
                i = j - 1;
            }
            if(ChatColor.stripColor(s).equalsIgnoreCase("Passive Skill Bonuses:")) {
                int j = i + 1;
                activeLoop:
                while(j < lore.size() && !lore.get(j).isEmpty()) {
                    if(!ChatColorUtil.getColor(lore.get(j)).contains(ChatColor.GOLD)) {
                        try {
                            WeaponModifier wm = WeaponAttributeHolder.parseWeaponModifier(lore, j);
                            if(wm == null) { continue; }
                            passiveObj.addProperty(wm.getName(), RomanNumber.romanToDecimal(wm.getLevel()));
                        } catch(RuntimeException ex) {
                            ex.printStackTrace();
                            break activeLoop;
                        }
                    }
                    j++;
                }
                obj.add(WeaponModifierType.PASSIVE.toString(), passiveObj);
                i = j - 1;
            }
        }

        SwordCraftOnline.logDebug("Parsed Weapon Data: " + obj.toString());
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
        JsonObject activeReforge = reforge.getAsJsonObject(WeaponModifierType.ACTIVE.toString());
        JsonObject passiveReforge = reforge.getAsJsonObject(WeaponModifierType.PASSIVE.toString());

        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();

        if(active != null && !active.entrySet().isEmpty()) {
            if(!ChatColorUtil.getColor(lore.get(lore.size() - 1)).contains(ChatColor.DARK_RED)) { lore.add(""); }
            lore.add(ChatColor.LIGHT_PURPLE + "Active Skill Bonuses:");
            for(Entry<String, JsonElement> entry : active.entrySet()) {
                lore.add(ChatColor.GOLD + " -" + entry.getKey() + " " + RomanNumber.toRoman(entry.getValue().getAsInt()));
            }
            if(activeReforge != null) {
                for(Entry<String, JsonElement> entry : activeReforge.entrySet()) {
                    lore.add(ChatColor.AQUA + " -" + entry.getKey() + " " + RomanNumber.toRoman(entry.getValue().getAsInt()));
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
                for(Entry<String, JsonElement> entry : passiveReforge.entrySet()) {
                    lore.add(ChatColor.AQUA + " -" + entry.getKey() + " " + RomanNumber.toRoman(entry.getValue().getAsInt()));
                }
            }
        }

        if(maxReforge != null) {
            lore.add("");
            lore.add(ChatColor.DARK_RED + "Remaining Weapon Slots:");
            if(maxReforge.get(WeaponModifierType.ACTIVE.toString()) != null) {
                int maxAR = maxReforge.get(WeaponModifierType.ACTIVE.toString()).getAsInt();
                int curAR = activeReforge == null ? 0 : activeReforge.entrySet().size();
                lore.add(ChatColor.DARK_RED + " -Active: " + (maxAR - curAR));
            }
            if(maxReforge.get(WeaponModifierType.PASSIVE.toString()) != null) {
                int maxPR = maxReforge.get(WeaponModifierType.PASSIVE.toString()).getAsInt();
                int curPR = passiveReforge == null ? 0 : passiveReforge.entrySet().size();
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
                        //SwordCraftOnline.logDebug("[WeaponAttributeHolder] Added Active Weapon Modifier: " + WeaponModifier.parseName(wm));
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
                        //SwordCraftOnline.logDebug("[WeaponAttributeHolder] Added Passive Weapon Modifier: " + WeaponModifier.parseName(wm));
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

        JsonObject newActive = new JsonObject();
        for(int i = 0; i < maxAR; i++) {
            // Rolling disposition / 10 chance for reforge
            if((SwordCraftOnline.r.nextInt(99) + 1) <= disposition * 10) {
                String newModifier = WeaponModifierList.getRandomModifier();
                int roll = (1 + disposition * 750) / (SwordCraftOnline.r.nextInt(99) + 1);
                if(roll > 100) { roll = 100; }

                int maxLevel = WeaponModifier.getWeaponMaxLevel(newModifier);
                int segment = 100 / maxLevel;
                int level = roll == 100 ? roll / segment : maxLevel;

                newActive.addProperty(newModifier, level);
            }
        }
        reforge.add(WeaponModifierType.ACTIVE.toString(), newActive);

        JsonObject newPassive = new JsonObject();
        for(int i = 0; i < maxPR; i++) {
            // Rolling disposition / 10 chance for reforge
            if((SwordCraftOnline.r.nextInt(99) + 1) <= disposition * 10) {
                String newModifier = WeaponModifierList.getRandomModifier();
                int roll = (1 + disposition * 750) / (SwordCraftOnline.r.nextInt(99) + 1);
                if(roll > 100) { roll = 100; }

                int maxLevel = WeaponModifier.getWeaponMaxLevel(newModifier);
                int segment = 100 / maxLevel;
                int level = roll == 100 ? roll / segment : maxLevel;

                newPassive.addProperty(newModifier, level);
            }
        }
        reforge.add(WeaponModifierType.PASSIVE.toString(), newPassive);

        weaponData.add("reforge", reforge);
        int reforgeCount = weaponData.get("Reforge Count").getAsInt() + 1;
        weaponData.addProperty("Reforge Count", reforgeCount);
        return weaponData;
    }
}
