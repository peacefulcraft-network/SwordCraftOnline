package net.peacefulcraft.sco.mythicmobs.mobs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.mythicmobs.io.IOHandler;
import net.peacefulcraft.sco.mythicmobs.io.IOLoader;

/**
 * 
 * Weeber mini-boss mob
 * Hunts players who say its name on nightwave
 * 
 */
public class WeeperManager {
    
    private List<String> targets;

    public WeeperManager() {
        targets = new ArrayList<>();

        
    }

    /**
     * Load player targets
     */
    public void load() {
        targets.clear();

        // Converting loaders from list to single
        IOLoader<SwordCraftOnline> defaultTargets = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "WeeperTargets.yml", "WeeperData");
        defaultTargets = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "WeeperTargets.yml", "WeeperData");
        List<File> targetFile = new ArrayList<File>();
        targetFile.add(defaultTargets.getFile());
        
        List<IOLoader<SwordCraftOnline>> loaders = IOHandler.getSaveLoad(SwordCraftOnline.getPluginInstance(), targetFile, "WeeperData");
        IOLoader<SwordCraftOnline> loader = loaders.get(0);

        targets = loader.getCustomConfig().getStringList("Targets");
    }

    /**
     * Save player targets
     */
    public void save() {
        IOLoader<SwordCraftOnline> file = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "WeeperTargets.yml", "WeeperData");
        FileConfiguration config = file.getCustomConfig();

        config.set("Targets", targets);

        try {
            config.save(file.getFile());
        } catch(IOException ex) {
            SwordCraftOnline.logDebug("[WeeperManager] Failed to save WeeperTargets.yml");
        }
        SwordCraftOnline.logDebug("[WeeperManager] WeeperTargets.yml saved!");
    }

    /**
     * Triggers weeber boss
     */
    public void trigger() {



    }

}
