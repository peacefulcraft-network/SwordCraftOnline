package net.peacefulcraft.sco.gamehandle.regions;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.mythicmobs.io.IOHandler;
import net.peacefulcraft.sco.mythicmobs.io.IOLoader;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;

public class RegionManager {
    
    /**Regions stored by name */
    private static HashMap<String, Region> regions = new HashMap<>();
        public static Map<String, Region> getRegionsMap() { return Collections.unmodifiableMap(regions); }

    /**Regions stored by floor number */
    private static HashMap<Integer, ArrayList<Region>> floorRegions = new HashMap<>();
        public static Map<Integer, ArrayList<Region>> getFloorRegionsMap() { return Collections.unmodifiableMap(floorRegions); }

    public RegionManager() {
        reload();
    }

    public void reload() {
        regions.clear();

        IOLoader<SwordCraftOnline> defaultRegions = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "ExampleRegions.yml", "Regions");
        defaultRegions  = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "ExampleRegions.yml", "Regions");
        List<File> regionFiles = IOHandler.getAllFiles(defaultRegions.getFile().getParent());
        List<IOLoader<SwordCraftOnline>> regionLoaders = IOHandler.getSaveLoad(SwordCraftOnline.getPluginInstance(), regionFiles, "Regions");

        for(IOLoader<SwordCraftOnline> sl : regionLoaders) {
            for(String name : sl.getCustomConfig().getConfigurationSection("").getKeys(false)) {
                MythicConfig mc = new MythicConfig(name, sl.getFile(), sl.getCustomConfig());
                String file = sl.getFile().getPath();

                Region r = new Region(file, name, mc);
                regions.put(name, r);
                
                int floor = r.getFloor();
                if(floorRegions.get(floor) == null) {
                    floorRegions.put(floor, new ArrayList<Region>());
                }
                floorRegions.get(floor).add(r);
            }
        }
        SwordCraftOnline.logInfo("[Region Manager] Loading complete!");
    }
}