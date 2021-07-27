package net.peacefulcraft.sco.gambit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gambit.MobArena.MobConfig;
import net.peacefulcraft.sco.gamehandle.announcer.Announcer;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.utilities.LocationUtil;
import net.peacefulcraft.sco.utilities.Pair;
import net.peacefulcraft.sco.utilities.WorldUtil;

public class ActiveMobArena implements Listener {
    
    /**Config instance */
    private MobArena ma;

    /**Name of this world file */
    private String activeWorldName;
    
    /**Actual world instance */
    private World activeWorld;

    /**Active world spawn location */
    private Location spawnLoc;

    /**Active death box location */
    private Location deathBoxLoc;

    /**Player death tracker */
    private HashMap<UUID, Integer> deathTracker;

    /**Players currently in death box */
    private List<SCOPlayer> deathBoxPlayers;

    /**Regions we spawn mobs in */
    private List<Pair<Location, Location>> spawnRegions;

    /**List of players */
    private List<SCOPlayer> players;

    /**Mobs spawn this round */
    private HashMap<UUID, ActiveMob> activeMobs;

    /**Current level */
    private int level;

    /**Total kills in arena */
    private int totalKills;

    /**Experience gained from group kills */
    private final int KILL_EXPERIENCE = 8;

    /**Experience removed on death */
    private final int DEATH_EXPERIENCE = -50;

    /**Experience gained from round completion */
    private final int LEVEL_EXPERIENCE = 10;

    /**
     * Initialize active mob arena instance
     * 
     * @param ma
     * @param players
     */
    public ActiveMobArena(MobArena ma, UUID uuid) {
        this.ma = ma;
        level = 0;
        totalKills = 0;
        players = new ArrayList<>();
        deathBoxPlayers = new ArrayList<>();
        deathTracker = new HashMap<>();

        String worldName = ma.getWorldName();
        activeWorldName = worldName + ":" + uuid.toString();
        activeWorld = WorldUtil.copyWorld(worldName, activeWorldName);

        if (activeWorld == null) {
            SwordCraftOnline.logDebug("[ActiveMobArena] Error copying world file.");
            return;
        }

        // Configured active locations
        spawnLoc = ma.getPlayerSpawn(activeWorld);
        spawnRegions = ma.getSpawnRegions(activeWorld);
        deathBoxLoc = ma.getDeathBox(activeWorld);
    }

    /**
     * Adds player to Arena
     * 
     * @param s
     */
    public void addPlayer(SCOPlayer s) {
        players.add(s);
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void arenaDeathListener(EntityDamageEvent ev) {
        Entity e = ev.getEntity();
        ModifierUser mu = ModifierUser.getModifierUser(e);
        if (mu == null) { return; }

        double damage = mu.calculateDamage(ev.getCause().toString(), ev.getFinalDamage(), true);
        if (mu.getHealth() - damage <= 0) {
            if (mu instanceof SCOPlayer) {
                ev.setCancelled(true);

                SCOPlayer s = (SCOPlayer)mu;
                if (!players.contains(s)) { return; }

                s.setHealth(s.getMaxHealth());

                // Death box logic
                deathTracker.put(s.getUUID(), deathTracker.get(s.getUUID()) + 1);
                deathBoxPlayers.add(s);
                s.getPlayer().teleport(deathBoxLoc);

                // All players died
                if (deathBoxPlayers.size() == players.size()) {
                    end();
                }
            } else if (mu instanceof ActiveMob) {
                ActiveMob am = (ActiveMob)mu;
                if (!activeMobs.keySet().contains(am.getUUID())) { return; }

                activeMobs.remove(am.getUUID());
                totalKills++;

                // End of round logic
                if (activeMobs.isEmpty()) {
                    endRound();
                }
            }
        }
    }

    /**
     * Main starting sequence for active arena
     */
    public void start() {
        // Pre-start sequence
        // Teleport players to spawn 
        // Send timer message
        for (SCOPlayer s : players) { 
            s.getPlayer().teleport(spawnLoc); 

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

            deathTracker.put(s.getUUID(), 0);
        }

        startRound();
    }

    /**
     * Main ending sequence for active arena
     * Called when all players die in a round
     */
    private void end() {
        for (SCOPlayer s : players) {
            Announcer.messageTitleBar(
                s, 
                "Defeat!", 
                "Level " + level);

            int deaths = deathTracker.get(s.getUUID());
            int exp = (totalKills * KILL_EXPERIENCE) + (level * LEVEL_EXPERIENCE) - (deaths * DEATH_EXPERIENCE);

            s.setAlphaExperience(s.getAlphaExperience() + exp);

            Announcer.messagePlayer(
                s, 
                PlayerArenaManager.getPrefix(),
                "Match Summary!\n Group Kills: " + totalKills + 
                    "\n Level Reached: " + level + "\n Deaths: " + deaths + "\n Total: " + exp,
                0);

            SwordCraftOnline.getPluginInstance().getMobArenaManager().teleportBaseSpawn(s);
        }

        // Queuing world unload
        SwordCraftOnline.getPluginInstance().getMobArenaManager().addWorldRemove(activeWorldName);
    }

    /**
     * Triggers end of round logic then begins
     * timer for next round
     */
    private void endRound() {
        // Teleporting any dead players out of death box
        for (SCOPlayer s : deathBoxPlayers) {
            s.getPlayer().teleport(spawnLoc);
            Announcer.messagePlayer(
                s, 
                PlayerArenaManager.getPrefix(), 
                "Fine! You get one more chance... But I'm taking my cut!", 
                0);
        }
        deathBoxPlayers.clear();

        for (SCOPlayer s : players) {
            Announcer.messageTitleBar(
                s, 
                "Level " + level + " complete!", 
                "One minute until next level.");

            Announcer.messagePlayer(
                s, 
                PlayerArenaManager.getPrefix(), 
                "You killed " + totalKills + " of my army that time!..", 
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

                for (ActiveMob am : roundSpawns) {
                    activeMobs.put(am.getUUID(), am);
                }
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
