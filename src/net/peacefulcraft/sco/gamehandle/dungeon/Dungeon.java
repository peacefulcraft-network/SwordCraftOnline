package net.peacefulcraft.sco.gamehandle.dungeon;

import java.util.ArrayList;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

public class Dungeon {
    
    /**List of players currently in dungeon */
    private static ArrayList<SCOPlayer> players;
        public ArrayList<SCOPlayer> getPlayers() { return players; }
        public void addPlayer(SCOPlayer s) { players.add(s); }

    /**Determines boss difficulty and rewards scaling */
    private int difficulty;
        public int getDifficulty() { return difficulty; }

    public Dungeon() {
        players = new ArrayList<SCOPlayer>();
        difficulty = setDifficulty();
    }

    /**
     * Handles difficulty calculation based on average of
     * player levels.
     * TODO: Consider ways to update this for more accurate difficulty calculation. 
     */
    private int setDifficulty() {
        int levelSum = 0;
        for(SCOPlayer s : players) {
            levelSum += s.getLevel();
        }
        return levelSum / players.size();
    }
}