package net.peacefulcraft.sco.gambit;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.io.IOHandler;
import net.peacefulcraft.sco.mythicmobs.io.IOLoader;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.utilities.WorldUtil;

public class MobArenaManager implements Runnable {
    
    private HashMap<String, MobArena> arenas;

    private HashMap<UUID, ActiveMobArena> activeArenas;

    private List<String> worldRemoveQueue;

    private BukkitTask arenaTask;

    private World baseWorld;

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
}
