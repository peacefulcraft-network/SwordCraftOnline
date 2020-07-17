package net.peacefulcraft.sco.quests.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

public class QuestPlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent ev) {
        SCOPlayer s = GameManager.findSCOPlayer(ev.getPlayer());
        if(s == null) { return; }

        
    }
    
}