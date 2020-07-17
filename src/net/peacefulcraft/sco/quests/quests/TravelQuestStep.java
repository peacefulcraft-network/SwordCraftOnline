package net.peacefulcraft.sco.quests.quests;

import net.peacefulcraft.sco.quests.QuestStep;
import net.peacefulcraft.sco.quests.Quest.QuestType;

public class TravelQuestStep extends QuestStep {

    public TravelQuestStep(QuestType type, String str) {
        super(type, str);
        String[] split = str.split(" ");

        //TODO: Check and validate location string

        this._setDescription();
    }

    @Override
    public String getDescription() {
        //TODO: Location string
        String ret = "Travel to ";
        return ret;
    }

    @Override
    public void _setDescription() {
        if(this.getDescriptionRaw() == null) {
            this.setDescription("Travel to [].");
        } else {
            try {
                //TODO: Add location string
                //this.setDescription(String.format(this.getDescriptionRaw()));
            } catch(Exception ex) {
                this.setDescription("Travel to [].");
            }
        }
    }
    
}