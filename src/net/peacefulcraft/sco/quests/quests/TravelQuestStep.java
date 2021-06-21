package net.peacefulcraft.sco.quests.quests;

import java.util.Map;

import com.google.gson.JsonObject;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.gamehandle.regions.Region;
import net.peacefulcraft.sco.gamehandle.regions.RegionManager;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.quests.ActiveQuest;
import net.peacefulcraft.sco.quests.QuestStep;

public class TravelQuestStep extends QuestStep {

    private Region targetRegion;

    public TravelQuestStep(MythicConfig mc, String questName) {
        super(mc, questName);

        String regionStr = mc.getString("TargetRegion", "");
        if (regionStr == null || regionStr.isEmpty()) {
            this.logInfo("No TargetRegion field in config.");
            return;
        }
        this.targetRegion = RegionManager.getRegion(regionStr);
        if (this.targetRegion == null) {
            this.logInfo("Invalid TargetRegion field in config.");
            return;
        }

        this._setDescription();
    }

    @Override
    public void _setDescription() {
        String target = targetRegion.getName();

        // If quest is not activated we use find npc description
        //
        // If no start region we use travel description
        if (!this.isActivated() && this.usesGiver) {
            String npcName = getGiverName();
            String startRegion = getGiverRegion().getName();
            this.setDescription(this.name + "\nTalk to " + npcName + " in " + startRegion + " to start quest.");
            return;
        }

        if (this.getDescriptionRaw() == null || this.getDescriptionRaw().isEmpty() || !this.usesGiver) {
            this.setDescription(this.name + "\nTravel to " + target + ".");
        } else {
            try {
                this.setDescription(String.format(this.getDescriptionRaw(), target));
            } catch (Exception ex) {
                this.setDescription(this.name + "\nTravel to " + target + ".");
            }
        }
    }

    @Override
    public boolean stepPreconditions(Event ev) {
        // Is event playermove
        if (!(ev instanceof PlayerMoveEvent)) {
            return false;
        }
        PlayerMoveEvent e = (PlayerMoveEvent) ev;

        // Is player in game
        SCOPlayer s = GameManager.findSCOPlayer(e.getPlayer());
        if (s == null) {
            return false;
        }

        // Is player in region
        if (s.getRegion() == null || !s.getRegion().equals(targetRegion)) {
            return false;
        }

        
        ActiveQuest aq = s.getQuestBookManager().getRegisteredQuest(this.name);
        if(aq != null) {
            SwordCraftOnline.logDebug("[Travel Quest Step] Protected update call.");
            aq.updateItemDescription();
        } else {
            SwordCraftOnline.logDebug("[Travel Quest Step] Active quest not found.");
        }

        return true;
    }

    @Override
    public void startupLifeCycle(SCOPlayer s) {
        startupMessage(s);
        return;
    }

    @Override
    public Map<String, Object> getSaveData() {
        //No data necessary for player progression
        //Location checks are done in lifecycle
        return null;
    }

    @Override
    public void processStepData(JsonObject data) {
        // TODO Auto-generated method stub
        
    }
}