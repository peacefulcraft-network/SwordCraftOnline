package net.peacefulcraft.sco.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.quests.ActiveQuest;

/**
 * Command responsible for interactive messages with quests
 * format: /questmessager [player name] [quest name] [response message name]
 */
public class QuestMessager implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("questmessager")) {
            SCOPlayer s = GameManager.findSCOPlayer(args[0]);
            if(s == null) {
                SwordCraftOnline.logSevere("Attempted to progress quest of non-existing player: " + args[1]);
            }
    
            ActiveQuest aq = s.getQuestBookManager().getRegisteredQuest(args[1]);
            if(aq == null) {
                SwordCraftOnline.logSevere("Attempted to progress Quest: " + args[1] + ", for player: " + args[0]);
                return true;
            }
    
            aq.sendResponse(args[2]);
    
            return true;
        }
        return true;
    }
}
