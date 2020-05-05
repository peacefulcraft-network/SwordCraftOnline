package net.peacefulcraft.sco.mythicmobs.spawners;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
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

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.mythicmobs.io.IOHandler;
import net.peacefulcraft.sco.mythicmobs.io.IOLoader;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;

public class SpawnerManager implements Runnable {

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

    /**Stores values for spawner registry in one list */
    private ArrayList<ActiveSpawner> registryList = new ArrayList<>();
        public List<ActiveSpawner> getRegistryList() { return Collections.unmodifiableList(this.registryList); }

    public SpawnerManager() {
        
        reloadSequence();

        /**Setting to run spawner task once a second */
        this.spawnerTask = Bukkit.getServer().getScheduler().runTaskTimer(SwordCraftOnline.getPluginInstance(), this, 20, 30);
    }

    /**Called on plugin reload or individual reload. */
    public void reloadSequence() {
        loadSpawners();
        reload();
    }

    public void reload() {
        IOLoader<SwordCraftOnline> config = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "SpawnerConfig.yml");
        this.config = config.getCustomConfig();

        for(String name : SwordCraftOnline.getPluginInstance().getMobManager().getMMList().keySet()) {
            List<Map<?, ?>> lis = this.config.getMapList(name);
            if(lis.isEmpty()) { continue; }
            for(Map<?,?> m : lis) {
                Location loc = new Location(
                    Bukkit.getWorld((String) m.get("world")), 
                    Double.parseDouble(String.valueOf(m.get("x"))), 
                    Double.parseDouble(String.valueOf(m.get("y"))),
                    Double.parseDouble(String.valueOf(m.get("z"))));
                int level = (int) m.get("level");
                String sName = String.valueOf(m.get("name"));
                ActiveSpawner as = setSpawner(sName, loc, level);
                if(as == null) {
                    SwordCraftOnline.logDebug("[Spawner Manager] Error loading: " + sName + " Invalid spawner name");
                    continue;
                }
            }
        }
    }

    public void loadSpawners() {
        
        this.spawnerList.clear();

        IOLoader<SwordCraftOnline> defaultSpawner = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "ExampleSpawner.yml", "Spawners");
        defaultSpawner = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "ExampleSpawner.yml", "Spawners");
        List<File> spawnerFiles = IOHandler.getAllFiles(defaultSpawner.getFile().getParent());
        List<IOLoader<SwordCraftOnline>> spawnerLoaders = IOHandler.getSaveLoad(SwordCraftOnline.getPluginInstance(), spawnerFiles, "Spawners");

        for(IOLoader<SwordCraftOnline> s1 : spawnerLoaders) {
            for(String name : s1.getCustomConfig().getConfigurationSection("").getKeys(false)) {
                try {
                    /**Fetching mythic config and file name */
                    MythicConfig mc = new MythicConfig(name, s1.getFile(), s1.getCustomConfig());

                    /**Fetching mythic mob to be loaded to spawner */
                    String mmStr = mc.getString("MythicMob");
                    MythicMob mm = SwordCraftOnline.getPluginInstance().getMobManager().getMMList().get(mmStr);
                    if(mmStr == null) {
                        SwordCraftOnline.logInfo("[Spawner Manager] Error loading file. MythicMob yml value null.");
                        continue;
                    } else if(mm == null) {
                        SwordCraftOnline.logInfo("[Spawner Manager] Error loading file. MythicMob Type null for: " + mmStr);
                        continue;
                    } 

                    Spawner s = new Spawner(name, mm, mc);
                    /**Loading spawner to spawner registry by mythic mob file name */
                    this.spawnerList.put(name, s);
                    SwordCraftOnline.logDebug("[Spawner Manager] Loaded: " + name + ", " + mmStr);
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
        /**Triggers a third of active spawners in the map. */
        for(int i = 0; i < this.registryList.size()/3; i++) {
            ActiveSpawner as = this.registryList.get(SwordCraftOnline.r.nextInt(this.registryList.size()));
            as.trigger();
        }
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
        if(!this.spawnerRegistry.containsKey(as.getSpawner().getMythicInternal())) {
            this.spawnerRegistry.put(as.getSpawner().getMythicInternal(), new ArrayList<>());
        }
        this.spawnerRegistry.get(as.getSpawner().getMythicInternal()).add(as);
        this.registryList.add(as);
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
                    this.registryList.remove(as);
                    return;
                }
            }
        }
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
        this.registryList.clear();
        return amount;
    }

    /**
     * Generates active spawner instance on location
     */
    public ActiveSpawner setSpawner(String spawnerName, Location loc, int level) {
        Spawner s = getSpawner(spawnerName);
        if(s != null) {
            ActiveSpawner as = new ActiveSpawner(s, level, loc);
            registerSpawner(as);
            as.activate();
            return as;
        }
        return null;
    }

    public ActiveSpawner setSpawner(String spawnerName, Location loc) {
        return setSpawner(spawnerName, loc, 1);
    }

    /**
     * Makes list of active spawners
     * @return String list.
     */
    public String getActiveSpawnerList() {
        String s = "Active Spawners: \n";
        for(ArrayList<ActiveSpawner> list : this.spawnerRegistry.values()) {
            for(ActiveSpawner as : list) {
                s += as.getSpawner().getName() + " " + String.valueOf(as.getLocation()) + "\n";
            }
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