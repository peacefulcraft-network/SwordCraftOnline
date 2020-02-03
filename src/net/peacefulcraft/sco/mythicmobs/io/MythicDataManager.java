package net.peacefulcraft.sco.mythicmobs.io;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class MythicDataManager {

    private String configName;
        public void setKey(String key) { this.configName = key; }
        public String getKey() { return this.configName; }

    private File file;
        public File getFile() { return this.file; }

    private FileConfiguration fc;
        public FileConfiguration getFileConfiguration() { return this.fc; }

    public MythicDataManager(String name, FileConfiguration fc) {
        this.configName = name;
        this.fc = fc;
    }

    public MythicDataManager(String name, File file, FileConfiguration fc) {
        this(name, fc);
        this.file = file;
    }

    public MythicDataManager(String name, File file) {
        this.configName = name;
        this.file = file;
        this.fc = (FileConfiguration)new YamlConfiguration();
        this.fc.createSection(this.configName);
    }
    
    public boolean isSet(String field) {
        return this.fc.isSet(this.configName + "." + field);
    }    

    public void set(String key, Object value) {
        this.fc.set(this.configName + "." + key,value);
    }

    public void load() {
        this.fc = (FileConfiguration)YamlConfiguration.loadConfiguration(this.file);
    }

    public void save() {
        try {
            this.fc.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}