package net.peacefulcraft.sco.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.swordskills.weaponskills.WeaponModifier;
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
    public abstract JsonObject getPassiveData();

    /**
     * Contains weapons active weapon skil data
     * 
     * @return
     */
    public abstract JsonObject getActiveData();

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
        //HashMap<String, Integer> actives = new HashMap<>();
        //HashMap<String, Integer> passives = new HashMap<>();
        JsonObject activeObj = new JsonObject();
        JsonObject passiveObj = new JsonObject();

        for(int i = 0; i < lore.size(); i++) {
            String s = lore.get(i);
            if(ChatColor.stripColor(s).equalsIgnoreCase("Active Skill Bonuses:")) {
                int j = i + 1;
                passiveLoop:
                while(j < lore.size() && !lore.get(j).isEmpty()) {
                    if(ChatColorUtil.getColor(lore.get(j)).contains(ChatColor.GOLD)) { continue; }
                    try {
                        WeaponModifier wm = WeaponAttributeHolder.parseWeaponModifier(lore, j);
                        if(wm == null) { continue; }
                       // actives.put(wm.getName(), RomanNumber.romanToDecimal(wm.getLevel()));
                       activeObj.addProperty(wm.getName(), RomanNumber.romanToDecimal(wm.getLevel()));
                    } catch(RuntimeException ex) {
                        ex.printStackTrace();
                        break passiveLoop;
                    }
                    j++;
                }
                //obj.add(WeaponModifierType.ACTIVE.toString(), new JsonElement(){
                obj.add(WeaponModifierType.ACTIVE.toString(), activeObj);
                i = j - 1;
            }
            if(ChatColor.stripColor(s).equalsIgnoreCase("Passive Skill Bonuses:")) {
                int j = i + 1;
                activeLoop:
                while(j < lore.size() && !lore.get(j).isEmpty()) {
                    if(ChatColorUtil.getColor(lore.get(j)).contains(ChatColor.GOLD)) { continue; }
                    try {
                        WeaponModifier wm = WeaponAttributeHolder.parseWeaponModifier(lore, j);
                        if(wm == null) { continue; }
                        passiveObj.addProperty(wm.getName(), RomanNumber.romanToDecimal(wm.getLevel()));
                    } catch(RuntimeException ex) {
                        ex.printStackTrace();
                        break activeLoop;
                    }
                    j++;
                }
                //out.put(WeaponModifierType.PASSIVE, passiveModifiers);
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
     * @param passive Passive skill data of weapon
     * @param active Active skill data of weapon
     * @return modified item stack
     */
    public static ItemStack applyLore(ItemStack item, JsonObject passive, JsonObject active) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();

        if(active != null && !active.entrySet().isEmpty()) {
            lore.add("");
            lore.add(ChatColor.LIGHT_PURPLE + "Active Skill Bonuses:");
            for(Entry<String, JsonElement> entry : active.entrySet()) {
                lore.add(ChatColor.GOLD + " -" + entry.getKey() + " " + RomanNumber.toRoman(entry.getValue().getAsInt()));
            }
        }
        if(passive != null && !passive.entrySet().isEmpty()) {
            lore.add("");
            lore.add(ChatColor.LIGHT_PURPLE + "Passive Skill Bonuses:");
            for(Entry<String, JsonElement> entry : passive.entrySet()) {
                lore.add(ChatColor.GOLD + " -" + entry.getKey() + " " + RomanNumber.toRoman(entry.getValue().getAsInt()));
            }
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
                        SwordCraftOnline.logDebug("[WeaponAttributeHolder] Added Active Weapon Modifier: " + WeaponModifier.parseName(wm));
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
                        SwordCraftOnline.logDebug("[WeaponAttributeHolder] Added Passive Weapon Modifier: " + WeaponModifier.parseName(wm));
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

}
