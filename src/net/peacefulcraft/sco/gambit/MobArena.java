package net.peacefulcraft.sco.gambit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.utilities.Pair;

/**
 * Gambit mob arena configurations
 * 
 * Notes: Supports 10 individual levels, 1-10
 */
public class MobArena {

    private MythicConfig config;

    private String file;

    private String internalName;

    private String worldName;
        public String getWorldName() { return worldName; }

    private int size;
        public int getSize() { return size; }

    /**
     * Spawn location coords
     */

    private int spawnX;
    private int spawnY;
    private int spawnZ;

    /**
     * Death box location coords
     */

    private int deathX;
    private int deathY;
    private int deathZ;

    private HashMap<Integer, MobArenaLevel> levels;
    
    private ArrayList<HashMap<String, Integer>> spawnRegions;
    
    public MobArena(String file, String internalName, MythicConfig mc) {
        config = mc;
        this.file = file;
        this.internalName = internalName;

        worldName = mc.getString("WorldName");
        spawnX = mc.getInteger("spawnX");
        spawnY = mc.getInteger("spawnY");
        spawnZ = mc.getInteger("spawnZ");
        deathX = mc.getInteger("deathBoxX");
        deathY = mc.getInteger("deathBoxY");
        deathZ = mc.getInteger("deathBoxZ");


        levels = new HashMap<>();
        for (int i = 1; i <= 10; i++) {
            List<String> levelConfigs = config.getStringList("level-" + i);
            if (levelConfigs == null || levelConfigs.isEmpty()) {
                size = i-1;
                break;
            }

            levels.put(i, new MobArenaLevel(this, levelConfigs));
        }

        spawnRegions = new ArrayList<>();
        /**Might change above loop to this.. */
        for (String s : config.getStringList("spawnRegions")) {
            HashMap<String, Integer> loc = new HashMap<>();
            for (String ss : s.split("$$")) {
                if (ss.contains("x1")) {
                    loc.put("x1", Integer.valueOf(ss.split(":")[1]));
                } else if (ss.contains("x2")) {
                    loc.put("x2", Integer.valueOf(ss.split(":")[1]));
                } else if (ss.contains("y1")) {
                    loc.put("y1", Integer.valueOf(ss.split(":")[1]));
                } else if (ss.contains("y2")) {
                    loc.put("y2", Integer.valueOf(ss.split(":")[1]));
                } else if (ss.contains("z1")) {
                    loc.put("z1", Integer.valueOf(ss.split(":")[1]));
                } else if (ss.contains("z2")) {
                    loc.put("z2", Integer.valueOf(ss.split(":")[1]));
                }
            }
            spawnRegions.add(loc);
        }
    }

    /**
     * Fetches spawn location for active world
     * @param world Active Arena world
     * @return Spawn location
     */
    public Location getPlayerSpawn(World world) {
        return new Location(world, spawnX, spawnY, spawnZ);
    }

    /**
     * Fetches death box location for active world
     * @param world Active Arena world
     * @return death box location
     */
    public Location getDeathBox(World world) {
        return new Location(world, deathX, deathY, deathZ);
    }

    /**
     * Creates pairs of locations marking spawning regions
     * @param world Active Arena world
     * @return list of spawning regions
     */
    public List<Pair<Location, Location>> getSpawnRegions(World world) {
        ArrayList<Pair<Location, Location>> out = new ArrayList<>();
        for (HashMap<String, Integer> map : spawnRegions) {
            Location loc1 = new Location(world, map.get("x1"), map.get("y1"), map.get("z1"));
            Location loc2 = new Location(world, map.get("x2"), map.get("y2"), map.get("z2"));
            out.add(new Pair<Location, Location>(loc1, loc2));
        }
        return out;
    }

    /**
     * Gets arena level mob configs
     * @param level Desired level, converted to within size
     * @return Mob configs
     */
    public List<MobConfig> getMobConfigs(int level) {
        if (level > size) {
            while (level > size) {
                level -= size;
            }
        }
        return levels.get(level).getMobConfigs();
    }

    private class MobArenaLevel {

        private MobArena parent;

        private List<MobConfig> mobConfigs;
            public List<MobConfig> getMobConfigs() { return Collections.unmodifiableList(mobConfigs); }

        public MobArenaLevel(MobArena ma, List<String> mobs) {
            parent = ma;

            mobConfigs = new ArrayList<>();
            for (String config : mobs) {
                mobConfigs.add(new MobConfig(config));
            }
        }

    }

    public class MobConfig {

        private String sConfig;

        private String mmName;
            public String getMMName() { return mmName; }

        private int frequencyMin;
            public int getFrequencyMin() { return frequencyMin; }

        private int frequencyMax;
            public int getFrequencyMax() { return frequencyMax; }

        private int level;
            public int getLevel() { return level; }

        public MobConfig(String config) {
            sConfig = config;

            List<String> split = Arrays.asList(sConfig.split("$$"));
            mmName = split.get(0);
            frequencyMin = Integer.valueOf(split.get(1));
            frequencyMax = Integer.valueOf(split.get(2));
            level = Integer.valueOf(split.get(3));
        }

    }
}
