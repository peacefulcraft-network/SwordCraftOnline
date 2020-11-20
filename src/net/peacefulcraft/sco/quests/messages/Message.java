package net.peacefulcraft.sco.quests.messages;

import org.apache.commons.lang.StringUtils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.quests.QuestStep;

/**
 * Main message class for quests. Handles chat formatting and interactive chat
 */
public class Message {
    
    /**Type of message for quest. Determines where string is sent and when. */
    public enum MessageType {
        BASIC, INTERACTIVE, STARTUP, COMPLETE, RESPONSE;
    }

    /**Type of message */
    private MessageType type;
        public MessageType getType() { return this.type; }

    /**Main messages list */
    private String message;
        public String getText() { return message; }

    /**QuestStep message is associated with */
    private QuestStep step;

    /**Message handler this is contained in */
    private MessageHandler handler;

    /**
     * Constructor
     * Takes raw string and parses information into valid Message config
     * @param sRaw Unparsed string
     */
    public Message(String sRaw, QuestStep step, MessageHandler handler) {
        this.step = step;
        this.handler = handler;

        String sType = sRaw.substring(0, sRaw.indexOf(' '));
        String message = sRaw.substring(sRaw.indexOf(' ') + 1);

        this.type = MessageType.valueOf(sType.toUpperCase());
        
        // Breaking message up into segments
        this.message = message.replace("&break", "\n");


    }

    /**
     * Sends message to player
     * @param s SCOPlayer we send to
     */
    public void sendMessage(SCOPlayer s) {
        String name = s.getName();

        // Formatting string components
        String copy = this.message.replace("&player", name);
        TextComponent mess = formatFollow(copy);
        mess = formatInteractive(mess, name);

        TextComponent title = new TextComponent(getTitleCard());
        title.addExtra(mess);

        s.getPlayer().spigot().sendMessage(title);
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

    /**
     * Helper function
     * If follow tag is found we replace with correct message
     */
    private TextComponent formatFollow(String s) {
        String follow = StringUtils.substringBetween(s, "&follow{", "}");
        String text = handler.getMessageText(follow);

        String mess = s.split("&follow{")[0];
        mess += " " + text;

        TextComponent comp = new TextComponent(mess);

        return comp;
    }

    /**
     * Helper function
     * Constructs interactive text components from message
     * @param s Unformatted string
     * @param name Name of player
     * @return Formatted text component message
     */
    private TextComponent formatInteractive(TextComponent comp, String name) {     
        TextComponent option1 = formatResponse(comp, name, 1);
        TextComponent option2 = formatResponse(comp, name, 2);
        
        String result = comp.getText().split("&option1")[0];
        TextComponent mess = new TextComponent(result);
        mess.addExtra(option1);
        mess.addExtra(" / ");
        mess.addExtra(option2);

        return mess;
    }

    /**
     * Helper function
     * Breaks apart and formats response command components
     */
    private TextComponent formatResponse(TextComponent comp, String name, int i) {
        String s = comp.getText();

        // Getting option strings
        String option1 = StringUtils.substringBetween(s, "&option" + i + "{", "}");
        String[] split = option1.split("-");
        TextComponent message = new TextComponent("[" + split[1] + "]");
        message.setBold(true);

        // Formatting command string
        String command = "/questmessager " + name + " " + step.getName() + " " + split[1];
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));

        return message;
    }
}
