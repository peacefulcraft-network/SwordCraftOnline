package net.peacefulcraft.sco.gambit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gambit.MobArena.MobConfig;
import net.peacefulcraft.sco.gamehandle.announcer.Announcer;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
import net.peacefulcraft.sco.utilities.LocationUtil;
import net.peacefulcraft.sco.utilities.Pair;
import net.peacefulcraft.sco.utilities.WorldUtil;

public class ActiveMobArena implements Listener {
    
    private MobArena ma;

    private String activeWorldName;
        
    private World activeWorld;

    private Location spawn;

    private List<Pair<Location, Location>> spawnRegions;

    private List<SCOPlayer> players;

    private List<ActiveMob> activeMobs;

    private int level;

    /**
     * Initialize active mob arena instance
     * 
     * @param ma
     * @param players
     */
    public ActiveMobArena(MobArena ma, UUID uuid) {
        this.ma = ma;
        level = 0;
        players = new ArrayList<>();

        String worldName = ma.getWorldName();
        activeWorldName = worldName + "-" + uuid.toString();
        activeWorld = WorldUtil.copyWorld(worldName, activeWorldName);

        if (activeWorld == null) {
            SwordCraftOnline.logDebug("[ActiveMobArena] Error copying world file.");
            return;
        }

        spawn = ma.getPlayerSpawn(activeWorld);
        spawnRegions = ma.getSpawnRegions(activeWorld);
    }

    /**
     * Adds player to Arena
     * 
     * @param s
     */
    public void addPlayer(SCOPlayer s) {
        players.add(s);
    }

    /**
     * Main starting sequence for active arena
     */
    public void start() {
        // Pre-start sequence
        // Teleport players to spawn 
        // Send timer message
        for (SCOPlayer s : players) { 
            s.getPlayer().teleport(spawn); 

            Announcer.messagePlayer(
                s, 
                PlayerArenaManager.getPrefix(), 
                "Welcome to my arena... You know the rules... Don't die!", 
                0);
            Announcer.messagePlayer(
                s, 
                PlayerArenaManager.getPrefix(), 
                "You have 1 minute to prepare yourselves.", 
                0);
        }

        startRound();
    }

    /**
     * Starts round with minute delay.
     * Protected internal call
     */
    private void startRound() {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
            SwordCraftOnline.getPluginInstance(), 
            new Runnable(){

                @Override
                public void run() {
                    _startRound_();
                }
        
            }, 
            1200);
    }

    /**
     * Main round logistics
     */
    private void _startRound_() {
        
        // Detailing our mob parameters
        level++;
        int modifier = level - ma.getSize();
        
        List<ActiveMobConfig> activeConfigs = new ArrayList<>();
        for (MobConfig mc : ma.getMobConfigs(level)) {
            activeConfigs.add(new ActiveMobConfig(mc, modifier));
        }

        for (SCOPlayer s : players) {
            Announcer.messageTitleBar(
                s, 
                "Begin level: " + level, 
                "Survive.");
        }

        new BukkitRunnable(){

            @Override
            public void run() {
                List<ActiveMob> roundSpawns = new ArrayList<>();
                for (ActiveMobConfig amc : activeConfigs) {
                    roundSpawns.addAll(amc.spawn(spawnRegions));
                }
                if (roundSpawns == null || roundSpawns.isEmpty()) {
                    this.cancel();
                    return;
                }

                activeMobs.addAll(roundSpawns);
            }
            
        }.runTaskTimer(SwordCraftOnline.getPluginInstance(), 20, 10);
    }

    

    /**
     * Spawn cases for mob configs
     */
    private class ActiveMobConfig {

        /**Decided spawn amount */
        private int amount;

        /**Mobs level */
        private int level;

        /**Mythic Mob internal */
        private String mm;

        private int spawned = 0;

        /**
         * Configures this levels mob settings for this config
         * @param mc Mob config we use as base
         * @param modifier level modifier from base
         */
        public ActiveMobConfig(MobConfig mc, int modifier) {
            int max = mc.getFrequencyMax() + (modifier * 6);
            int min = mc.getFrequencyMin() + (modifier * 6);

            amount = SwordCraftOnline.r.nextInt(max - min) + min;
            level = mc.getLevel() + (modifier * 2);
            mm = mc.getMMName();
        }

        /**
         * Spawns a configured mob within one of the spawning regions
         * @param regions
         * @return List of mobs spawned in this pass
         */
        public List<ActiveMob> spawn(List<Pair<Location, Location>> regions) {
            
            // Back out if we are maxed out
            List<ActiveMob> out = new ArrayList<>();
            if (spawned >= amount) { return out; }

            // Resetting spawn amount if we are about to max out
            int spawnAmount = SwordCraftOnline.r.nextInt(10);
            if (spawned + spawnAmount >= amount) {
                spawnAmount = amount - spawned;
            }

            for (int i = 0; i <= spawnAmount; i++) {

                Pair<Location, Location> p = regions.get(SwordCraftOnline.r.nextInt(regions.size() - 1));
                Location first = p.getFirst();
                Location second = p.getSecond();

                ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().spawnMob(
                    mm, 
                    LocationUtil.getLocationsWithinSquare(first, second), 
                    level);
                if (am == null) { continue; }
                out.add(am);
                spawned++;
            }

            return out;
        }

    }


}
