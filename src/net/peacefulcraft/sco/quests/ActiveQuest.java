package net.peacefulcraft.sco.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
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

    /**Sends player announce title of quest completed */
    private void sendCompletedTitle() {
        Announcer.sendTitle(s.getPlayer(), "Quest Completed!", "Completed: \"" + getName() + "\"");
    }

    /**Sends player announce title of quest step completed */
    private void sendProgressedTitle() {
        Announcer.sendTitle(s.getPlayer(), "Quest progression unlocked!", "Progression unlocked in: \"" + getName() + "\"");
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
        step.updateDescription();
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

    /**@return Item represent of quest at current step */
    public ItemStack getItem() {
        ItemStack icon = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = icon.getItemMeta();

        QuestStep qs = quest.getQuestStep(this.currentStep);
        
        ArrayList<String> desc = new ArrayList<>();
        desc.add(qs.getDescription());

        if(this.locked) {
            meta.setDisplayName(ChatColor.RED + "" + quest.getQuestName());
            desc.add(ChatColor.RED + "" + qs.getDescription());
        } else {
            meta.setDisplayName(ChatColor.AQUA + "" + quest.getQuestName());
            desc.add(ChatColor.WHITE + "" + qs.getDescription());
        }

        desc.add("");
        desc.add(ChatColor.DARK_PURPLE + getProgress());
        desc.add("");
        desc.add(getRewardStr());
        meta.setLore(desc);
        icon.setItemMeta(meta);

        return icon;
    }

    /**Helper function gets current progress as string bar */
    private String getProgress() {
        String ret = "[" + new String(new char[this.currentStep]).replace("\0", Announcer.getSquare());
        ret += new String(new char[this.quest.getSize()-this.currentStep]).replace("\0", "-") + "]";
        return ret;
    }

    /**Helper function gets rewards as string */
    private String getRewardStr() {
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