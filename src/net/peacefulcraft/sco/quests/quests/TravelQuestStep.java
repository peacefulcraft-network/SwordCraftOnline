package net.peacefulcraft.sco.quests.quests;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.quests.QuestStep;

public class TravelQuestStep extends QuestStep {

    public TravelQuestStep(QuestType type, String str) {
        super(type, str);
        String[] split = str.split(" ");

        // TODO: Check and validate location string

        this._setDescription();
    }

    @Override
    public String getDescription() {
        // TODO: Location string
        String ret = "Travel to ";
        return ret;
    }

    @Override
    public void _setDescription() {
        String npcName = getGiverName();

        //If quest is not activated we use find npc description
        if(!this.isActivated()) {
            this.setDescription("Talk to " + npcName + " in [] to start quest");
            return;
        }

        if (this.getDescriptionRaw() == null) {
            this.setDescription("Travel to [].");
        } else {
            try {
                // TODO: Add location string
                // this.setDescription(String.format(this.getDescriptionRaw()));
            } catch (Exception ex) {
                this.setDescription("Travel to [].");
            }
        }
    }

    @Override
    public boolean stepPreconditions(Event ev) {
        //Is event playermove
        if(!(ev instanceof PlayerMoveEvent)) { return false; }
        PlayerMoveEvent e = (PlayerMoveEvent)ev;

        //Is player in game
        SCOPlayer s = GameManager.findSCOPlayer(e.getPlayer());
        if(s == null) { return false; }

        //TODO: Check location

        return true;
    }

    @Override
    public void startupLifeCycle(SCOPlayer s) {
        return;
    }
    
}