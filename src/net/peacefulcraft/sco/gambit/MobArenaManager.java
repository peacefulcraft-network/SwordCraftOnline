package net.peacefulcraft.sco.gambit;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.mythicmobs.io.IOHandler;
import net.peacefulcraft.sco.mythicmobs.io.IOLoader;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;

public class MobArenaManager {
    
    private HashMap<String, MobArena> arenas;

    private HashMap<UUID, ActiveMobArena> activeArenas;

    public MobArenaManager() {

        arenas = new HashMap<>();
        activeArenas = new HashMap<>();
        load();

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
}
