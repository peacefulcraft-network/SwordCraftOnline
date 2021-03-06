package net.peacefulcraft.sco.mythicmobs.spawners;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.announcer.Announcer;
import net.peacefulcraft.sco.mythicmobs.io.IOHandler;
import net.peacefulcraft.sco.mythicmobs.io.IOLoader;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;

public class SpawnerManager implements Runnable {

    /**The ./SpawnerConfig.yml config for active spawner loading */
    private FileConfiguration config;

    private BukkitTask spawnerTask;
        public void toggleSpawnerTask() {
            if(spawnerTask.isCancelled()) {
                this.spawnerTask = Bukkit.getServer().getScheduler().runTaskTimer(SwordCraftOnline.getPluginInstance(), this, 20, 30);
            } else {
                this.spawnerTask.cancel();
            }
        }

    /**Stores Spawner: Key file name */
    private HashMap<String, Spawner> spawnerList = new HashMap<>();
        public Map<String, Spawner> getSpawnerList() { return Collections.unmodifiableMap(this.spawnerList); }

    /**Stores active spawners. Key: Spawner name, Value: list of active spawners*/
    private HashMap<String, ArrayList<ActiveSpawner>> spawnerRegistry = new HashMap<>();
        public Map<String, ArrayList<ActiveSpawner>> getSpawnerRegistry() { return Collections.unmodifiableMap(this.spawnerRegistry); }

    private HashMap<String, ArrayList<ActiveSpawner>> passiveRegistry = new HashMap<>();
        public Map<String, ArrayList<ActiveSpawner>> getPassiveRegistry() { return Collections.unmodifiableMap(this.passiveRegistry); }
        public List<ActiveSpawner> getPassiveList() { return this.passiveRegistry.values().stream().flatMap(List::stream).collect(Collectors.toList()); }

    private HashMap<String, ArrayList<ActiveSpawner>> hostileRegistry = new HashMap<>();
        public Map<String, ArrayList<ActiveSpawner>> getHostileRegistry() { return Collections.unmodifiableMap(this.hostileRegistry); }
        public List<ActiveSpawner> getHostileList() { return this.hostileRegistry.values().stream().flatMap(List::stream).collect(Collectors.toList()); }

    /**Stores values for active spawners registry in one list */
    private List<ActiveSpawner> registryList = new ArrayList<>();
        public List<ActiveSpawner> getRegistryList() { return Collections.unmodifiableList(this.registryList); }
    
    /**Determines if nightwave event is active */
    private boolean isNightwave;
        public boolean isNightwave() { return this.isNightwave; }
        public void toggleNightwave() { isNightwave = !isNightwave; }

    private boolean isHighlighting;
        private void setIsHighlighting(boolean b) { this.isHighlighting = b; }

    /**
     * Constructs spawner manager and calls reload sequence.
     * Activates spawner task.
     */
    public SpawnerManager() {
        
        loadSequence();

        /**Setting to run spawner task once a second */
        this.spawnerTask = Bukkit.getServer().getScheduler().runTaskTimer(SwordCraftOnline.getPluginInstance(), this, 20, 30);
        this.isNightwave = false;
        this.isHighlighting = false;
    }

    /**Called on plugin reload or individual reload. */
    public void loadSequence() {
        loadSpawners();
        reload(false);
    }

    /**Saves all active spawners to SpawnerConfig.yml */
    public void save() { 
        IOLoader<SwordCraftOnline> config = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "SpawnerConfig.yml");
        this.config = config.getCustomConfig();
        
        /**Reading all active spawners from registry by key */
        for(String name : getSpawnerRegistry().keySet()) {
            ArrayList<Object> lis = new ArrayList<>();
            for(ActiveSpawner as : getSpawnerRegistry().get(name)) {
                lis.add(as.getConfig());
            }
            this.config.createSection(name);
            this.config.set(name, lis);
        }
        try{
            this.config.save(config.getFile());
        }catch(IOException ex) {
            SwordCraftOnline.logInfo("[Spawner Manager] Failed to save SpawnerConfig.yml");
            return;
        }

        this.spawnerRegistry.clear();
        SwordCraftOnline.logInfo("[Spawner Manager] SpawnerConfig saved!");
    }

    /**Loads all active spawners from SpawnerConfig.yml and activates them in the world */
    public void reload(boolean silent) {
        /**Loading spawner config for RW */
        IOLoader<SwordCraftOnline> config = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "SpawnerConfig.yml");
        this.config = config.getCustomConfig();

        for(String name: getSpawnerList().keySet()) {
            try{
                List<Map<?, ?>> lis = this.config.getMapList(name);
                if(lis.isEmpty()) { continue; }
                for(Map<?,?> m : lis) {
                    Location loc = new Location(
                        Bukkit.getWorld((String) m.get("world")),
                        Double.parseDouble(String.valueOf(m.get("x"))),
                        Double.parseDouble(String.valueOf(m.get("y"))),
                        Double.parseDouble(String.valueOf(m.get("z"))));
                    
                    int level = Integer.valueOf((String)m.get("level"));
                    ActiveSpawner as = setSpawner(name, loc, level, silent);
                    if(as == null) {
                        SwordCraftOnline.logInfo("[Spawner Manager] Error loading: " + name + " for invalid Active Spawner parameter.");
                        continue;
                    }
                }
            }catch(IllegalArgumentException ex) {
                SwordCraftOnline.logInfo("[Spawner Manager] Illegal argument in: " + name);
                continue;
            }
        }
    }

    /**Loads all spawners from ./Spawners/ into active storage */
    public void loadSpawners() {
        
        this.spawnerList.clear();
        this.registryList.clear();

        IOLoader<SwordCraftOnline> defaultSpawner = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "ExampleSpawner.yml", "Spawners");
        defaultSpawner = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "ExampleSpawner.yml", "Spawners");
        List<File> spawnerFiles = IOHandler.getAllFiles(defaultSpawner.getFile().getParent());
        List<IOLoader<SwordCraftOnline>> spawnerLoaders = IOHandler.getSaveLoad(SwordCraftOnline.getPluginInstance(), spawnerFiles, "Spawners");

        for(IOLoader<SwordCraftOnline> s1 : spawnerLoaders) {
            loaderLoop:
                for(String name : s1.getCustomConfig().getConfigurationSection("").getKeys(false)) {
                    try {
                        /**Fetching mythic config and file name */
                        MythicConfig mc = new MythicConfig(name, s1.getFile(), s1.getCustomConfig());

                        /**Fetching mythic mob to be loaded to spawner */
                        List<String> mmStrList = mc.getStringList("MythicMobs");
                        List<MythicMob> mobList = new ArrayList<MythicMob>();
                        for(String mmStr : mmStrList) {
                            MythicMob mm = SwordCraftOnline.getPluginInstance().getMobManager().getMMList().get(mmStr);
                            if(mmStr == null) {
                                SwordCraftOnline.logInfo("[Spawner Manager] Error loading file. MythicMob yml value null in: " + name + ".");
                                continue loaderLoop;
                            } else if(mm == null) {
                                SwordCraftOnline.logInfo("[Spawner Manager] Error loading file. MythicMob Type null for: " + mmStr);
                                continue loaderLoop;
                            }
                            mobList.add(mm);
                        }

                        Spawner s = new Spawner(name, mobList, mc);
                        /**Loading spawner to spawner registry by mythic mob file name */
                        this.spawnerList.put(name, s);
                    } catch(NullPointerException e) {
                        SwordCraftOnline.logInfo("[Spawner Manager] Error loading: " + name);
                    }
                }
        }
        SwordCraftOnline.logInfo("[Spawner Manager] Loading complete!");
    }

    /**
     * Handles mob spawning procedures
     */
    @Override
    public void run() {
        if(!this.isNightwave && !GameManager.isDay() && SwordCraftOnline.r.nextInt(199) == 1) {
            this.isNightwave = true; 
            Announcer.messageServer(ChatColor.BLACK + "[" + ChatColor.RED + "Nightwave" + ChatColor.BLACK + "]" + ChatColor.RED + " is approaching...", 0);
        } else if(GameManager.isDay() && this.isNightwave) {
            this.isNightwave = false;
        }
        
        /**Triggers a third of hostile active spawners in the map. */
        List<ActiveSpawner> hostiles = getLoadedHostileList();
        for(int i = 0; i < hostiles.size()/3; i++) {
            ActiveSpawner as = hostiles.get(SwordCraftOnline.r.nextInt(hostiles.size()));
            as.trigger();
        }

        /**Triggers a fourth of passive active spawners in the map */
        List<ActiveSpawner> passives = getLoadedPassiveList();
        for(int i = 0; i < passives.size()/4; i++) {
            ActiveSpawner as = passives.get(SwordCraftOnline.r.nextInt(passives.size()));
            as.trigger();
        }
    }

    /**Temporarily highlights all blocks */
    public void highlight() {
        if(this.isHighlighting) {
            SwordCraftOnline.logDebug("[Spawner Manager] Spawners are already highlighted!");
            return;
        }

        this.isHighlighting = true;
        for(ActiveSpawner as : getRegistryList()) {
            as.highlight();
        }
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), new Runnable() {
			@Override
			public void run() {
				setIsHighlighting(false);
			}
        }, 200);
    }

    /**@return spawner instance. Null if not in list */
    public Spawner getSpawner(String s) {
        if(s == null) { return null; }
        if(this.spawnerList.containsKey(s)){
            return this.spawnerList.get(s);
        }
        return null;
    }

    /**Used internally to register spawners. Not called as spawn method */
    public ActiveSpawner registerSpawner(ActiveSpawner as) {
        String name = as.getSpawner().getName();
        if(!this.spawnerRegistry.containsKey(name)) {
            this.spawnerRegistry.put(name, new ArrayList<>());
        }
        this.spawnerRegistry.get(name).add(as);

        /**Adding spawner to hostile and passive registrys */
        if(as.getSpawner().isPassive()) {
            if(!this.passiveRegistry.containsKey(name)) {
                this.passiveRegistry.put(name, new ArrayList<>());
            }
            this.passiveRegistry.get(name).add(as);
        } else {
            if(!this.hostileRegistry.containsKey(name)) {
                this.hostileRegistry.put(name, new ArrayList<>());
            }
            this.hostileRegistry.get(name).add(as);
        }

        updateRegistryList();
        return as;
    }

    /**Used internally to unregister spawner */
    public void unregisterSpawner(Location loc) {
        Location floored = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        for(ArrayList<ActiveSpawner> lis : this.spawnerRegistry.values()) {
            Iterator<ActiveSpawner> iter = lis.iterator();
            while(iter.hasNext()) {
                ActiveSpawner as = iter.next();
                if(as.getLocation().equals(floored)) {
                    iter.remove();
                    /**Removing from passive and hostile registry */
                    if(as.getSpawner().isPassive()) {
                        this.passiveRegistry.get(as.getSpawner().getName()).remove(as);
                    } else {
                        this.hostileRegistry.get(as.getSpawner().getName()).remove(as);
                    }
                    updateRegistryList();
                    save();
                    reload(true);
                    SwordCraftOnline.logInfo("[Spawner Manager] Successfully removed spawner.");
                    return;
                }
            }
        }
        SwordCraftOnline.logInfo("[Spawner Manager] Failed to remove spawner.");
    }

    /**Removes all spawners */
    public int removeAllSpawners() {
        int amount = 0;
        for(ArrayList<ActiveSpawner> lis : this.spawnerRegistry.values()) {
            Iterator<ActiveSpawner> iter = lis.iterator();
            while(iter.hasNext()) {
                iter.remove();
                amount++;
            }
        }
        this.hostileRegistry.clear();
        this.passiveRegistry.clear();

        updateRegistryList();
        return amount;
    }

    /**
     * Generates active spawner instance on location
     */
    public ActiveSpawner setSpawner(String spawnerName, Location loc, int level, boolean silent) {
        Spawner s = getSpawner(spawnerName);
        if(s != null) {
            ActiveSpawner as = new ActiveSpawner(s, level, loc);
            registerSpawner(as);
            as.activate(silent);
            return as;
        }
        return null;
    }

    public ActiveSpawner setSpawner(String spawnerName, Location loc, boolean silent) {
        return setSpawner(spawnerName, loc, 1, silent);
    }

    public ActiveSpawner setSpawner(String spawnerName, Location loc) {
        return setSpawner(spawnerName, loc, 1, false);
    }

    private void updateRegistryList() {
        this.registryList = getSpawnerRegistry().values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    /**
     * Iterates and finds loaded hostile spawners
     * @return List of loaded spawners
     */
    private List<ActiveSpawner> getLoadedHostileList() {
        List<ActiveSpawner> ret = new ArrayList<>();
        for(ActiveSpawner as : getHostileList()) {
            if(as.isLoaded()) { ret.add(as); }
        }
        return ret;
    }  

    /**
     * Iterates and finds loaded passive spawners
     * @return List of loaded spawners
     */
    private List<ActiveSpawner> getLoadedPassiveList() {
        List<ActiveSpawner> ret = new ArrayList<>();
        for(ActiveSpawner as : getPassiveList()) {
            if(as.isLoaded()) { ret.add(as); }
        }
        return ret;
    } 

    /**
     * Makes list of active spawners
     * @return String list.
     */
    public String getActiveSpawnerList() {
        String s = "Active Spawners: \n";
        for(ActiveSpawner as : getRegistryList()) {
            s += as.getSpawner().getName() + " " + String.valueOf(as.getLocation());
        }
        return s;
    }

    /**
     * Makes list of loaded spawners
     * @return String list
     */
    public String getSpawnerStringList() {
        String s = "Spawners: \n";
        for(Spawner sp : getSpawnerList().values()) {
            s += sp.getName() + "\n";
        } 
        return s;
    }

}