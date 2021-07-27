package net.peacefulcraft.sco.gambit;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.announcer.Announcer;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.io.IOHandler;
import net.peacefulcraft.sco.mythicmobs.io.IOLoader;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.utilities.WorldUtil;

public class MobArenaManager implements Runnable, Listener {
    
    private HashMap<String, MobArena> arenas;

    private HashMap<UUID, ActiveMobArena> activeArenas;

    private List<String> worldRemoveQueue;

    private BukkitTask arenaTask;

    private World baseWorld;

    private HashMap<SCOPlayer, MobArenaParty> parties;

    public MobArenaManager() {

        arenas = new HashMap<>();
        activeArenas = new HashMap<>();
        worldRemoveQueue = new LinkedList<>();
        load();

        baseWorld = Bukkit.getServer().getWorld("GambitArena");
        arenaTask = Bukkit.getServer().getScheduler().runTaskTimer(SwordCraftOnline.getPluginInstance(), this, 20, 1200);
    }

    public void load() {
        IOLoader<SwordCraftOnline> defaultConfig = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "ExampleConfig.yml", "GambitArenas");
        defaultConfig = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "ExampleConfig.yml", "GambitArenas");
        List<File> arenaFiles = IOHandler.getAllFiles(defaultConfig.getFile().getParent());
        List<IOLoader<SwordCraftOnline>> arenaLoaders = IOHandler.getSaveLoad(SwordCraftOnline.getPluginInstance(), arenaFiles, "GambitArenas");

        for (IOLoader<SwordCraftOnline> sl : arenaLoaders) {
            for (String name : sl.getCustomConfig().getConfigurationSection("").getKeys(false)) {
                try {
                    MythicConfig mc = new MythicConfig(name, sl.getFile(), sl.getCustomConfig());
                    String file = sl.getFile().getPath();

                    String arenaName = sl.getCustomConfig().getString(name + ".ArenaName");
                    arenaName = sl.getCustomConfig().getString(name + ".Name", arenaName);

                    MobArena ma = new MobArena(file, name, mc);
                    arenas.put(name, ma);
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Initializes active arena of desired map config
     * @param arenaName wanted map config
     * @return initialized active instance
     */
    public ActiveMobArena initializeArena(String arenaName) {
        UUID uuid = UUID.randomUUID();
        ActiveMobArena ama = new ActiveMobArena(
            arenas.get(arenaName), 
            uuid);
        activeArenas.put(uuid, ama);
        return ama;
    }

    /**
     * Teleports player to base world spawn location
     * @param s
     */
    public void teleportBaseSpawn(SCOPlayer s) {
        s.getPlayer().teleport(baseWorld.getSpawnLocation());
    }

    /**
     * Queues world for removal
     * @param name
     */
    public void addWorldRemove(String name) {
        worldRemoveQueue.add(name);
    }

    @Override
    public void run() {
        for (String name : worldRemoveQueue) {
            WorldUtil.unloadWorld(name);
            UUID uuid = UUID.fromString(name.split(":")[1]);
            activeArenas.remove(uuid);
        }
        worldRemoveQueue.clear();
    }

    /*
     * 
     * Gambit party logic
     *
     */

    @EventHandler
    public void playerLogOff(PlayerQuitEvent ev) {
        SCOPlayer s = GameManager.findSCOPlayer(ev.getPlayer());
        if (s == null) { return; }

        if (parties.containsKey(s)) {
            disbandParty(s);
            return;
        }

        for (MobArenaParty map : parties.values()) {
            if (map.checkMembers(s)) {
                map.leave(s);
            }
        }
    }

    /**
     * Begins arena loading process and moves players to this arena
     * @param leader
     */
    public void movePartyToArena(SCOPlayer leader, String arenaName) {
        MobArenaParty map = parties.get(leader);
        if (map == null) { return; }
        
        // We delay ten seconds from teleport to ensure world is configured.
        ActiveMobArena ama = initializeArena(arenaName);
        if (ama == null) {
            Announcer.messagePlayer(leader, PlayerArenaManager.getPrefix(), "Issue loading your Gambit... Try again soon.", 0);
            return;
        }

        for (SCOPlayer s : map.getMembers()) {
            Announcer.messagePlayer(s, PlayerArenaManager.getPrefix(), "Creating your Gambit.. 10 seconds!", 0);
        }

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
            SwordCraftOnline.getPluginInstance(), 
            new Runnable(){

                @Override
                public void run() {
                    
                    for (SCOPlayer s : map.getMembers()) {
                        ama.addPlayer(s);
                    }

                    ama.start();
                }
                
            }, 200);
    }

    /**
     * Creates new mob arena party
     * @param leader Owner of the party
     */
    public void createParty(SCOPlayer leader) {
        MobArenaParty map = new MobArenaParty(leader);
        parties.put(leader, map);

        Announcer.messagePlayer(
            leader, 
            PlayerArenaManager.getPrefix(), 
            "You are now a Gambit Party leader..", 
            0);
    }

    /**
     * Creates new mob arena party
     * @param leader
     */
    public void createParty(Player leader) {
        SCOPlayer s = GameManager.findSCOPlayer(leader);
        if (s == null) { return; }
        createParty(s);
    }

    /**
     * Sends party invite
     * @param leader
     * @param name
     */
    public void sendPartyInvite(SCOPlayer leader, String name) {
        MobArenaParty map = parties.get(leader);

        SCOPlayer invitee = GameManager.findSCOPlayer(name);
        if (invitee == null) {
            Announcer.messagePlayer(
                leader, 
                PlayerArenaManager.getPrefix(), 
                "Cannot send invite to this person!", 
                0);
            return;
        }
        map.sendInvite(invitee);
    }

    /**
     * Accepts / deny party invite
     * @param invitee
     * @param leader
     * @param accept
     */
    public void acceptPartyInvite(SCOPlayer invitee, String leader, boolean accept) {
        SCOPlayer s = GameManager.findSCOPlayer(leader);
        if (s == null) {
            Announcer.messagePlayer(invitee, PlayerArenaManager.getPrefix(), "Party not found..", 0);
            return;
        }

        MobArenaParty map = parties.get(s);
        if (map == null) {
            Announcer.messagePlayer(invitee, PlayerArenaManager.getPrefix(), "Party not found..", 0);
            return;
        }

        for (MobArenaParty mapp : parties.values()) {
            if (mapp.checkMembers(invitee)) {
                Announcer.messagePlayer(invitee, PlayerArenaManager.getPrefix(), "Idiot.. You cannot join another party.", 0);
                return;
            }
        } 

        map.acceptInvite(invitee, accept);
    }

    /**
     * Disbands party by leader
     * @param leader
     */
    public void disbandParty(SCOPlayer leader) {
        MobArenaParty map = parties.get(leader);
        if (map == null) { return; }

        map.disband();
        parties.remove(leader);
    }

    private class MobArenaParty {

        private SCOPlayer leader;

        private List<SCOPlayer> members;
            public List<SCOPlayer> getMembers() { return Collections.unmodifiableList(members); }

        private HashMap<SCOPlayer, Long> invites;

        public MobArenaParty(SCOPlayer leader) {
            this.leader = leader;
            
            members = new ArrayList<>();
            invites = new HashMap<>();

            members.add(leader);
        }

        /** 
         * Verifies if player is member of this party
         */
        public boolean checkMembers(SCOPlayer s) {
            return members.contains(s);
        }

        /**
         * Removes player from party
         * @param s
         */
        public void leave(SCOPlayer s) {
            members.remove(s);
        }

        /**
         * Sends disband messages to members
         */
        public void disband() {
            for (SCOPlayer member : members) {
                Announcer.messagePlayer(member, PlayerArenaManager.getPrefix(), "Party disbanded..", 0);
            }
        }

        /**
         * Sends player invite to party
         * @param s
         */
        public void sendInvite(SCOPlayer s) {
            invites.put(s, System.currentTimeMillis() + 60000);
            Announcer.messagePlayer(
                s, 
                PlayerArenaManager.getPrefix(), 
                "You have been invite to a Gambit..\nType: /gambit accept " + leader.getName() + 
                "\n to accept or: /gambit deny " + leader.getName(), 
                0);
        }

        public void acceptInvite(SCOPlayer s, boolean accept) {
            Long time = invites.get(s);
            if (time == null) { return; }

            if (time < System.currentTimeMillis()) { 
                Announcer.messagePlayer(s, PlayerArenaManager.getPrefix(), "Invite has expired!", 0);
                invites.remove(s);
             
            } else if (accept) {
                members.add(s);
                for (SCOPlayer member : members) {
                    Announcer.messagePlayer(
                        member, 
                        PlayerArenaManager.getPrefix(),
                        s.getName() + " has joined the Gambit..", 
                        0);
                }
                invites.remove(s);
            } else {
                invites.remove(s);
                Announcer.messagePlayer(s, 
                    PlayerArenaManager.getPrefix(), 
                    "Denied " + leader.getName() + "'s invite.", 
                    0);
                Announcer.messagePlayer(
                    leader, 
                    PlayerArenaManager.getPrefix(), 
                    s.getName() + " has denied your invite.", 
                    0);
            }
        }
    }
}
