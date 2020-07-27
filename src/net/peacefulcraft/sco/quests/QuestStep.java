package net.peacefulcraft.sco.quests;

import java.util.List;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.drops.Reward;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;

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

    private Boolean toReset = false;
        public Boolean toReset() { return this.toReset; }
        public void setReset(Boolean b) { this.toReset = b; }

    /**NPC quest giver */
    private MythicMob giver;
        public MythicMob getGiver() { return this.giver; }
        public String getGiverName() { return this.giver.getDisplayName(); }

    //TODO: Key location in which NPC can be found. A city, landmark, etc.

    /**If NPC has given the quest. I.e. player interacted with quest NPC */
    private Boolean activated = false;
        public Boolean isActivated() { return this.activated; }
        public void setActivated(Boolean b) { this.activated = b; }

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

    /**Protected call to reset description */
    public void updateDescription() {
        //TODO: Update actual item dsecription associated with quest
        this._setDescription();
    }

    /**Internal method to format step description */
    public abstract void _setDescription();

    public abstract boolean stepPreconditions(Event ev);

    /**Any real world effects that need to happen on activation of step */
    public abstract void startupLifeCycle(SCOPlayer s);

    public enum QuestType {
        KILL, DELIVER, TRAVEL, GATHER, ESCORT;
    }
}