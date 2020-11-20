package net.peacefulcraft.sco.quests.messages;

import java.util.HashMap;
import java.util.Map;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.quests.QuestStep;
import net.peacefulcraft.sco.quests.messages.Message.MessageType;

/**
 * Handles message progression and display logic
 */
public class MessageHandler {
    
    private HashMap<String, Message> messages;

    private QuestStep step;

    /**
     * Constructor
     * @param entries Entries pulled from .yml
     */
    public MessageHandler(Map<?,?> entries, QuestStep step) {
        this.messages = new HashMap<>();
        this.step = step;

        // Converting entry map into message map
        for(Object i : entries.keySet()) {
            Object value = entries.get(i);
            messages.put(i.toString(), new Message(value.toString(), step, this));
        }
    }

    /**
     * Searches for startup message and sends to player
     */
    public void sendStartup(SCOPlayer s) {
        for(Message m : messages.values()) {
            if(m.getType().equals(MessageType.STARTUP)) {
                m.sendMessage(s);
            }
        }
    }

    /**
     * Searches for complete message and sends to player
     */
    public void sendComplete(SCOPlayer s) {
        for(Message m : messages.values()) {
            if(m.getType().equals(MessageType.COMPLETE)) {
                m.sendMessage(s);
            }
        }
    }

    /**
     * Sends correct message to player
     * @param name Name of message in handler
     * @param s Target player
     */
    public void sendMessage(String name, SCOPlayer s) {
        messages.get(name).sendMessage(s);
    }

    /**
     * Gets text of message
     * @param name Name of message
     * @return Text
     */
    public String getMessageText(String name) {
        return messages.get(name).getText();
    }
}
