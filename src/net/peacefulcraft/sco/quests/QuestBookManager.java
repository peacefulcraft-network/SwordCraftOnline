package net.peacefulcraft.sco.quests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.InventoryBase;
import net.peacefulcraft.sco.inventories.InventoryType;
import net.peacefulcraft.sco.quests.QuestStep.QuestType;

public class QuestBookManager {
    
    private SCOPlayer s;
        public SCOPlayer getSCOPlayer() { return this.s; }

    /**Players active quests they can complete */
    private HashMap<QuestType, ArrayList<ActiveQuest>> quests = new HashMap<>();
        public Map<QuestType, ArrayList<ActiveQuest>> getQuests() { return Collections.unmodifiableMap(quests); }

    /**Players completed quests */
    private ArrayList<String> completedQuests = new ArrayList<>();

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
                InventoryBase base = s.getInventory(InventoryType.QUEST_BOOK);
                Inventory inv = base.getInventory();
                for(ItemStack i : inv.getContents()) {
                    String name = ChatColor.stripColor(i.getItemMeta().getDisplayName());
                    if(name.equalsIgnoreCase(aq.getName())) {
                        int index = inv.first(i);
                        inv.setItem(index, aq.getItem());
                        return;
                    }
                }
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

    /**Helper function: Safely removes quest from active to completed */
    private void completeQuest(ActiveQuest aq) {
        if(!aq.isCompleted()) { return; }
        if(completedQuests.contains(aq.getName())) { return; }
        
        unregisterQuest(aq.getName());
        completedQuests.add(aq.getName());
    }

}