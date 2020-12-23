package net.peacefulcraft.sco.quests.quests;

import java.util.Map;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.gamehandle.regions.Region;
import net.peacefulcraft.sco.gamehandle.regions.RegionManager;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.quests.QuestStep;

public class TravelQuestStep extends QuestStep {

    private Region targetRegion;

    public TravelQuestStep(MythicConfig mc) {
        super(mc);

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
        String npcName = getGiverName();
        String startRegion = getGiverRegion().getName();
        String target = targetRegion.getName();

        // If quest is not activated we use find npc description
        if (!this.isActivated()) {
            this.setDescription("Talk to " + npcName + " in " + startRegion + " to start quest.");
            return;
        }

        if (this.getDescriptionRaw() == null) {
            this.setDescription("Travel to " + target + ".");
        } else {
            try {
                this.setDescription(String.format(this.getDescriptionRaw(), target));
            } catch (Exception ex) {
                this.setDescription("Travel to " + target + ".");
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
        if (!s.getRegion().equals(targetRegion)) {
            return false;
        }

        completeMessage(s);
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
}