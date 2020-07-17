package net.peacefulcraft.sco.quests;

import java.util.List;

import net.peacefulcraft.sco.mythicmobs.drops.Reward;
import net.peacefulcraft.sco.quests.Quest.QuestType;

public abstract class QuestStep {
    
    private QuestType type;
        public QuestType getType() { return this.type; }

    private boolean isInvalid = false;
        public boolean isInvalid() { return this.isInvalid; }
        public void setInvalid() { this.isInvalid = true; }

    private List<Reward> rewards;
        public List<Reward> getRewards() { return this.rewards; }

    public QuestStep(QuestType type, String str) {
        /**
         * String formatting: {Type} {Target} [if kill, collect, gather: Amount]
         * Example: "kill SkeletonKing 2", "deliver {item name} {amount} {npc name}",
         *          "travel {location name}", "gather bone 20", "escort {npc name} {location name}"
         */
        this.type = type;
        //Loading rewards
        String rewardStr = str.split("$Rewards:")[1];
        for(String s : rewardStr.split(",")) {
            this.rewards.add(new Reward(s));
        }
    }

    public abstract String getDescription();
}