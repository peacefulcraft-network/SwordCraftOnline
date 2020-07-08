package net.peacefulcraft.sco.tutorial;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.io.IOHandler;
import net.peacefulcraft.sco.mythicmobs.io.IOLoader;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MobManager;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;

public class TutorialManager {

    /** List of players in tutorial */
    private static ArrayList<SCOPlayer> players;

    public static List<SCOPlayer> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    /** Storing tutorial bots by display name of mob */
    private HashMap<String, TutorialBot> bots = new HashMap<String, TutorialBot>();

    public TutorialManager() {
        players = new ArrayList<SCOPlayer>();
        loadTutorials();
    }

    public void loadTutorials() {
        this.bots.clear();

        IOLoader<SwordCraftOnline> defaultTutorials = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "ExampleTutorial.yml", "Tutorial");
        defaultTutorials = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "ExampleTutorial.yml", "Tutorial");
        List<File> tutorialFiles = IOHandler.getAllFiles(defaultTutorials.getFile().getParent());
        List<IOLoader<SwordCraftOnline>> tutorialLoaders = IOHandler.getSaveLoad(SwordCraftOnline.getPluginInstance(), tutorialFiles, "Tutorial");
    
        for(IOLoader<SwordCraftOnline> sl : tutorialLoaders) {
            for(String name : sl.getCustomConfig().getConfigurationSection("").getKeys(false)) {
                MythicConfig mc = new MythicConfig(name, sl.getFile(), sl.getCustomConfig());
                String file = sl.getFile().getPath();

                try{
                    TutorialBots.valueOf(name.toUpperCase());
                } catch(IllegalArgumentException ex) {
                    SwordCraftOnline.logInfo("[Tutorial Manager] Attempted to load file with illegal name: " + name);
                    continue;
                }

                SwordCraftOnline.logInfo("DEBUG: " + name);

                Map<String, MythicMob> mobList = SwordCraftOnline.getPluginInstance().getMobManager().getTutorialList();
                if(!mobList.containsKey(name)) {
                    SwordCraftOnline.logInfo("[Tutorial Manager] Attempted to load file with no corresponding Mythic Mob.");
                    continue;
                }

                TutorialBot bot = new TutorialBot(file, name, mc);
                bot.setMythicMob(mobList.get(name));
            }
        }
    }

    /**Main call to process players joining tutorial */
    public void joinTutorial(SCOPlayer s) {
        players.add(s);
    }

    public void joinTutorial(Player p) {
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) {
            SwordCraftOnline.logInfo("Player not yet in SCO attempted to join tutorial: " + p.getName());
            return;
        }
        joinTutorial(s);
    }

    /**Main call to process players leaving tutorial */
    public void leaveTutorial(SCOPlayer s) {
        players.remove(s);
    }

    public void leaveTutorial(Player p) {
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) {
            SwordCraftOnline.logInfo("Player not yet in SCO attempted to leave tutorial: " + p.getName());
            return;
        }
        leaveTutorial(s);
    }

    public boolean isInTutorial(SCOPlayer s) {
        return players.contains(s);
    }

    public boolean isInTutorial(Player p) {
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) { return false; }
        return isInTutorial(s);
    }

    public static String getTutorialChatBar(String name) {
        return "" + ChatColor.RED + "[" + ChatColor.AQUA + name 
            + ChatColor.RED + "]" + ChatColor.WHITE + ": ";
    }

    public enum TutorialBots {
        YUI;
    }
}