package net.peacefulcraft.sco.quests.quests;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.quests.QuestStep;
import net.peacefulcraft.sco.quests.Quest.QuestType;

public class EscortQuestStep extends QuestStep {

    private MythicMob npc;

    public EscortQuestStep(QuestType type, String str) {
        super(type, str);
        String[] split = str.split(" ");

        MythicMob mm = SwordCraftOnline.getPluginInstance().getMobManager().getMythicMob(split[1]);
        if(mm == null) {
            SwordCraftOnline.logInfo("Issue loading EscortQuestStep. Invalid mob target: " + split[1]);
            this.setInvalid();
            return;
        }

        //TODO: Load location and compare

        this._setDescription();
    }

    @Override
    public void _setDescription() {
        String npcName = this.npc.getDisplayName();

        if(this.getDescriptionRaw() == null) {
            this.setDescription("Help " + npcName + " get to [] safely!");
        } else {
            try {
                //TODO: Add location to fields.
                this.setDescription(String.format(this.getDescriptionRaw(), npcName));
            } catch(Exception ex) {
                this.setDescription("Help " + npcName + " get to [] safely!");
            }
        }
    }
    
}