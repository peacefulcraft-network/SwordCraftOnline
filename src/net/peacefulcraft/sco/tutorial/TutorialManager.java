package net.peacefulcraft.sco.tutorial;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.entity.Player;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

public class TutorialManager {
    
    /**List of players in tutorial */
    private static ArrayList<SCOPlayer> players;
        public static List<SCOPlayer> getPlayers() { return Collections.unmodifiableList(players); }

    public TutorialManager() {
        players = new ArrayList<SCOPlayer>();
    }

    /**Main call to process players joining tutorial */
    public void joinTutorial(SCOPlayer s) {
        players.add(s);
    }

    public void joinTutorial(Player p) {
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) {
            SwordCraftOnline.logInfo("Player not yet in SCO attempted to join tutorial: " + p.getName());
            return;
        }
        joinTutorial(s);
    }

    /**Main call to process players leaving tutorial */
    public void leaveTutorial(SCOPlayer s) {
        players.remove(s);
    }

    public void leaveTutorial(Player p) {
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) {
            SwordCraftOnline.logInfo("Player not yet in SCO attempted to leave tutorial: " + p.getName());
            return;
        }
        leaveTutorial(s);
    }

    public boolean isInTutorial(SCOPlayer s) {
        return players.contains(s);
    }

    public boolean isInTutorial(Player p) {
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) { return false; }
        return isInTutorial(s);
    }
}