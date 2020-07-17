package net.peacefulcraft.sco.quests;

import java.util.ArrayList;
import java.util.HashMap;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.quests.QuestStep.QuestType;

public class QuestBookManager {
    
    private SCOPlayer s;
        public SCOPlayer getSCOPlayer() { return this.s; }

    private HashMap<QuestType, ArrayList<ActiveQuest>> quests = new HashMap<>();

    public QuestBookManager(SCOPlayer s) {
        this.s = s;
    }

    public void registerQuest(String questName) {
        ActiveQuest aq = SwordCraftOnline.getPluginInstance().getQuestManager().activateQuest(questName, this.s);
        if(aq == null) {
            SwordCraftOnline.logInfo("Attempted to register quest + " + questName + " to player " + s.getName());
            return;
        }

        //Fetching quests current step type
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


}