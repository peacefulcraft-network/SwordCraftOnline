package net.peacefulcraft.sco.quests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.announcer.Announcer;
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

    /**
     * Registers quest with base step count
     * @param questName
     */
    public void registerQuest(String questName) {
        registerQuest(questName, null, -1);
    }

    /**
     * Registers quest to player 
     */
    public void registerQuest(String questName, JsonObject data, int index) {
        ActiveQuest aq = SwordCraftOnline.getPluginInstance().getQuestManager().activateQuest(questName, this.s);
        if(aq == null) {
            SwordCraftOnline.logInfo("Failed to register quest + " + questName + " to player " + s.getName());
            return;
        }

        if(data != null) {
            aq.setCurrentStep(data.get("step").getAsInt());
            if(data.get("activated").getAsBoolean()) {
                aq.setStepActivated();
            }

            if(data.get("stepData").getAsJsonObject().size() != 0) {
                aq.passStepData(data);
            }
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
                    Announcer.messagePlayer(s, "You have already accepted " + aq.getName() + ", check your Quest Book.", 0);
                    return;
                }
            }
        }

        quests.get(qt).add(aq);

        // Giving quest item to player
        ItemIdentifier ident = generateQuestItem(aq);
        CustomDataHolder cus = (CustomDataHolder)ident;
        if(data != null) {
            cus.setCustomData(data);
        }

        if(index != -1) {
            s.getQuestBookInventory().setItem(index, ident);
        } else {
            s.getQuestBookInventory().addItem(ident);
        }
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
            SwordCraftOnline.logDebug("[Quest Book Manager] Life Cycle: " + aq.getName());
            aq.execLifeCycle(type, ev);
            
            //Is active quest completed
            if(aq.isCompleted()) {
                completeQuest(aq);
            } else {
                //If quest was not completed we want to update item in their inventory
                // This logic is handled in the quest steps
                int xx = 0;
                /*
                QuestBookInventory base = s.getQuestBookInventory();

                List<ItemIdentifier> identLis = base.generateItemIdentifiers();
                for(int i = 0; i < identLis.size(); i++) {
                    ItemIdentifier ident = identLis.get(i);
                    if(!ident.getName().equalsIgnoreCase("Quest")) { continue; }
                    if(!ident.getDisplayName().equalsIgnoreCase(aq.getName())) { continue; }

                    ItemIdentifier identt = generateQuestItem(aq);
                    base.setItem(i, identt);
                }*/
            }
        }
    }

    public boolean isQuestRegistered(String name) {
        Quest q = SwordCraftOnline.getPluginInstance().getQuestManager().getQuest(name);
        if(q == null) { return false; }

        return isQuestRegistered(q);
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

    /**
     * Force clear the registered quests
     */
    public void unregisterAllQuests() {
        this.quests = new HashMap<QuestType, ArrayList<ActiveQuest>>();
    }
 
    public void syncQuestBookInventory(QuestBookInventory inv) {
        unregisterAllQuests();
        
        List<ItemIdentifier> identLis = inv.generateItemIdentifiers();
        for(int i = 0; i < identLis.size(); i++) {
            ItemIdentifier identifier = identLis.get(i);
            if (identifier instanceof CustomDataHolder) {
                CustomDataHolder cus = (CustomDataHolder)identifier;

                SwordCraftOnline.logDebug("[Quest Book Manager] Sync Task: " + identifier.getDisplayName() + ".Data: " + cus.getCustomData().toString());

                this.registerQuest(
                    cus.getCustomData().get("questName").getAsString(), 
                    cus.getCustomData(),
                    i
                    );
            }
        }

    }

    /**
     * Static method.
     * Formats and creates a quest item for the inventory
     * @param aq ActiveQuest we are formatting item for
     * @return ItemIdentifier quest item
     */
    public static ItemIdentifier generateQuestItem(ActiveQuest aq) {
        JsonObject obj = new JsonObject();
        obj.addProperty("questName", aq.getName());
        
        String desc = aq.isCompleted() 
            ? "Completed.\n" + aq.getProgressBar()
            : aq.getItemDescription() + "\n" + aq.getRewardStr() + "\n\n" + aq.getProgressBar();  
        obj.addProperty("description", desc);
        obj.addProperty("step", aq.getCurrentStep());
        obj.addProperty("activated", aq.isStepActivated());
        obj.addProperty("completed", aq.isCompleted());

        ItemIdentifier ident = ItemIdentifier.generateIdentifier("Quest", ItemTier.COMMON, 1);
        ((CustomDataHolder)ident).setCustomData(obj);
        return ident;
    }

    /**Helper function: Safely removes quest from active to completed */
    private void completeQuest(ActiveQuest aq) {
        if(!aq.isCompleted()) { return; }
        if(completedQuests.contains(aq.getName())) { return; }
        
        unregisterQuest(aq.getName());
        completedQuests.add(aq.getName());
    }

}