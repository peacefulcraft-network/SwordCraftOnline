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
import net.peacefulcraft.sco.swordskills.weaponskills.WeaponModifier;
import net.peacefulcraft.sco.utilities.RomanNumber;

/**
 * Sub category of ItemIdentifier For weapons that need lore parsed into
 * applicable effects
 */
public interface WeaponAttributeHolder {
    
    public abstract JsonObject getPassiveData();

    public abstract JsonObject getActiveData();

    public static ItemStack applyLore(ItemStack item, JsonObject passive, JsonObject active) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();

        if(active != null && !active.entrySet().isEmpty()) {
            lore.add("");
            lore.add("Active Skill Bonuses:");
            for(Entry<String, JsonElement> entry : active.entrySet()) {
                lore.add(entry.getKey() + " " + RomanNumber.toRoman(entry.getValue().getAsInt()));
            }
        }
        if(passive != null && !passive.entrySet().isEmpty()) {
            lore.add("");
            lore.add("Passive Skill Bonuses:");
            for(Entry<String, JsonElement> entry : passive.entrySet()) {
                lore.add(entry.getKey() + " " + RomanNumber.toRoman(entry.getValue().getAsInt()));
            }
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static HashMap<String, ArrayList<WeaponModifier>> parseLore(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();

        HashMap<String, ArrayList<WeaponModifier>> out = new HashMap<>();

        loreLoop:
        for(int i = 0; i <= lore.size(); i++) {
            String s = lore.get(i);
            if(ChatColor.stripColor(s).contains("Active")) {
                ArrayList<WeaponModifier> activeModifiers = new ArrayList<>();

                int j = i + 1;
                while(lore.get(j).isEmpty() || j != lore.size()) {

                    try {
                        WeaponModifier wm = WeaponAttributeHolder.parseWeaponModifier(lore, j);
                        activeModifiers.add(wm);
                    } catch(RuntimeException ex) {
                        break;
                    }

                    j++;
                    if(j >= lore.size()) { break loreLoop; }
                }
                out.put("active", activeModifiers);

                i = j;
            }
            if(ChatColor.stripColor(s).contains("Passive")) {
                ArrayList<WeaponModifier> passiveModifiers = new ArrayList<>();

                int j = i + 1;
                while(lore.get(j).isEmpty() || j != lore.size()) {
                    
                    try {
                        WeaponModifier wm = WeaponAttributeHolder.parseWeaponModifier(lore, j);
                        passiveModifiers.add(wm);
                    } catch(RuntimeException ex) {
                        break;
                    }

                    j++;
                    if(j >= lore.size()) { break loreLoop; }
                }
                out.put("passive", passiveModifiers);

                i = j;
            }
        }    
        return out;  
    }

    /**
     * Parses weapon modifier from lore data
     * 
     * @param lore of item
     * @param j Current index we are parsing
     * @return Weaponmodifier
     * @throws RuntimeException on invalid parsing
     */
    public static WeaponModifier parseWeaponModifier(List<String> lore, int j) throws RuntimeException {
        String[] split = ChatColor.stripColor(lore.get(j)).split(" ");
        String level = "";
        String skillName = "";
        List<String> skillNameLis = new ArrayList<>();
        
        int k = 0;
        for(; k <= split.length; k++) {
            try {
                RomanNumber.romanToDecimal(split[k]);
                level = split[k];
                skillName = String.join(" ", skillNameLis);
            } catch(RuntimeException ex) {
                skillNameLis.add(split[k]);
            }
        }
        if(level.isEmpty() || skillName.isEmpty()) {
            throw new RuntimeException("Error parsing weapon modifiers.");
        }

        return WeaponModifier.generateWeaponSkill(skillName, level);
    }

}
