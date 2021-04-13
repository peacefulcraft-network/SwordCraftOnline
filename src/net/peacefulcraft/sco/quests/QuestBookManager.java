package net.peacefulcraft.sco.quests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.JsonObject;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.QuestBookInventory;
import net.peacefulcraft.sco.items.CustomDataHolder;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.quests.QuestStep.QuestType;

public class QuestBookManager {
    
    private SCOPlayer s;
        public SCOPlayer getSCOPlayer() { return this.s; }

    /**Players active quests they can complete */
    private HashMap<QuestType, ArrayList<ActiveQuest>> quests = new HashMap<>();
        public Map<QuestType, ArrayList<ActiveQuest>> getQuests() { return Collections.unmodifiableMap(quests); }

    /**
     * Players completed quests 
     */
    private ArrayList<String> completedQuests = new ArrayList<>();
        public void addCompletedQuest(String name) {
            if(completedQuests.contains(name)) { return; }
            completedQuests.add(name);
        }

    public QuestBookManager(SCOPlayer s) {
        this.s = s;
    }

    /**Registers quest to player */
    public void registerQuest(String questName) {
        ActiveQuest aq = SwordCraftOnline.getPluginInstance().getQuestManager().activateQuest(questName, this.s);
        if(aq == null) {
            SwordCraftOnline.logInfo("Failed to register quest + " + questName + " to player " + s.getName());
            return;
        }

        //Fetching quests current step type
        //Storing quest by quest step type
        QuestType qt = aq.getQuestType();
        if(quests.get(qt) == null) {
            quests.put(qt, new ArrayList<ActiveQuest>());
        } else {
            for(ActiveQuest temp : quests.get(qt)) {
                //Double register somehow. We don't care
                if(temp.getName().equals(questName)) {
                    return;
                }
            }
        }

        quests.get(qt).add(aq);

        // Giving quest item to player
        JsonObject obj = new JsonObject();
        obj.addProperty("questName", aq.getName());
        obj.addProperty(
            "description", 
            aq.getItemDescription() + "\n" + aq.getRewardStr() + "\n\n" + aq.getProgressBar()
        );
        obj.addProperty("step", 0);

        ItemIdentifier ident = ItemIdentifier.generateIdentifier("Quest", ItemTier.COMMON, 1);
        ((CustomDataHolder)ident).setCustomData(obj);
        s.getPlayerInventory().addItem(ident);
    }

    /**Unregisters quest to player */
    public void unregisterQuest(String questName) {
        ActiveQuest aq = SwordCraftOnline.getPluginInstance().getQuestManager().activateQuest(questName, s);
        if(aq == null) { return; }

        QuestType type = aq.getQuestType();
        ArrayList<ActiveQuest> lis = quests.get(type);
        lis.remove(aq);

        if(lis.size() == 0) {
            quests.remove(type);
        }
    }

    /**Updates quests storage location in map */
    public void updateQuest(String questName) {
        ActiveQuest aq = SwordCraftOnline.getPluginInstance().getQuestManager().activateQuest(questName, s);
        if(aq == null) { return; }

        //Iterating over every quest in map. Removing old instance
        for(ArrayList<ActiveQuest> lis : quests.values()) {
            Iterator<ActiveQuest> iter = lis.iterator();
            while(iter.hasNext()) {
                ActiveQuest temp = iter.next();
                if(temp.getName().equalsIgnoreCase(questName)) {
                    iter.remove();
                }
            }
        }
        //Registering quest again in new location
        registerQuest(questName);
    }

    public void executeLoop(QuestType type, Event ev) {
        if(quests.get(type) == null) { return; }

        for(ActiveQuest aq : quests.get(type)) {
            aq.execLifeCycle(type, ev);
            
            //Is active quest completed
            if(aq.isCompleted()) {
                completeQuest(aq);
            } else {
                //If quest was not completed we want to update item in their inventory
                QuestBookInventory base = s.getQuestBookInventory();

                int index = 0;
                for(ItemIdentifier ident : base.generateItemIdentifiers()) {
                    if(!ident.getName().equalsIgnoreCase("Quest")) { continue; }

                    CustomDataHolder cus = (CustomDataHolder)ident;
                    JsonObject obj = cus.getCustomData();

                    String name = obj.get("questName").getAsString();
                    if(name.equalsIgnoreCase(aq.getName())) {
                        obj.addProperty(
                            "description", 
                            aq.getItemDescription() + "\n" + aq.getRewardStr() + "\n\n" + aq.getProgressBar()
                        );
                        obj.addProperty("step", aq.getCurrentStep());
                        cus.setCustomData(obj);

                        base.setItem(index, ident);
                    }

                    index++;
                }
            }
        }
    }

    /**
     * Creates map of player data relative to their quests and completed quests
     */
    public void save() {
        //Map of String,? to store by quest name
        //Object will be another map of containing necessary information of step
        //CurrentStep, QuestStep(Step data), etc.
        
        Map<String,Object> saveMap = new HashMap<>();

        //Iterating over all registered active quests
        Map<String, Object> actives = new HashMap<>();
        for(QuestType type : quests.keySet()) {
            for(ActiveQuest aq : quests.get(type)) {
                String name = aq.getName();
                actives.put(name, aq.getSaveData());
            }
        }

        //Saving active quests to map
        saveMap.put("ActiveQuests", actives);
        
        //Saving completed quests to map
        saveMap.put("CompletedQuests", this.completedQuests);
    }

    /**
     * Loads player data from map object
     */
    public void load() {
        //Load map object created from save() somehow
        //Iterate over ActiveQuests key. Read in CurrentStep, StepData key
        //Read in CompletedQuests key
        //TODO: This^
    }

    public boolean isQuestRegistered(String name) {
        Quest q = SwordCraftOnline.getPluginInstance().getQuestManager().getQuest(name);
        if(q == null) { return false; }

        return isQuestRegistered(q);
    }

    /**
     * Fetches active quest by name from player register
     * @param name Name of quest
     * @return ActiveQuest instance if exists, null otherwise
     */
    public ActiveQuest getRegisteredQuest(String name) {
        // Quest step should be registered in here
        for(ArrayList<ActiveQuest> lis : getQuests().values()) {
            for(ActiveQuest aq : lis) {
                if(aq.getName().equals(name)) {
                    return aq;
                }
            }
        }
        return null;
    }

    public boolean isQuestRegistered(Quest q) {
        for(QuestType type : quests.keySet()) {
            for(ActiveQuest aq : quests.get(type)) {
                if(aq.getName().equals(q.getQuestName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**Helper function: Safely removes quest from active to completed */
    private void completeQuest(ActiveQuest aq) {
        if(!aq.isCompleted()) { return; }
        if(completedQuests.contains(aq.getName())) { return; }
        
        unregisterQuest(aq.getName());
        completedQuests.add(aq.getName());
    }

}