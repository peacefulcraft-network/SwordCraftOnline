package net.peacefulcraft.sco.quests.quests;

import net.peacefulcraft.sco.quests.QuestStep;
import net.peacefulcraft.sco.quests.Quest.QuestType;

public class TravelQuestStep extends QuestStep {

    public TravelQuestStep(QuestType type, String str) {
        super(type, str);
        String[] split = str.split(" ");

        //TODO: Check and validate location string
    }

    @Override
    public String getDescription() {
        //TODO: Location string
        String ret = "Travel to ";
        return ret;
    }
    
}