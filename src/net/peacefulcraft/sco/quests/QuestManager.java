package net.peacefulcraft.sco.quests;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.io.IOHandler;
import net.peacefulcraft.sco.mythicmobs.io.IOLoader;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;

public class QuestManager {
    
    /**Stores loaded quests by String name */
    private HashMap<String, Quest> questMap;
    
    /**Stores invalid quests */
    private HashMap<String, Quest> invalidQuests;

    public QuestManager() {
        this.questMap = new HashMap<>();
        loadQuests();
    }

    public void loadQuests() {
        this.questMap.clear();

        IOLoader<SwordCraftOnline> defaultQuests = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "ExampleQuests.yml", "Quests");
        defaultQuests= new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "ExampleQuests.yml", "Quests");
        List<File> questFiles = IOHandler.getAllFiles(defaultQuests.getFile().getParent());
        List<IOLoader<SwordCraftOnline>> questLoaders = IOHandler.getSaveLoad(SwordCraftOnline.getPluginInstance(), questFiles, "Quests");

        for(IOLoader<SwordCraftOnline> sl : questLoaders) {
            /**
             * Changes regarding Quest loading from .yml
             * Constructing a list of MythicConfigs which store step data
             * One config which stores the quest requirement data
             * The main name of the quest is passed in requirement data, sub-names of quests are passed in steps
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

            String qName = mainConfig.getString("Name");
            String file = sl.getFile().getPath();
            String internalName = sl.getFile().getName();
            Quest q = new Quest(file, internalName, stepConfigs, mainConfig);
            
            //We save all invalid quests for reference
            if(q.isInvalid()) { 
                this.invalidQuests.put(qName, q);
            } else {
                this.questMap.put(qName, q);
            }
        }
        SwordCraftOnline.logInfo("[Quest Manager] Loading complete!");
    }

    /**@return new ActiveQuest instance if quest String valid. Null Otherwise */
    public ActiveQuest activateQuest(String quest, SCOPlayer s) {
        Quest q = questMap.get(quest);
        if(q == null) { return null; }
        return new ActiveQuest(s, q);
    }

    /**@return Quest from map or null if does not exist */
    public Quest getQuest(String quest) {
        return questMap.get(quest);
    }
}