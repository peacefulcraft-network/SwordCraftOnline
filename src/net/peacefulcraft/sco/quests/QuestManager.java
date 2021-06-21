package net.peacefulcraft.sco.quests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitTask;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.io.IOHandler;
import net.peacefulcraft.sco.mythicmobs.io.IOLoader;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;

public class QuestManager implements Runnable {
    
    /**Stores loaded quests by String name */
    private HashMap<String, Quest> questMap;
    
    /**Stores invalid quests */
    private HashMap<String, Quest> invalidQuests;

    /**Stores quests available on the map currently */
    private HashMap<String, Quest> availableQuests;

    /**Stores current NPCs giving quests currently */
    private ArrayList<ActiveMob> currentQuestGivers;

    private BukkitTask questTask;

    public QuestManager() {
        this.questMap = new HashMap<>();
        this.availableQuests = new HashMap<>();
        this.currentQuestGivers = new ArrayList<>();
        this.invalidQuests = new HashMap<>();
        loadQuests();

        this.questTask = Bukkit.getServer().getScheduler().runTaskTimer(SwordCraftOnline.getPluginInstance(), this, 20, 200);
    }

    public void reload() {
        this.questMap.clear();
        this.availableQuests.clear();
        this.currentQuestGivers.clear();
        this.invalidQuests.clear();

        loadQuests();
    }

    public void loadQuests() {
        this.questMap.clear();
        this.invalidQuests.clear();

        IOLoader<SwordCraftOnline> defaultQuests = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "ExampleQuests.yml", "Quests");
        defaultQuests= new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "ExampleQuests.yml", "Quests");
        List<File> questFiles = IOHandler.getAllFiles(defaultQuests.getFile().getParent());
        List<IOLoader<SwordCraftOnline>> questLoaders = IOHandler.getSaveLoad(SwordCraftOnline.getPluginInstance(), questFiles, "Quests");

        for(IOLoader<SwordCraftOnline> sl : questLoaders) {
            /**
             * Changes regarding Quest loading from .yml
             * Constructing a list of MythicConfigs which store step data
             * One config which stores the quest requirement data
             * The main name of the quest is passed in requirement data, sub-names of quests steps are passed in steps
             */
            List<MythicConfig> stepConfigs = new ArrayList<MythicConfig>();
            MythicConfig mainConfig = null;
            for(String name : sl.getCustomConfig().getConfigurationSection("").getKeys(false)) {
                MythicConfig mc = new MythicConfig(name, sl.getFile(), sl.getCustomConfig());

                //Does config have proper name
                String innerName = sl.getCustomConfig().getString(name + ".Name");
                if(innerName == null || innerName.isEmpty()) {
                    SwordCraftOnline.logInfo("[Quest Manager] Failed to load quest config: " + name + ". No config name.");
                    continue;
                }
                
                //Is config the main data config
                boolean isMain = sl.getCustomConfig().getBoolean(name + ".isMain");
                isMain = sl.getCustomConfig().getBoolean(name + ".IsMain", isMain);
                if(isMain) {
                    mainConfig = mc;
                } else {
                    stepConfigs.add(mc);
                }
            }

            // Catching no main configs
            if(mainConfig == null) {
                continue;
            }

            String qName = mainConfig.getString("Name");
            String file = sl.getFile().getPath();
            String internalName = sl.getFile().getName();
            Quest q = new Quest(file, internalName, stepConfigs, mainConfig);
            
            //We save all invalid quests for reference
            if(q.isInvalid()) { 
                this.invalidQuests.put(qName, q);
                SwordCraftOnline.logDebug("[Quest Manager] Marked invalid: " + qName);
            } else {
                this.questMap.put(qName, q);
                SwordCraftOnline.logInfo("[Quest Manager] Loaded: " + qName + ", size: " + q.getSize());
            }
        }
        SwordCraftOnline.logInfo("[Quest Manager] Loading complete!");
    }

    public void postInitializeTask() {
        // Once quests are loaded we go through and assign
        // story quests to their relative quest givers
        // At this point all persistent mobs should be loaded
        ArrayList<ActiveMob> lis = SwordCraftOnline.getPluginInstance().getMobManager().getStoryQuestGivers();
        for(Quest q : getStoryQuests()) {
            for(ActiveMob am : lis) {
                for(int i = 0; i < q.getSize(); i++) {
                    if(!q.getQuestStep(i).usesGiver()) { continue; }
                    String giverName = q.getQuestStep(i).getGiverName();
                    if(giverName == null) { continue; } 
                    if(am.getDisplayName().equalsIgnoreCase(giverName) && am.getQuest() == null) {
                        SwordCraftOnline.logDebug("[Quest Manager] Assigning: " + q.getQuestStep(i).getName() + ", to: " + am.getDisplayName());
                        am.setQuest(q);
                        this.currentQuestGivers.add(am);
                    }
                }
            }
        }
        SwordCraftOnline.logDebug("[Quest Manager] Post initizalization complete!");
    }

    @Override
    public void run() {
        ArrayList<ActiveMob> lis = SwordCraftOnline.getPluginInstance().getMobManager().getQuestGivers();
        ArrayList<Quest> nonStory = getNonStoryQuests();

        // Checking all current quest givers for expired quests
        Iterator<ActiveMob> iter = currentQuestGivers.iterator();
        while(iter.hasNext()) {
            ActiveMob am = iter.next();

            if(am.getQuest() != null && !am.getQuest().isStoryQuest() && am.checkQuestAssignment()) {
                am.setQuest(null);
                availableQuests.remove(am.getQuest().getQuestName());
                iter.remove();

                SwordCraftOnline.logDebug("[Quest Manager] Removing assigned quest on: " + am.getDisplayName());
            }
        }

        // Checking situational fraction of available quest givers
        // If for some reason there is less than 4 we default to max size
        int upperLimit = lis.size() < 4 ? lis.size() : lis.size()/4;
        for(int i = 0; i < upperLimit; i++) {
            // Selecting random AM and checking if it has quest
            ActiveMob am = lis.get(SwordCraftOnline.r.nextInt(lis.size()));
            if(am.getQuest() != null) { continue; }

            // If quest is already available we don't care
            if(nonStory.size() <= 0) { continue; }
            Quest q = nonStory.get(SwordCraftOnline.r.nextInt(nonStory.size()));
            if(availableQuests.containsKey(q.getQuestName())) { continue; }

            am.setQuest(q);
            availableQuests.put(q.getQuestName(), q);
            this.currentQuestGivers.add(am);            

            SwordCraftOnline.logDebug("[Quest Manager] Assigning quest: " + q.getQuestName() + ", to: " + am.getDisplayName());
        }
    }

    /**
     * Marks given quest name deleted in system.
     * This prevents quest from being distributed, but can still be loaded and completed
     * @param name Name of quest
     */
    public void markDeleted(String name) {
        Quest q = questMap.get(name);
        if(q == null) { return; }
        
        q.setDeleted(true);

        IOLoader<SwordCraftOnline> file = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), q.getFile(), "Quests");
        FileConfiguration config = file.getCustomConfig();

        config.set("IsDeleted", true);

        try{
            config.save(file.getFile());
            SwordCraftOnline.logInfo("[Quest Manager] Successfully saved: " + q.getFile());
        } catch(IOException ex) {
            SwordCraftOnline.logInfo("[Quset Manager] Failed to save: " + q.getFile());
        }
    }

    /**
     * Iterates over quests and finds quests marked deleted
     * @return List of quests
     */
    public ArrayList<Quest> getDeletedQuests() {
        ArrayList<Quest> ret = new ArrayList<>();
        for(Quest q : questMap.values()) {
            if(q.isDeleted()) {
                ret.add(q);
            }
        }
        return ret;
    }

    /**
     * Fetches all non-story quests in map that aren't marked deleted
     * @return List of quests
     */
    private ArrayList<Quest> getNonStoryQuests() {
        ArrayList<Quest> ret = new ArrayList<>();
        for(Quest q : questMap.values()) {
            if(q.isStoryQuest() || q.isDeleted()) { continue; }
            ret.add(q);
        }
        return ret;
    }

    /**
     * Fetches all story quests in map
     * @return List of quests
     */
    private ArrayList<Quest> getStoryQuests() {
        ArrayList<Quest> ret = new ArrayList<>();
        for(Quest q : questMap.values()) {
            if(q.isStoryQuest()) {
                ret.add(q);
            }
        }
        return ret;
    }

    /**
     * Enables and disables quest giving task
     */
    public void toggleQuestTask() {
        if(questTask.isCancelled()) {
            this.questTask = Bukkit.getServer().getScheduler().runTaskTimer(SwordCraftOnline.getPluginInstance(), this, 20, 2400);
        } else {
            this.questTask.cancel();
        }
    }

    /**@return new ActiveQuest instance if quest String valid. Null Otherwise */
    public ActiveQuest activateQuest(String quest, SCOPlayer s) {
        Quest q = questMap.get(quest);
        if(q == null) { return null; }
        return new ActiveQuest(s, q.copy());
    }

    /**@return Quest from map or null if does not exist */
    public Quest getQuest(String quest) {
        return questMap.get(quest);
    }

    /**
     * Checks if Active Mob is registered current quest giver
     * @param am Active Mob we are checking against
     * @return True if registered, false otherwise
     */
    public boolean isCurrentQuestGiver(ActiveMob am) {
        return this.currentQuestGivers.contains(am);
    }
}