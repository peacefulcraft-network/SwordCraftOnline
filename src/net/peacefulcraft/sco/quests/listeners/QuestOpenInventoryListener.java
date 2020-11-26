package net.peacefulcraft.sco.quests.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.quests.QuestStep.QuestType;

public class QuestOpenInventoryListener implements Listener{
    
    @EventHandler
    public void onOpenInventory(InventoryOpenEvent ev) {
        Player p = (Player)ev.getPlayer();
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) { return; }

        s.getQuestBookManager().executeLoop(QuestType.GATHER, ev);
    }

}