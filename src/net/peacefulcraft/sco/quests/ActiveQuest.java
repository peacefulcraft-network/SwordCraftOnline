package net.peacefulcraft.sco.quests;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.gamehandle.announcer.Announcer;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.drops.Reward;
import net.peacefulcraft.sco.quests.QuestStep.QuestType;

public class ActiveQuest {
    
    private Quest quest;

    private SCOPlayer s;

    private boolean locked = false;
        public boolean isLocked() { return this.locked; }

    private int currentStep;
        public int getCurrentStep() { return this.currentStep; }

    private boolean completed;
        public boolean isCompleted() { return this.completed; }

    public ActiveQuest(SCOPlayer s, Quest quest) {
        this.quest = quest;
        this.s = s;

        if(!quest.checkRequirements(s)) {
            this.locked = true;
        }

        this.currentStep = 0;
        this.completed = false;
    }

    public void progressQuest() {
        //Progressing step then updating in quest book manager
        this.currentStep += 1;
        
        //Updates quest position in questMap
        s.getQuestBookManager().updateQuest(quest.getQuestName());

        //If the quest doesn't use NPC we auto activate
        if(!getQuestStep().usesGiver()) {
            setStepActivated();
        }

        //If quest is completed
        if(this.currentStep == quest.getSize()) {
            sendCompletedTitle();
            
            this.completed = true;
            s.getQuestBookManager().unregisterQuest(quest.getQuestName());
            //TODO: Decide how we handle story quest progression. 

            //Adding quest name to completed quest list
            s.getQuestBookManager().addCompletedQuest(quest.getQuestName());
        } else {
            sendProgressedTitle();
        }
    }

    /**
     * Executes the active quest lifecycle: Check validity, distribute rewards, progress instance
     * @param type The quest step type
     * @param ev Triggering event
     */
    public void execLifeCycle(QuestType type, Event ev) {
        if(getQuestType().equals(type)) {
            QuestStep step = getQuestStep();

            //Does the quest need to reset
            //If yes, we set to deactivated and update the item description
            if(step.toReset()) {
                step.setActivated(false);
                step.updateDescription();
            }

            //Checking if quest step is activated by NPC
            if(!step.isActivated()) { return; }

            //Check preconditions for this quest step type
            if(!step.stepPreconditions(ev)) { return; }

            //Distribute rewards from current step
            this.giveRewards();

            //Progressing step
            this.progressQuest();
        }
    }

    /**@return Map of relative save data */
    public Map<String,Object> getSaveData() {
        Map<String,Object> ret = new HashMap<>();

        ret.put("CurrentStep", Integer.valueOf(currentStep));
        ret.put("StepData", getQuestStep().getSaveData());
        return ret;
    }

    /**
     * Fetches the current description
     * of the quest step
     * @return String description
     */
    public String getItemDescription() {
        return getQuestStep().getDescription();
    }

    /**Sends player announce title of quest completed */
    private void sendCompletedTitle() {
        Announcer.messageTitleBar(s.getPlayer(), "Quest Completed!", "Completed: \"" + getName() + "\"");
    }

    /**Sends player announce title of quest step completed */
    private void sendProgressedTitle() {
        Announcer.messageTitleBar(s.getPlayer(), "Quest progression unlocked!", "Progression unlocked in: \"" + getName() + "\"");
    }

    /**@return QuestType of current step */
    public QuestType getQuestType() {
        return getQuestStep().getType();
    }

    /**@return Name of quest */
    public String getName() {
        return this.quest.getQuestName();
    }

    /**@return Name of NPC giving quest */
    public String getNPCName() {
        return getQuestStep().getGiverName();
    }

    /**@return If current step is activated via NPC */
    public Boolean isStepActivated() {
        return getQuestStep().isActivated();
    }

    /**
     * Sends current steps response message
     * @param resName Response name
     */
    public void sendResponse(String resName) {
        QuestStep step = getQuestStep();

        step.sendResponse(resName, s);
    }

    /**Toggles startup lifecycle */
    public void setStepActivated() {
        QuestStep step = getQuestStep();

        //Toggling activation
        step.setActivated(true);

        //Startup lifecycle of step
        step.startupLifeCycle(this.s);

        //Updates step item description to match quest
        step.updateDescription(this.s, this);
    }

    /**Gives player associated with quest rewards */
    public void giveRewards() {
        for(Reward r : getQuestStep().getRewards()) {
            ItemStack item = r.getReward();
            Player p = s.getPlayer();
            if(item == null) { 
                p.giveExp(r.getExperience());
                continue;
            }

            //Dropping items on player if inventory full
            HashMap<Integer, ItemStack> ret = p.getInventory().addItem(r.getReward());
            if(ret != null) {
                for(ItemStack i : ret.values()) {
                    p.getLocation().getWorld().dropItemNaturally(p.getLocation(), i);
                }
            }
        }
    }

    /**
     * Gets progress bar string of the active quest
     * @return Square bar
     */
    public String getProgressBar() {
        String ret = "[" + new String(new char[this.currentStep]).replace('\0', (char)0x25A0);
        ret += new String(new char[this.quest.getSize()-this.currentStep]).replace('\0', (char)0x25A1);
        ret += "]";
        return ret;
    }

    /**
     * Gets rewards for item description
     * @return Complete formatted string of rewards
     */
    public String getRewardStr() {
        String ret = "Rewards: ";
        for(Reward r : getQuestStep().getRewards()) {
            if(r.getReward() == null) {
                ret += String.valueOf(r.getExperience()) + " Exp, ";
            } else {
                String item = r.getReward().getItemMeta().getDisplayName();
                String amount = String.valueOf(r.getReward().getAmount());
                ret += amount + " " + item + ", ";
            }
        }
        ret = ret.substring(0, ret.length()-2);
        return ret;
    }

    private QuestStep getQuestStep() {
        return this.quest.getQuestStep(this.currentStep);
    }
}