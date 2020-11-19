package net.peacefulcraft.sco.quests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.gamehandle.regions.Region;
import net.peacefulcraft.sco.gamehandle.regions.RegionManager;
import net.peacefulcraft.sco.mythicmobs.drops.Reward;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.quests.Message.MessageType;

public abstract class QuestStep {
    
    /**Quests type */
    private QuestType type;
        public QuestType getType() { return this.type; }

    /**Steps name */
    private String name;
        public String getName() { return this.name; }

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

    /**Key location npc can be found */
    private Region giverRegion;
        public Region getGiverRegion() { return this.giverRegion; }

    /**If NPC has given the quest. I.e. player interacted with quest NPC */
    private Boolean activated = false;
        public Boolean isActivated() { return this.activated; }
        public void setActivated(Boolean b) { this.activated = b; }

    /**Messages for quest organized by type */
    protected HashMap<MessageType, Message> messages;

    /**
     * If step uses an NPC to activate Quest
     * If false, step is activated on completion of previous step.
     * Beginning steps cannot be false
     */
    private Boolean usesGiver;
        public Boolean usesGiver() { return this.usesGiver; }
        public void setUsesGiver(boolean b) { this.usesGiver = b; }

    public QuestStep(MythicConfig mc) {
        //TODO: Handle this step name in description of item created.
        this.name = mc.getString("Name");
        
        //Fetching quest type
        try{
            this.type = QuestType.valueOf(mc.getString("Type").toUpperCase());
        } catch(IllegalArgumentException ex) {
            SwordCraftOnline.logInfo("IllegalArguementException loading QuestStep: " + this.name);
            this.isInvalid = true;
            return;
        }

        // Giver name validation
        String giverName = mc.getString("GiverName", "");
        giverName = mc.getString("Giver", giverName);
        if(giverName == null || giverName.isEmpty()) {
            this.logInfo("No GiverName field in config.");
            return;
        }
        this.giver = SwordCraftOnline.getPluginInstance().getMobManager().getMythicMob(giverName);
        if(this.giver == null) {
            this.logInfo("Invalid GiverName field in config.");
            return;
        }

        // Region validation
        String regionName = mc.getString("GiverRegion", "");
        if(regionName == null || regionName.isEmpty()) {
            this.logInfo("No GiverRegion field in config.");
            return;
        }
        this.giverRegion = RegionManager.getRegion(regionName);
        if(this.giverRegion == null) {
            this.logInfo("Invalid GiverRegion field in config.");
            return;
        }

        this.usesGiver = mc.getBoolean("UsesGiver", true);

        this.descriptionRaw = mc.getString("Description", "");
        descriptionRaw = mc.getString("Desc", descriptionRaw);

        // Reward validation
        List<String> rewardsLis = mc.getStringList("Rewards");
        for(String s : rewardsLis) {
            this.rewards.add(new Reward(s));
        }

        // Message validation
        List<String> messageLis = mc.getStringList("Messages");
        for(String s : messageLis) {
            Message mess = new Message(s, this);
            messages.put(mess.getType(), mess);
        }
    }

    /**Protected call to reset description */
    public void updateDescription() {
        //TODO: Update actual item dsecription associated with quest
        this._setDescription();
    }

    /**Logs info regarding Step loading in console and sets step invalid*/
    public void logInfo(String info) {
        SwordCraftOnline.logInfo("Error loading: " + this.getName() + ". " + info);
        this.setInvalid();
    }

    /**Internal method to format step description */
    public abstract void _setDescription();

    /**
     * Conditions for a quest step. Must return true for quest
     * to be marked complete
     * @param ev Respective event passed by listeners
     * @return True if step is completed, false otherwise.
     */
    public abstract boolean stepPreconditions(Event ev);

    /**Any real world effects that need to happen on activation of step */
    public abstract void startupLifeCycle(SCOPlayer s);

    /**@return Map object for relative save data of step */
    public abstract Map<String,Object> getSaveData();

    /**
     * Sends startup message to player. Called in startup cycle
     * @param s Player we send message to
     */
    protected void startupMessage(SCOPlayer s) {
        Message mess = messages.get(MessageType.STARTUP);
        if(mess == null) { return; }

        mess.sendMessage(s);
    }

    /**Type of quest step. Determines how loading is handled */
    public enum QuestType {
        KILL, DELIVER, TRAVEL, GATHER, ESCORT;
    }

}