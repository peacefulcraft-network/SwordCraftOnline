package net.peacefulcraft.sco.quests.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.quests.QuestStep.QuestType;

public class QuestEntityDamageListener implements Listener {
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void entityDamange(EntityDamageByEntityEvent ev) {
        if(!(ev.getDamager() instanceof Player)) { return; }
        
        Player p = (Player)ev.getDamager();
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) { return; }

        s.getQuestBookManager().executeLoop(QuestType.KILL, ev);
    }
}