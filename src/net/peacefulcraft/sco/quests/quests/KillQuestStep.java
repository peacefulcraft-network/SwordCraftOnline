package net.peacefulcraft.sco.quests.quests;

import java.util.HashMap;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.quests.QuestStep;
import net.peacefulcraft.sco.quests.Quest.QuestType;

public class KillQuestStep extends QuestStep {

    private HashMap<MythicMob, Integer> targets = new HashMap<>();
    
    public KillQuestStep(QuestType type, String str) {
        super(type, str);
        String[] split = str.split(" ");

        //If there are multiple mobs
        if(split[1].contains(",")) {
            String[] split2 = split[1].split(",");
            for(String s : split2) {
                addTarget(s);
            }
        } else {
            addTarget(split[1]);
        }

        this._setDescription();
    }

    private void addTarget(String s) {
        try{
            String[] split = s.split("#");
        
            MythicMob mm = SwordCraftOnline.getPluginInstance().getMobManager().getMythicMob(split[0]);
            if(mm == null) {
                SwordCraftOnline.logInfo("Issue loading KillQuestStep. Invalid mob target: " + split[0]);
                this.setInvalid();
                return;
            }
            int amount = Integer.valueOf(split[1]);
            this.targets.put(mm, amount);
        } catch(Exception ex) {
            this.setInvalid();
        }
    }

    @Override
    public void _setDescription() {
        String targetStr = "";
        for(MythicMob mm : targets.keySet()) {
            String dis = mm.getDisplayName();
            String num = String.valueOf(targets.get(mm));
            targetStr += num + " " + dis + ", ";
        }
        targetStr = targetStr.substring(0, targetStr.length()-2);
        //If there is no description to format
        if(this.getDescriptionRaw() == null) {
            this.setDescription("You are tasked to kill " + targetStr + ".");
        } else {
            try {
                this.setDescription(String.format(this.getDescriptionRaw(), targetStr));
            } catch(Exception ex) {
                this.setDescription("You are tasked to kill " + targetStr + ".");
            }
        }
    }
    
}