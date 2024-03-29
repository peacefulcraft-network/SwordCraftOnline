package net.peacefulcraft.sco.gamehandle.announcer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.Party;
import net.peacefulcraft.sco.gamehandle.dungeon.Dungeon;
import net.peacefulcraft.sco.gamehandle.dungeon.DungeonManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

/**
 * Announcer class for SCO
 * Handles group announcing for dungeons, game, parties, etc.
 */
public abstract class Announcer {
    /**Prefix for SCO */
    private static final String prefix = ChatColor.DARK_RED + "[" + ChatColor.RED + "SwordCraftOnline" + ChatColor.DARK_RED + "]";
        public static String getPrefix() { return prefix; }

    /**
     * Cooldown map for announcer messages
     * Stores String of message sent with a map of player cooldowns
     * Integer cooldown is in ms
     */
    private static HashMap<SCOPlayer,HashMap<String, Long>> cooldowns = new HashMap<>();

    /**
     * Messages single player 
     * @param cooldown Cooldown timer of message in ms. Set to 0 means no cooldown
     */
    public static void messagePlayer(Player p, String message, int cooldown) {
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) { return; }
        
        //Adding to cooldown map
        addCooldown(message, s, cooldown);

        //Checking cooldown map
        if(checkCooldown(message, s)) {
            p.sendMessage(getPrefix() + ChatColor.WHITE + message);
        }
    }

    /**
     * Messages single player 
     * @param cooldown Cooldown timer of message in ms. Set to 0 means no cooldown
     */
    public static void messagePlayer(SCOPlayer s, String message, int cooldown) {
        addCooldown(message, s, cooldown);

        if(checkCooldown(message, s)) {
            s.getPlayer().sendMessage(getPrefix() + ChatColor.WHITE + message);
        }
    }

    /**
     * Messages group of players 
     * @param cooldown Cooldown timer of message in ms. Set to 0 means no cooldown
     */
    public static void messageGroup(Collection<SCOPlayer> l, String message, int cooldown) {
        for(SCOPlayer s : l) {
            messagePlayer(s, message, cooldown);
        }
    }

    /**
     * Messages all players in gamemanager 
     * @param cooldown Cooldown timer of message in ms. Set to 0 means no cooldown
     */
    public static void messageServer(String message, int cooldown) {
        messageGroup(GameManager.getPlayers().values(), message, cooldown);
    }

    /**
     * Messages player action bar 
     * @param cooldown Cooldown timer of message in ms. Set to 0 means no cooldown
     */
    public static void messagePlayerActionBar(Player p, String message, int cooldown) {
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) { return; }

        //Adding to cooldown map
        addCooldown(message, s, cooldown);

        //Checking cooldown map
        if(checkCooldown(message, s)) {
            (new ActionBarMessage(1, p, message)).runTaskTimer(SwordCraftOnline.getPluginInstance(), 0L, 20L);
        }
    }
    
    /**
     * Messages group of player action bar 
     * @param cooldown Cooldown timer of message in ms. Set to 0 means no cooldown
     */
    public static void messageGroupActionBar(List<Player> l, String message, int cooldown) {
        for(Player p : l) {
            messagePlayerActionBar(p, message, cooldown);
        }
    }

    /**Private class. Converts message to action bar */
	private static class ActionBarMessage extends BukkitRunnable {
		private int seconds;
		private Player p;
		private String message;
		
		public ActionBarMessage(int seconds, Player p, String message) {
			this.seconds = seconds;
			this.p = p;
			this.message = message;
		}
		
		@Override
		public void run() {
			BaseComponent base = new TextComponent(message);
			base.setColor(ChatColor.RED);
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, base);
			
			seconds--;
			if(seconds<1) {
				this.cancel();
			}
		}
    }

    /**
     * Messages all online players in a party 
     * @param cooldown Cooldown timer of message in ms. Set to 0 means no cooldown
     */
    public static void messageParty(String partyName, String message, int cooldown) {
        Party p = SwordCraftOnline.getPartyManager().getParty(partyName);
        if(p == null) { 
            SwordCraftOnline.logInfo("Failed to send message to: " + partyName); 
            return;
        }
        messageGroup(p.getOnlinePlayers(), message, cooldown);
    }

    /**
     * Messages all players in a dungeon 
     * @param cooldown Cooldown timer of message in ms. Set to 0 means no cooldown
     */
    public static void messageDungeon(int index, String message, int cooldown) {
        Dungeon d = DungeonManager.getDungeon(index);
        if(d == null) {
            SwordCraftOnline.logInfo("Failed to send message to dungeon: " + index);
            return;
        }
        messageGroup(d.getPlayers(), message, cooldown);
    }

    /**
     * Adding to cooldown map safety. Handles no cooldown internally
     * @param message String message to player
     * @param player SCOPlayer being added
     * @param cooldown Cooldown time in ms
     */
    private static void addCooldown(String message, SCOPlayer player, int cooldown) {
        //If cooldown is less than 0 we do nothing
        if(cooldown <= 0) { return; }

        if(!cooldowns.containsKey(player)) {
            cooldowns.put(player, new HashMap<String, Long>());
        }
        cooldowns.get(player).put(message, System.currentTimeMillis() + cooldown);
    }

    /**
     * Checks cooldown map and returns if player can receive message
     * @param message String message being checked
     * @param player SCOPlayer being checked
     * @return True if player can receive message. False otherwise
     */
    private static boolean checkCooldown(String message, SCOPlayer player) {
        //Player is not in map. Message can be sent
        if(!cooldowns.containsKey(player)) { return true; }

        //Message is not in map. Message can be sent
        if(!cooldowns.get(player).containsKey(message)) { return true; }

        //Cooldown has surpassed timeframe. Message can be sent
        //We remove message from map
        if(cooldowns.get(player).get(message) <= System.currentTimeMillis()) { 
            cooldowns.get(player).remove(message);
            if(cooldowns.get(player).size() == 0) { cooldowns.remove(player); }
            return true; 
        }

        //Message is in map and not expired. Message cannot be sent
        return false;
    }

    /**
     * Clears cooldown hashmap.
     */
    public static void clearCooldownMap() {
        cooldowns.clear();
    }

    public static void messageCountdown(Player p, int seconds, String preRunMessage, String postRunMessage, boolean isTitle) {
        if(isTitle) {
            Announcer.messageTitleBar(p, preRunMessage, " ");
        } else {
            Announcer.messagePlayer(p,preRunMessage, 0);
        }
        (new CountdownTimer(seconds, p, postRunMessage, isTitle)).runTaskTimer(SwordCraftOnline.getPluginInstance(), 20L, 20L);
    }

    public static void messageCountdown(List<Player> lis, int seconds, String preRunMessage, String postRunMessage, boolean isTitle){
        Announcer.messageGroup(GameManager.findSCOPlayers(lis), preRunMessage, 0);
        (new CountdownTimer(seconds, lis, postRunMessage, isTitle)).runTaskTimer(SwordCraftOnline.getPluginInstance(), 20L, 20L);
    }

    private static class CountdownTimer extends BukkitRunnable {
        private int seconds;
        private List<Player> lis;
        private String countdownMessage;
        private boolean isTitle;

        public CountdownTimer(int seconds, Player p, String postMessage, boolean isTitle) {
            this.seconds = seconds;
            lis = new ArrayList<>();
            lis.add(p);
            this.countdownMessage = postMessage;
            this.isTitle = isTitle;
        }

        public CountdownTimer(int seconds, List<Player> lis, String postMessage, boolean isTitle) {
            this.seconds = seconds;
            this.lis = lis;
            this.countdownMessage = postMessage;
            this.isTitle = isTitle;
        }

        @Override
        public void run() {
            if(seconds < 1) {
                if(isTitle) {
                    Announcer.messageTitleBar(lis, countdownMessage, " ");
                } else {
                    Announcer.messageGroup(GameManager.findSCOPlayers(lis), countdownMessage, 0);
                }
                this.cancel();
                return;
            }
            String message = String.valueOf(seconds) + "...";
            if(isTitle) {
                Announcer.messageTitleBar(lis, message, " ");
            } else {
                Announcer.messageGroup(GameManager.findSCOPlayers(lis), message, 0);
            }
            seconds--;
        }
        
    }

    /**Sends player a title bar message */
    public static void messageTitleBar(Player p, String message, String subMessage) {
        p.sendTitle(message, subMessage, 10, 70, 20);
    }

    /**Sends player a title bar message */
    public static void messageTitleBar(SCOPlayer s, String message, String subMessage) {
        Announcer.messageTitleBar(s.getPlayer(), message, subMessage);
    }

    /**Sends players a title bar message */
    public static void messageTitleBar(List<Player> lis, String message, String subMessage) {
        for(Player p : lis) {
            Announcer.messageTitleBar(p, message, subMessage);
        }
    }

    /**@return Square icon */
    public static String getSquare() {
        return "■";
    }
}