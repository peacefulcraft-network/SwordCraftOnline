package net.peacefulcraft.sco.tutorial;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.announcer.Announcer;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.io.IOHandler;
import net.peacefulcraft.sco.mythicmobs.io.IOLoader;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;

public class TutorialManager implements Runnable{

    /**Time before player is automatically removed after checkpoint */
    private int cooldown;

    /** List of players in tutorial */
    private static HashMap<SCOPlayer, Long> players;
        public static Set<SCOPlayer> getPlayers() {
            return Collections.unmodifiableSet(players.keySet());
        }

    /** Storing tutorial bots by display name of mob */
    private HashMap<String, TutorialBot> bots = new HashMap<String, TutorialBot>();
        public Map<String, TutorialBot> getTutorialBotMap() { return Collections.unmodifiableMap(bots); }

    private BukkitTask tutorialTask;

    public TutorialManager() {
        players = new HashMap<SCOPlayer, Long>();
        this.cooldown = SwordCraftOnline.getSCOConfig().getTutorialCooldown();
        loadTutorials();

        this.tutorialTask = Bukkit.getServer().getScheduler().runTaskTimer(SwordCraftOnline.getPluginInstance(), this, 1200, 10);
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

                boolean usesMob = sl.getCustomConfig().getBoolean(name + ".UsesMythicMob");
                Map<String, MythicMob> mobList = SwordCraftOnline.getPluginInstance().getMobManager().getTutorialList();
                if(!mobList.containsKey(name) && usesMob) {
                    SwordCraftOnline.logInfo("[Tutorial Manager] Attempted to load file with no corresponding Mythic Mob: " + name);
                    continue;
                }

                TutorialBot bot = new TutorialBot(file, name, mc);
                if(usesMob) {
                    bot.setMythicMob(mobList.get(name));
                }

                this.bots.put(name, bot);
            }
        }
        SwordCraftOnline.logInfo("[Tutorial Manager] Loading complete!");
    }

    @Override
    public void run() {
        for(SCOPlayer s : getPlayers()) {
            long secondsLeft = ((players.get(s)/1000)+cooldown) - (System.currentTimeMillis()/1000);
            //If cooldown expire or not on floor 1
            if(secondsLeft < 0 || s.getFloor() != 1) {
                leaveTutorial(s);
            }
        }
    }

    public void toggleTutorialTask() {
        if(tutorialTask.isCancelled()) {
            this.tutorialTask = Bukkit.getServer().getScheduler().runTaskTimer(SwordCraftOnline.getPluginInstance(), this, 20, 30);
        } else {
            this.tutorialTask.cancel();
        }
    }

    /**Called on events to give player time to get to next point */
    public void updatePlayerTime(SCOPlayer s) {
        players.put(s, System.currentTimeMillis());
    }

    /**Main call to process players joining tutorial */
    public void joinTutorial(SCOPlayer s) {
        players.put(s, System.currentTimeMillis());
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
        Announcer.messagePlayer(s, "You have left the tutorial!", true);
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
        return getPlayers().contains(s);
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