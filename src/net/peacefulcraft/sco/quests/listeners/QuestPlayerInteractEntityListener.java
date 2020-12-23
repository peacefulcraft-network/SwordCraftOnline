package net.peacefulcraft.sco.quests.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.quests.QuestStep.QuestType;

public class QuestPlayerInteractEntityListener implements Listener {
    
    @EventHandler
    public void onInteract(PlayerInteractEntityEvent ev) {
        SCOPlayer s = GameManager.findSCOPlayer(ev.getPlayer());
        if(s == null) { return; }

        s.getQuestBookManager().executeLoop(QuestType.DELIVER, ev);
    }    
}