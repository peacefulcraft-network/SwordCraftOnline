package net.peacefulcraft.sco.gambit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.announcer.Announcer;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.utilities.Pair;

public class PlayerArenaManager implements Runnable, Listener {

    private static final String GREED_PREFIX = ChatColor.RED + "[" + ChatColor.DARK_PURPLE + "Greed" + ChatColor.RED + "]";
        public static final String getPrefix() { return GREED_PREFIX; }
    
    private Queue<Pair<SCOPlayer, SCOPlayer>> playerQueue;

    private HashMap<Pair<SCOPlayer, SCOPlayer>, Long> openChallenges;

    private BukkitTask challengeTask;

    private Pair<SCOPlayer, SCOPlayer> currentMatch;

    private final Location ARENA_LOC = new Location(Bukkit.getWorld("SwordCraftOnline"), 58, 5, 177);
    private final Location SPAWN_LOC = new Location(Bukkit.getWorld("SwordCraftOnline"), 58, 5, 166);

    public PlayerArenaManager() {
        playerQueue = new LinkedList<>();
        openChallenges = new HashMap<Pair<SCOPlayer, SCOPlayer>, Long>();
        currentMatch = null;

        // Task runs every 15 seconds
        challengeTask = Bukkit.getServer().getScheduler().runTaskTimer(SwordCraftOnline.getPluginInstance(), this, 20, 300);

        SwordCraftOnline.logDebug("[PlayerArenaManager] Loading complete!");
    }

    @Override
    public void run() {
        SwordCraftOnline.logDebug("[PlayerArenaManager] Initiating task run.");

        // Clearing expired challenges
        Iterator<Entry<Pair<SCOPlayer, SCOPlayer>, Long>> iter = openChallenges.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<Pair<SCOPlayer, SCOPlayer>, Long> entry = iter.next();

            if (entry.getValue() + 60000 < System.currentTimeMillis()) {
                Pair<SCOPlayer, SCOPlayer> pair = entry.getKey();
                Announcer.messagePlayer(
                    pair.getFirst(), 
                    GREED_PREFIX,
                    "Challenge against " + pair.getSecond().getName() + " has expired", 
                    0);
                Announcer.messagePlayer(
                    pair.getSecond(), 
                    GREED_PREFIX,
                    "Challenge from " + pair.getFirst().getName() + " has expired", 
                    0);
                iter.remove();
            }
        }

        // Moving queue into arena
        if (currentMatch == null) {
            try {
                SwordCraftOnline.logDebug("[PlayerArenaManager] Preparing new current match.");
                Pair<SCOPlayer, SCOPlayer> pair = playerQueue.remove();
                while (!pairHealthCheck(pair)) {
                    pair = playerQueue.remove();    
                }

                currentMatch = pair.clone();
                SwordCraftOnline.logDebug("[PlayerArenaManager] CurrentMatch: " + currentMatch.toString());

                pair.getFirst().getPlayer().teleport(ARENA_LOC);
                pair.getSecond().getPlayer().teleport(ARENA_LOC);

            } catch(NoSuchElementException e) {}
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void DeathPreventionListener(EntityDamageEvent ev) {
        // Don't care if match is null
        if (currentMatch == null) { return; }

        Entity e = ev.getEntity();
        if (!(e instanceof Player)) { return; }

        SCOPlayer s = GameManager.findSCOPlayer((Player)e);
        if (s == null) { return; }
        
        // Name check
        if (!s.getName().equalsIgnoreCase(currentMatch.getFirst().getName()) &&
            !s.getName().equalsIgnoreCase(currentMatch.getSecond().getName())) { return; }

        // Main processing
        double damage = s.calculateDamage(ev.getCause().toString(), ev.getFinalDamage(), true);
        if (s.getHealth() - damage <= 0) { 

            SwordCraftOnline.logDebug("[PlayerArenaManager] End match triggered.");

            if (s.getName().equalsIgnoreCase(currentMatch.getFirst().getName())) { 
                endMatch(currentMatch.getSecond(), currentMatch.getFirst());
            } else {
                endMatch(currentMatch.getFirst(), currentMatch.getSecond());
            }
            ev.setCancelled(true);
        }
    }

    private void endMatch(SCOPlayer winner, SCOPlayer loser) {
        winner.getPlayer().teleport(SPAWN_LOC);
        loser.getPlayer().teleport(SPAWN_LOC);

        winner.setHealth(winner.getMaxHealth());
        loser.setHealth(loser.getMaxHealth());

        // Updating winstreak data
        winner.setAlphaWinStreak(winner.getAlphaWinStreak() + 1);
        loser.setAlphaWinStreak(0);

        // Gifting experience
        int winnerExp = winner.getAlphaExperience() + 150 + (50 * (winner.getAlphaWinStreak() - 2));
        winner.setAlphaExperience(winnerExp);
        loser.setAlphaExperience(loser.getAlphaExperience() + 80);

        // Sending messages
        Announcer.messageTitleBar(winner, "Greed's Gambit Victory!", "Gifted " + winnerExp + " Gambit Experience.");
        Announcer.messageTitleBar(loser, "Greed's Gambit Defeat!", "Gifted 80 Gambit Experience.");

        // Clearing variables
        currentMatch = null;
    }

    /**
     * Opens a challenge against a player. Must be accepted to
     * be put into the official queue
     * @param challenger
     * @param challenged
     */
    public void initiateChallenge(SCOPlayer challenger, SCOPlayer challenged) {

        // Check pre-existing challenge
        for (Pair<SCOPlayer, SCOPlayer> pair : openChallenges.keySet()) {
            if (pair.getFirst().getName().equalsIgnoreCase(challenger.getName())) {
                Announcer.messagePlayer(challenger, GREED_PREFIX, "Cannot initiate more than one challenge!", 0);
                return;
            }
        }

        Announcer.messagePlayer(challenger, GREED_PREFIX, "Sending challenge to " + challenged.getName(), 0);

        openChallenges.put(new Pair<SCOPlayer, SCOPlayer>(challenger, challenged), System.currentTimeMillis());

        Announcer.messagePlayer(
            challenged, 
            GREED_PREFIX,
            "You have been challenged to a 1v1! Type: /challenge [accept/deny]. You have 1 minute to accept.",
            0);
    }

    /**
     * Accepts / denies challenge if it exists
     * @param challenged
     * @param accept
     */
    public void respondChallenge(SCOPlayer challenged, boolean accept) {
        Iterator<Entry<Pair<SCOPlayer, SCOPlayer>, Long>> iter = openChallenges.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<Pair<SCOPlayer, SCOPlayer>, Long> entry = iter.next();
            
            Pair<SCOPlayer, SCOPlayer> pair = entry.getKey();
            if (pair.getSecond().getName().equalsIgnoreCase(challenged.getName())) {
                // Challenge expired
                if (entry.getValue() + 3600 < System.currentTimeMillis()) {
                    Announcer.messagePlayer(challenged, GREED_PREFIX, "Challenge has expired!", 0);
                    iter.remove();
                } else {
                    if (accept) {
                        addToQueue(pair.getFirst(), pair.getSecond());
                        Announcer.messagePlayer(challenged, GREED_PREFIX, "Challenge accepted!", 0);
                    } else {
                        Announcer.messagePlayer(challenged, GREED_PREFIX, "Challenge denied!", 0);
                        Announcer.messagePlayer(pair.getFirst(), GREED_PREFIX, "Challenge was denied!", 0);
                    }
                    iter.remove();
                }
            }
        }  
    } 

    /**
     * Adds two players to arena queue
     * @param playerOne
     * @param playerTwo
     */
    private void addToQueue(SCOPlayer playerOne, SCOPlayer playerTwo) {
        Pair<SCOPlayer, SCOPlayer> pair = new Pair<SCOPlayer, SCOPlayer>(playerOne, playerTwo);
        playerQueue.add(pair);
    }

    /**
     * Queue health check
     * @param pair
     * @return
     */
    private boolean pairHealthCheck(Pair<SCOPlayer, SCOPlayer> pair) {
        if (pair.getFirst() == null || pair.getSecond() == null) {
            return false;
        }
        
        SCOPlayer checkOne = GameManager.findSCOPlayer(pair.getFirst().getPlayer());
        if (checkOne == null) { return false; }

        SCOPlayer checkTwo = GameManager.findSCOPlayer(pair.getSecond().getPlayer());
        if (checkTwo == null) { return false; }

        return true;
    }

}
