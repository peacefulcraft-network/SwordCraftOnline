package net.peacefulcraft.sco.quests;

import java.util.List;

import net.peacefulcraft.sco.mythicmobs.drops.Reward;

public abstract class QuestStep {
    
    /**Quests type */
    private QuestType type;
        public QuestType getType() { return this.type; }

    /**If this step is loaded improperly */
    private boolean isInvalid = false;
        public boolean isInvalid() { return this.isInvalid; }
        public void setInvalid() { this.isInvalid = true; }

    /**List of rewards given on completion of step */
    private List<Reward> rewards;
        public List<Reward> getRewards() { return this.rewards; }

    /**Description displayed on item in quest book */
    private String description = null;
        public String getDescription() { return this.description; }
        public void setDescription(String s) { this.description = s; }

    /**Raw unformatted description */
    private String descriptionRaw = null;
        public String getDescriptionRaw() { return this.descriptionRaw; }

    public QuestStep(QuestType type, String str) {
        this.type = type;
        //Loading rewards
        //kill SkeletonKing#3,SkeletonDrone#5 $Rewards:GoldCoin 2, Diamond 3 $Description:
        String rewardStr = str.split("$Rewards:")[1];
        if(rewardStr.contains(" $Description:")) {
            String[] split = rewardStr.split(" $Description:");
            rewardStr = split[0];
            this.descriptionRaw = split[1];
        }
        for(String s : rewardStr.split(",")) {
            this.rewards.add(new Reward(s));
        }
    }

    /**Internal method to format step description */
    public abstract void _setDescription();

    public enum QuestType {
        KILL, DELIVER, TRAVEL, GATHER, ESCORT;
    }
}