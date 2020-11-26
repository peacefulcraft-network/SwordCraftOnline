package net.peacefulcraft.sco.quests.listeners;

import java.util.ArrayList;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
import net.peacefulcraft.sco.quests.ActiveQuest;

/***
 * Main class to handle activating quests via NPC interaction.
 * Verifies quest givers nameplate against quest step giver NPC
 */
public class NPCActivateListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent ev) {
        Player p = ev.getPlayer();
        
        //Is player in the game
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) { return; }

        Entity entity = ev.getRightClicked();
        ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().get(entity.getUniqueId());
        if(am == null) { return; }

        //Checking if NPC name is matched
        for(ArrayList<ActiveQuest> lis : s.getQuestBookManager().getQuests().values()) {
            for(ActiveQuest aq : lis) {
                //If match and step was not activated by NPC
                if(aq.getNPCName().equalsIgnoreCase(am.getDisplayName()) && !aq.isStepActivated()) {
                    aq.setStepActivated();
                    return;
                }
            }
        }
    }
    
}