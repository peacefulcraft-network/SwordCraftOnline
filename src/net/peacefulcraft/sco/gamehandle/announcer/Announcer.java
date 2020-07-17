package net.peacefulcraft.sco.gamehandle.announcer;

import java.util.Collection;
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
    private static final String prefix = ChatColor.DARK_RED + "[" + ChatColor.RED + "SwordCrafOnline" + ChatColor.DARK_RED + "]";
        public static String getPrefix() { return prefix; }

    /**Messages single player */
    public static void messagePlayer(Player p, String message) {
        p.sendMessage(getPrefix() + ChatColor.WHITE + message);
    }

    /**Messages single player */
    public static void messagePlayer(SCOPlayer s, String message) {
        messagePlayer(s.getPlayer(), message);
    }

    /**Messages group of players */
    public static void messageGroup(Collection<SCOPlayer> l, String message) {
        for(SCOPlayer s : l) {
            messagePlayer(s, message);
        }
    }

    /**Messages player action bar */
    public static void messagePlayerActionBar(Player p, String message) {
		(new ActionBarMessage(1, p, message)).runTaskTimer(SwordCraftOnline.getPluginInstance(), 0L, 20L);
    }
    
    /**Messages group of player action bar */
    public static void messageGroupActionBar(List<Player> l, String message) {
        for(Player p : l) {
            (new ActionBarMessage(1, p, message)).runTaskTimer(SwordCraftOnline.getPluginInstance(), 0L, 20L);
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
    
    /**Messages all players in gamemanager */
    public static void messageInGame(String message) {
        messageGroup(GameManager.getPlayers().values(), message);
    }

    /**Messages all online players in a party */
    public static void messageParty(String partyName, String message) {
        Party p = SwordCraftOnline.getPartyManager().getParty(partyName);
        if(p == null) { 
            SwordCraftOnline.logInfo("Failed to send message to: " + partyName); 
            return;
        }
        messageGroup(p.getOnlinePlayers(), message);
    }

    /**Messages all players in a dungeon */
    public static void messageDungeon(int index, String message) {
        Dungeon d = DungeonManager.getDungeon(index);
        if(d == null) {
            SwordCraftOnline.logInfo("Failed to send message to dungeon: " + index);
            return;
        }
        messageGroup(d.getPlayers(), message);
    }

    /**@return Square icon */
    public static String getSquare() {
        return "■";
    }
}