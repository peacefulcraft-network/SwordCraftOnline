package net.peacefulcraft.sco.quests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.gamehandle.regions.Region;
import net.peacefulcraft.sco.gamehandle.regions.RegionManager;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.mythicmobs.drops.Reward;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.quests.messages.MessageHandler;

public abstract class QuestStep {
    
    /**Quests type */
    private QuestType type;
        public QuestType getType() { return this.type; }

    /**Steps name */
    protected String name;
        public String getName() { return this.name; }

    /**If this step is loaded improperly */
    private boolean isInvalid = false;
        public boolean isInvalid() { return this.isInvalid; }
        public void setInvalid() { this.isInvalid = true; }

    /**List of rewards given on completion of step */
    protected List<Reward> rewards = new ArrayList<>();
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
    protected MythicMob giver;
        public MythicMob getGiver() { return this.giver; }
        public String getGiverName() { return this.giver.getDisplayName(); }

    /**Key location npc can be found */
    protected Region giverRegion;
        public Region getGiverRegion() { return this.giverRegion; }

    /**If NPC has given the quest. I.e. player interacted with quest NPC */
    private Boolean activated = false;
        public Boolean isActivated() { return this.activated; }
        public void setActivated(Boolean b) { this.activated = b; }

    /**
     * If step uses an NPC to activate Quest
     * If false, step is activated on completion of previous step.
     * Beginning steps cannot be false
     */
    protected Boolean usesGiver;
        public Boolean usesGiver() { return this.usesGiver; }
        public void setUsesGiver(boolean b) { this.usesGiver = b; }

    /**Messages for quest organized by type */
    protected MessageHandler messageHandler = null;

    public QuestStep(MythicConfig mc, String questName) {
        this.name = mc.getString("Name");
        
        //Fetching quest type
        try{
            this.type = QuestType.valueOf(mc.getString("Type").toUpperCase());
        } catch(IllegalArgumentException ex) {
            SwordCraftOnline.logInfo("IllegalArguementException loading QuestStep: " + this.name);
            this.isInvalid = true;
            return;
        }

        this.usesGiver = mc.getBoolean("UsesGiver", true);

        if(this.usesGiver) {
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
            if(!usesGiver && (regionName == null || regionName.isEmpty())) {
                this.logInfo("No GiverRegion field in config.");
                return;
            }
            this.giverRegion = RegionManager.getRegion(regionName);
            if(!usesGiver && this.giverRegion == null) {
                this.logInfo("Invalid GiverRegion field in config.");
                return;
            }
        }

        this.descriptionRaw = mc.getString("Description", "");
        descriptionRaw = mc.getString("Desc", descriptionRaw);

        // Reward validation
        List<String> rewardsLis = mc.getStringList("Rewards");
        for(String s : rewardsLis) {
            this.rewards.add(new Reward(s));
        }

        // Message validation
        List<Map<?,?>> temp = mc.getMapList("Messages");
        if(!temp.isEmpty()) { 
            Map<?,?> messageMap = mc.getMapList("Messages").get(0);
            messageHandler = new MessageHandler(messageMap, this); 
        }
    }

    public void updateDescription() {
        updateDescription(null, null);
    }

    /**Protected call to reset description */
    public void updateDescription(SCOPlayer s, ActiveQuest aq) {
        this._setDescription();

        if(s == null || aq == null) { return; }
        int index = 0;
        for(ItemIdentifier ident : s.getQuestBookInventory().generateItemIdentifiers()) {
            if(ident.getName().equalsIgnoreCase("Quest") && ident.getDisplayName().equalsIgnoreCase(aq.getName())) {
                break;
            }
            index++;
        }

        // Giving quest item to player
        ItemIdentifier ident = QuestBookManager.generateQuestItem(aq);
        s.getQuestBookInventory().setItem(index, ident);
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

    public abstract void processStepData(JsonObject data);

    /**@return Map object for relative save data of step */
    public abstract Map<String,Object> getSaveData();

    /**
     * Sends startup message to player. Called in startup cycle
     * @param s Player we send message to
     */
    protected void startupMessage(SCOPlayer s) {
        if(messageHandler == null) { return; }
        messageHandler.sendStartup(s);
    }

    /**
     * Sends complete message to player. Called at end of preconditions
     * @param s Player we send message to
     */
    protected void completeMessage(SCOPlayer s) {
        if(messageHandler == null) { return; }
        messageHandler.sendComplete(s);
    }

    /**
     * Sends response message to player
     * @param resName Name of response
     */
    public void sendResponse(String resName, SCOPlayer s) {
        if(messageHandler == null) { return; }
        messageHandler.sendMessage(resName, s);
    }

    /**Type of quest step. Determines how loading is handled */
    public enum QuestType {
        KILL, DELIVER, TRAVEL, GATHER, ESCORT;
    }

}