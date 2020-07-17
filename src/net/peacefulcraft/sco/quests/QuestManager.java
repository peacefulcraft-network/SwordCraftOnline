package net.peacefulcraft.sco.quests;

import java.io.File;
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
            for(String name : sl.getCustomConfig().getConfigurationSection("").getKeys(false)) {
                MythicConfig mc = new MythicConfig(name, sl.getFile(), sl.getCustomConfig());
                String file = sl.getFile().getPath();

                String qName = sl.getCustomConfig().getString(name + ".Name");
                if(qName == null) {
                    SwordCraftOnline.logInfo("[Quest Manager] Failed to load quest: " + name + " due to no quest name.");
                    continue;
                }

                Quest q = new Quest(file, name, mc);
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