package net.peacefulcraft.sco.gambit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;

/**
 * Gambit mob arena configurations
 * 
 * Notes: Supports 10 individual levels, 1-10
 */
public class MobArena {

    private MythicConfig config;

    private String file;

    private String internalName;

    private int size;
        public int getSize() { return size; }

    private HashMap<Integer, MobArenaLevel> levels;
    
    public MobArena(String file, String internalName, MythicConfig mc) {
        config = mc;
        this.file = file;
        this.internalName = internalName;

        levels = new HashMap<>();

        for (int i = 1; i <= 10; i++) {
            List<String> levelConfigs = config.getStringList("level-" + i);
            if (levelConfigs == null || levelConfigs.isEmpty()) {
                size = i-1;
                break;
            }

            levels.put(i, new MobArenaLevel(this, levelConfigs));
        }
    }

    private class MobArenaLevel {

        private MobArena parent;

        private List<MobConfig> mobConfigs;

        public MobArenaLevel(MobArena ma, List<String> mobs) {
            parent = ma;

            mobConfigs = new ArrayList<>();
            for (String config : mobs) {
                mobConfigs.add(new MobConfig(config));
            }
        }

    }

    private class MobConfig {

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
