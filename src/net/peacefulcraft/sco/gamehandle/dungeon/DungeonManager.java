package net.peacefulcraft.sco.gamehandle.dungeon;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.gamehandle.player.Teleports;

public class DungeonManager {
    
    private static HashMap<Integer, Dungeon> players;
        public static HashMap<Integer, Dungeon> getPlayers() { return players; }
        public static boolean isActive(int index) { return players.get(index).getPlayers().size() > 0; }

    public DungeonManager() {
        players = new HashMap<Integer, Dungeon>();
    }

    /**
     * Adds player to dungeon hashmap. Teleporting them
     * into the dungeon arena.
     * Should only be called internally.
     */
    public void joinDungeon(SCOPlayer s, int index) {
        if(isInDungeon(s)) {
            throw new RuntimeException("Command executor is already in the dungeon.");
        }
        if(Teleports.getDungeonArena(index) == null) {
            throw new RuntimeException("No dungeon teleport set.");
        }
        if(s.getFloor() != index) {
            throw new RuntimeException("Command executor is not in proper floor.");
        }

        //Adding player to dungeon. If dungeon doesn't exist we make it.
        if(players.get(index) != null) {
            players.get(index).getPlayers().add(s);
        } else {
            Dungeon d = new Dungeon();
            d.addPlayer(s);
            players.put(index, d);
        }

        Player p = s.getPlayer();
        if(Teleports.getDungeonArena(index) != null) {
            p.teleport(Teleports.getDungeonArena(1));
            p.sendMessage("You have joined " + ChatColor.BLUE + "Dungeon " + index);
        }
    }

    /**Adding a list or party of players to a dungeon. */
    public void joinDungeon(ArrayList<SCOPlayer> ls, int index) {
        for(SCOPlayer s : ls) {
            joinDungeon(s, index);
        }
    }

    /**
     * Removes player from hashmap. Teleporting them
     * to dungeon entrance or next floor.
     */
    public void leaveDungeon(SCOPlayer s, int index, boolean win) {
        if(!isInDungeon(s)) {
            throw new RuntimeException("Command executor is not in dungeon");
        }
        if(Teleports.getDungeonEntrance(index) == null) {
            throw new RuntimeException("No dungeon teleport set.");
        }

        Player p = s.getPlayer();
        if(win) {
            p.teleport(Teleports.getWaystone(index + 1));
        } else {
            p.teleport(Teleports.getDungeonEntrance(index));
        }
        players.get(index).getPlayers().remove(s);

        p.sendMessage("You have left " + ChatColor.BLUE + "Dungeon " + index);
    }

    /**Removing a player from a dungeon assuming win condition false */
    public void leaveDungeon(SCOPlayer s, int index) {
        leaveDungeon(s, index, false);
    }

    /**Removing a list or party of players from a dungeon */
    public void leaveDungeon(ArrayList<SCOPlayer> ls, int index) {
        for(SCOPlayer s : ls) {
            leaveDungeon(s, index);
        }
    }

    /**Checking if SCOPlayer is in dungeon */
    public boolean isInDungeon(SCOPlayer s) {
        for(Dungeon d : players.values()) {
            if(d.getPlayers().contains(s)) { return true; }
        }
        return false;
    }
}