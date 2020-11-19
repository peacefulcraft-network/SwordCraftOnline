package net.peacefulcraft.sco.quests;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.announcer.Announcer;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

/**
 * Main message class for quests. Handles chat formatting and interactive chat
 */
public class Message {
    
    /**Type of message for quest. Determines where string is sent and when. */
    public enum MessageType {
        BASIC, INTERACTIVE, STARTUP;
    }

    /**Delay between messages in seconds */
    private final int MESSAGE_DELAY = 1;

    /**Type of message */
    private MessageType type;
        public MessageType getType() { return this.type; }

    /**Main messages list */
    private List<String> messages;

    /**QuestStep message is associated with */
    private QuestStep step;

    /**
     * Constructor
     * Takes raw string and parses information into valid Message config
     * @param sRaw Unparsed string
     */
    public Message(String sRaw, QuestStep step) {
        this.step = step;

        String sType = sRaw.substring(0, sRaw.indexOf(' '));
        String message = sRaw.substring(sRaw.indexOf(' ') + 1);

        this.type = MessageType.valueOf(sType.toUpperCase());
        
        // Breaking message up into segments
        String[] split = message.split("&break");
        for(String s : split) {
            messages.add(s);
        }
    }

    /**
     * Sends message to player
     * @param s SCOPlayer we send to
     */
    public void sendMessage(SCOPlayer s) {
        String name = s.getName();

        List<String> copy = new ArrayList<>(messages);

        // Delayed chat scrolling
        // NOTE: Might replace with just spaced messages with empty line
        new BukkitRunnable(){
            @Override
            public void run() {
                if(copy.isEmpty()) { 
                    this.cancel(); 
                } else {
                    String mess = copy.remove(0);
                    mess = mess.replace("&player", name);

                    Announcer.messagePlayer(s, getTitleCard() + " " + mess);
                }
            }
        }.runTaskTimer(SwordCraftOnline.getPluginInstance(), MESSAGE_DELAY * 20, 1);


        //TextComponent comp = new TextComponent(copy);
    }

    /**
     * Helper function
     * Constructs title card for NPC message
     * @return Formatted title card
     */
    private String getTitleCard() {
        return ChatColor.BLUE + "[" + ChatColor.GOLD + step.getGiverName()
            + ChatColor.BLUE + "]" + ChatColor.WHITE;
    }
}
