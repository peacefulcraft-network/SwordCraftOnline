package net.peacefulcraft.sco.swordskills;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;

/**
 * Handles relevant information on sword skill classing
 * and organization
 */
public class SwordSkillDataManager {

    private static List<String> ssLis = new ArrayList<>();

    private static HashMap<SwordSkillType, List<String>> ssType = new HashMap<>();

    private static HashMap<ItemTier, List<String>> ssTier = new HashMap<>();

    public SwordSkillDataManager() {

        // Initialize ssType lists
        for (SwordSkillType type : SwordSkillType.values()) {
            ssType.put(type, new ArrayList<>());
        }

        for (ItemTier tier : ItemTier.values()) {
            ssTier.put(tier, new ArrayList<>());
        }

        // Processing type organization
        for (String name : ssLis) {
            name = name.replaceAll(":", "");
            name = name.replaceAll(" ", "");

            ItemIdentifier identifier = ItemIdentifier.generateIdentifier(name, 1);
            if(identifier == null) { 
                SwordCraftOnline.logDebug("[Sword Skill Data Manager] Failed to load: " + name);
                continue;
            }

            SwordSkillProvider prov = (SwordSkillProvider)identifier;
            ssType.get(prov.getType()).add(name);

            for (ItemTier tier : prov.getAllowedTiers()) {
                ssTier.get(tier).add(name);
            }
        }

        SwordCraftOnline.logDebug("[SwordSkill Data Manager] Loading complete!");

    }

    /**
     * Returns 10 entries from sword skill list with appropriate hover text
     * 
     * @param pageNumber
     * @return
     */
    public TextComponent getInfoPage(int pageNumber) {
        pageNumber = pageNumber == 0 ? 0 : pageNumber - 1;
        TextComponent out = new TextComponent();
        out.addExtra(ChatColor.GOLD + "Sword Skill List:\n");

        for(int i = 0 + (pageNumber * 10); i <= 9 + (pageNumber * 10); i++) {
            try {
                String name = ssLis.get(i);
                SwordCraftOnline.logDebug("[SwordSkill Data Manager] Processing: " + name);
                ItemIdentifier identifier = ItemIdentifier.generateIdentifier(name.replaceAll(":", ""), 1);
                SwordSkillProvider prov = (SwordSkillProvider)identifier;

                TextComponent message = new TextComponent((i+1) + ". " + name);
                message.setColor(ChatColor.GRAY);

                // Reprocessing lore for hover text
                ArrayList<String> lore = prov.getLore();
                String sLore = "";

                sLore += ChatColor.GRAY + name + ": ";

                for(int j = 0; j < lore.size(); j++) {
                    String s = lore.get(j);
                    s = ChatColor.stripColor(s);
                    if(j == 0) {
                        s = s.replaceAll(prov.getTier().toString() + " ", "");
                    }

                    sLore += ChatColor.GRAY + s + "\n";
                }
                
                sLore += ChatColor.GRAY + "Possible Tiers: ";
                for(ItemTier tier : prov.getAllowedTiers()) {
                    sLore += ItemTier.getTierColor(tier) + tier.toString() + ChatColor.GRAY + ", ";
                }
                sLore = StringUtils.substring(sLore, 0, sLore.length() - 2) + ChatColor.GRAY + ".\n";

                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(sLore)));
                out.addExtra(message);
                out.addExtra("\n");
            } catch(IndexOutOfBoundsException e) {
                break;
            }
        }

        String page = ChatColor.GOLD + "Page Number: " + ChatColor.RED + (pageNumber + 1) + ChatColor.GOLD + "/" + (ssLis.size() / 10);
        out.addExtra(page);

        return out;
    }

    /**
     * Fetches sword skills organized by type
     * @param type we search against
     * @return List of string names
     */
    public List<String> getSwordSkills(SwordSkillType type) {
        return Collections.unmodifiableList(ssType.get(type));
    }

    /**
     * Fetches swordskills organized by tier
     * @param tier we search against
     * @return List of string names
     */
    public List<String> getSwordSkills(ItemTier tier) {
        return Collections.unmodifiableList(ssTier.get(tier));
    }

    static {
        ssLis.add("Artificial Hope");
        ssLis.add("Armored Agility");
        ssLis.add("Back Pedal");
        ssLis.add("Blind Rage");
        ssLis.add("Critical Combo");
        ssLis.add("Critical Strike");
        ssLis.add("Dangerous Game: Explosion Tag");
        ssLis.add("Deity Blink");
        ssLis.add("Elephant Heart");
        ssLis.add("Ender Blitz: Control");
        ssLis.add("Ender Blitz");
        ssLis.add("Fatal Teleport");
        ssLis.add("Final Stand: Pure Flame");
        ssLis.add("Final Stand: Venom");
        ssLis.add("Follow Through");
        ssLis.add("Forward Lunge");
        ssLis.add("Full Counter");
        ssLis.add("Glass Cannon");
        ssLis.add("God Of Isolation: Repel");
        ssLis.add("Gods Condition: Hostility");
        ssLis.add("Guardian");
        ssLis.add("Heavy Cloth");
        ssLis.add("High Gear");
        ssLis.add("Hunter Sight");
        ssLis.add("Killer Sense");
        ssLis.add("Leg Day");
        ssLis.add("Overbearing Stance");
        ssLis.add("Over Critical");
        ssLis.add("Overheat");
        ssLis.add("Parry");
        ssLis.add("Perfect Gift");
        ssLis.add("Poor Mans Third Strike");
        ssLis.add("Pristine Grindstone");
        ssLis.add("Quick Hands");
        ssLis.add("Red Dawn");
        ssLis.add("Resolution Wind");
        ssLis.add("Rooted");
        ssLis.add("Serpents Bite");
        ssLis.add("Serrated Blade");
        ssLis.add("Serration Wave");
        ssLis.add("Severe Upward Strike");
        ssLis.add("Simple Punch");
        ssLis.add("Souls Of The Fallen");
        ssLis.add("Speed Override");
        ssLis.add("Supreme Lockdown");
        ssLis.add("Swift Swim");
        ssLis.add("System Break: Accelerant Burst");
        ssLis.add("System Chain: Accelerant Drive");
        ssLis.add("System Chain: Witch Doctor");
        ssLis.add("System Defense: Accelerant Guard");
        ssLis.add("System Defense: Thunderstruck");
        ssLis.add("Ten Commandments: Faith");
        ssLis.add("Ten Commandments: Love");
        ssLis.add("Ten Commandments: Pacifism");
        ssLis.add("Ten Commandments: Patience");
        ssLis.add("Ten Commandments: Piety");
        ssLis.add("Ten Commandments: Repose");
        ssLis.add("Thief Kings Demon Ledger Combo");
        ssLis.add("Thunderstruck");
        ssLis.add("Total Concentration");
        ssLis.add("Unfortunate Divisor");
        ssLis.add("Vampire Carving");
        ssLis.add("Volatile Containment");
        ssLis.add("Weakened Pulse");
        ssLis.add("Wild Slice");
        ssLis.add("Withering Necrolysis");
        ssLis.add("Youthful Idiocracy");
    }

}
