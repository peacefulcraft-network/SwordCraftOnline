package net.peacefulcraft.sco.mythicmobs.mobs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.announcer.Announcer;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.adapters.BukkitAdapter;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractEntity;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractLocation;
import net.peacefulcraft.sco.mythicmobs.io.IOHandler;
import net.peacefulcraft.sco.mythicmobs.io.IOLoader;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.listeners.MobSpawnHandler;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntityType;

public class MobManager implements Runnable {
    private final SwordCraftOnline s;

    /**Stores MM: Key file name */
    private HashMap<String, MythicMob> mmList = new HashMap<>();
        public Map<String, MythicMob> getMMList() { return Collections.unmodifiableMap(this.mmList); }

    /**Stores MM by display name */
    private HashMap<String, MythicMob> mmDisplay = new HashMap<>();
        public Map<String, MythicMob> getMMDisplay() { return Collections.unmodifiableMap(this.mmDisplay); }

    /**Stores by default entity type */
    private Map<MythicEntityType, MythicMob> mmDefault = new HashMap<>();

    private HashMap<UUID, ActiveMob> mobRegistry = new HashMap<>();
        public Map<UUID, ActiveMob> getMobRegistry() { return Collections.unmodifiableMap(this.mobRegistry); }

    private HashMap<String, Centipede> centipedeList = new HashMap<String, Centipede>();
        public Map<String, Centipede> getCentipedeList() { return Collections.unmodifiableMap(this.centipedeList); }

    /**Registry of active herculean level mobs */
    private HashMap<UUID, ActiveMob> herculeanRegistry = new HashMap<>();
        public Map<UUID, ActiveMob> getHerculeanRegistry() { return Collections.unmodifiableMap(this.herculeanRegistry); }

    /**Registry of active pets */
    private HashMap<UUID, MythicPet> petRegistry = new HashMap<>();
        public Map<UUID, MythicPet> getPetRegistry() { return Collections.unmodifiableMap(this.petRegistry); }

    /** Main task logic for mob manager*/
    private BukkitTask mobTask;

    public MobManager(SwordCraftOnline s) {
        this.s = s;
        this.mobTask = Bukkit.getServer().getScheduler().runTaskTimer(SwordCraftOnline.getPluginInstance(), this, 0, 20);

        loadMobs();
    }

    /**Loads mobs from files*/
    public void loadMobs() {

        clearLists();

        IOLoader<SwordCraftOnline> defaultMobs = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "ExampleMobs.yml", "Mobs");
        defaultMobs  = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "ExampleMobs.yml", "Mobs");
        List<File> mobFiles = IOHandler.getAllFiles(defaultMobs.getFile().getParent());
        List<IOLoader<SwordCraftOnline>> mobLoaders = IOHandler.getSaveLoad(SwordCraftOnline.getPluginInstance(), mobFiles, "Mobs");

        for(IOLoader<SwordCraftOnline> sl : mobLoaders) {
            for(String name : sl.getCustomConfig().getConfigurationSection("").getKeys(false)) {
                try {
                    MythicConfig mc = new MythicConfig(name, sl.getFile(), sl.getCustomConfig());
                    String file = sl.getFile().getPath();

                    //Is mob a Vanilla replacement or removal?
                    if(file.contains("VanillaMobs.yml")) {
                        MobSpawnHandler.addVanillaMobs(name, mc);
                        continue;
                    }

                    if(MythicEntity.getMythicEntity(name) != null) {
                        MythicEntityType met = MythicEntityType.get(name);
                        MythicMob mm = new MythicMob(file, name, mc);
                        this.mmDefault.put(met, mm);
                        continue;
                    }
                    
                    String display = sl.getCustomConfig().getString(name + ".Display");
                    display = sl.getCustomConfig().getString(name + ".DisplayName", display);
                    
                    MythicMob mm = new MythicMob(file, name, mc);
                    this.mmList.put(name, mm);
                    
                    if(display != null) {
                        this.mmDisplay.put(display, mm);
                    }

                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        //Loading in persistent mobs stored in .yml
        IOLoader<SwordCraftOnline> file = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "PersistentMobConfig.yml", "Mobs");
        FileConfiguration config = file.getCustomConfig();
        for(String internal : mmList.keySet()) {
            List<Map<?,?>> lis = config.getMapList(internal);
            if(lis.isEmpty()) { continue; }
            for(Map<?,?> m : lis) {
                Location loc = new Location(
                    Bukkit.getWorld((String) m.get("world")),
                    Double.parseDouble(String.valueOf(m.get("x"))),
                    Double.parseDouble(String.valueOf(m.get("y"))),
                    Double.parseDouble(String.valueOf(m.get("z"))));

                ActiveMob am = spawnMob(internal, loc);
                if(am == null) {
                    SwordCraftOnline.logInfo("[Mob Manager] Error loading: " + internal + " from PersistentMobConfig.yml");
                    continue;
                }
            }
        }

        SwordCraftOnline.logInfo("[Mob Manager] Loading complete!");
    }

    /**
     * Call on plugin disable.
     * Safely despawns all mobs, including persistent.
     */
    public void save() {
        IOLoader<SwordCraftOnline> file = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "PersistentMobConfig.yml", "Mobs");
        FileConfiguration config = file.getCustomConfig();

        /**
         * Iterating over every active mob to save those with persistent locations
         * Since AM are not stored by Mob type we need to iterate over completely
         * then save to config.
         */
        HashMap<String, ArrayList<Object>> toSave = new HashMap<>();
        for(ActiveMob am : mobRegistry.values()) {
            MythicMob type = am.getType();
            if(type.isPersistent()) {
                HashMap<String, Object> loc = new HashMap<>();
                Location l = am.getPermLocation();
                loc.put("world", l.getWorld().getName());
                loc.put("x", l.getX());
                loc.put("y", l.getY());
                loc.put("z", l.getZ());

                String internal = type.getInternalName();
                if(toSave.get(internal) == null) {
                    toSave.put(internal, new ArrayList<>());
                }
                toSave.get(internal).add(loc);
            }
        }

        //Second iteration to save to config
        for(String internal : toSave.keySet()) {
            config.createSection(internal);
            config.set(internal, toSave.get(internal));
        }

        try{
            config.save(file.getFile());
        } catch(IOException ex) {
            SwordCraftOnline.logInfo("[Mob Manager] Failed to save PersistenMobConfig.yml");
            return;
        } 

        removeAllMobs(true);
        SwordCraftOnline.logInfo("[Mob Manager] PersistentMobConfig.yml saved!");
    }
  
    /**Clears lists but keeps persistent mobs */
    private void clearLists() {
        this.mmList.clear();
        this.mmDisplay.clear();
        this.mmDefault.clear();
        this.petRegistry.clear();
        HashMap<UUID, ActiveMob> temp = new HashMap<UUID, ActiveMob>();
        Iterator<ActiveMob> iterator = this.mobRegistry.values().iterator();
        while(iterator.hasNext()) {
            ActiveMob am = iterator.next();
            if(am.getType().isPersistent()) { 
                temp.put(am.getUUID(), am);
                continue; 
            }
        }
        this.mobRegistry.clear();
        this.mobRegistry = temp;

        MobSpawnHandler.clearVanillaMobs();
    }

    public void reload() {
        despawnAllMobs();
        loadMobs();
    }

    @Override
    //Mob managers herculean run task
    public void run() {
        HashMap<SCOPlayer, Integer> map = new HashMap<>();
        
        //Iterating over every registered herculean level AM
        for(ActiveMob am : herculeanRegistry.values()) {
            for(Entity e : am.getLivingEntity().getNearbyEntities(30, 15, 30)) {
                if(!(e instanceof Player)) { continue; }

                //If player is playing game
                SCOPlayer s = GameManager.findSCOPlayer((Player)e);
                if(s == null) { continue; }

                //Tracking how many Herculean mobs are nearby
                if(!map.containsKey(s)) {
                    map.put(s, 0);
                }
                map.put(s, map.get(s) + 1);
            }
        }

        //Iterating over players to message them
        for(SCOPlayer s : map.keySet()) {
            if(map.get(s) > 1) {
                Announcer.messagePlayer(s, "There are multiple Herculean level mobs nearby...", 60000);
            } else {
                Announcer.messagePlayer(s, "There is a Herculean level mob nearby...", 60000);
            }
        }
    }

    public MythicMob getMythicMob(String s) {
        if(s == null) { return null; }
        if(this.mmList.containsKey(s)) {
            return this.mmList.get(s);
        }
        return null;
    }

    public List<LivingEntity> getAllMythicEntries() {
        List<LivingEntity> list = new ArrayList<>();
        for(World w : Bukkit.getWorlds()) {
            for(LivingEntity e : w.getLivingEntities()) {
                if(this.mobRegistry.keySet().contains(e.getUniqueId())) {
                    list.add(e);
                }
            }
        }
        return list;
    }

    public ActiveMob registerActiveMob(AbstractEntity l, MythicMob mm, int level) {
        if(mmList.containsValue(mm)) {
            return getMythicMobInstance(l);
        }
        ActiveMob am = new ActiveMob(l.getUniqueId(), l, mm, level);
        this.mobRegistry.put(l.getUniqueId(), am);
        //Registering herculean mobs to own registry
        if(am.isHerculean()) {
            this.herculeanRegistry.put(l.getUniqueId(), am);
        }
        mm.applyMobVolatileOptions(am);
        return am;
    }

    public ActiveMob registerActiveMob(ActiveMob am) {
        this.mobRegistry.put(am.getUUID() , am);
        //Registering herculean mobs to own registry
        if(am.isHerculean()) {
            this.herculeanRegistry.put(am.getUUID(), am);
        }
        return am;
    }

    public ActiveMob registerActiveMob(AbstractEntity l) {
        if(this.mobRegistry.containsKey(l.getUniqueId())) {
            return getMythicMobInstance(l);
        }
        final MythicMob mm = determineMobType(l);
        if(mm == null) {
            return null;
        }
        final ActiveMob am = new ActiveMob(l.getUniqueId(), l, mm, getMythicMobLevel(mm, l));
        this.mobRegistry.put(l.getUniqueId(), am);
        if(am.isHerculean()) {
            this.herculeanRegistry.put(l.getUniqueId(), am);
        }
        mm.applyMobVolatileOptions(am);
        return am;
    }

    public void unregisterActiveMob(UUID u) {
        this.mobRegistry.remove(u);
        this.herculeanRegistry.remove(u);
    }

    /**
     * Unregisteres an active mob from registry
     * @param am ActiveMob to be unregistered
     */
    public void unregisterActiveMob(ActiveMob am) {
        this.mobRegistry.remove(am.getEntity().getUniqueId());
        this.herculeanRegistry.remove(am.getEntity().getUniqueId());
    }

    /**
     * Unregisteres a list of active mobs
     * @param lis List to be cleared
     */
    public void unregisterActiveMobs(List<ActiveMob> lis) {
        for(ActiveMob am : lis) {
            unregisterActiveMob(am);
        }
    }

    public ActiveMob getMythicMobInstance(Entity target) {
        return getMythicMobInstance(BukkitAdapter.adapt(target));
    }

    public ActiveMob getMythicMobInstance(AbstractEntity target) {
        ActiveMob am = this.mobRegistry.get(target.getUniqueId());
        if(am != null) { return am; }
        return registerActiveMob(target);
    }

    public static int getMythicMobLevel(MythicMob mm, AbstractEntity l) {
        int level = 1;
        if(mm.lvlModHealth > 0.0D) {
            double health = l.getMaxHealth() - mm.getHealth();
            level += (int)(health / mm.lvlModHealth);
        } else {
            return 1;
        }
        return level;
    }

    public static int getMythicMobLevel(LivingEntity l) {
        ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().getMythicMobInstance((Entity)l);
        if(am == null) {
            return 1;
        }
        return am.getLevel();
    }

    public ActiveMob spawnMob(String mobName, AbstractLocation loc, int level, List<SpawnFields> fields) {
        MythicMob mm = SwordCraftOnline.getPluginInstance().getMobManager().getMythicMob(mobName);
        if(mm != null) {
            //If mob is hostile and gamemode peaceful we abort spawn
            if(BukkitAdapter.adapt(loc).getWorld().getDifficulty().equals(Difficulty.PEACEFUL) 
                && !MythicEntity.isPassive(mm.getStrMobType())) {
                SwordCraftOnline.logInfo("[Mob Manager] World difficult is peaceful. Spawning abandoned.");
                return null;
            }
            //If we set herculean
            if(fields != null && fields.contains(SpawnFields.HERCULEAN)) {
                mm.setHerculean(true);
            }
            //If spawn reason is not BOSS_INTERFACE and mob is locked we cancel spawn
            if(fields != null && !fields.contains(SpawnFields.BOSS_INTERFACE) && mm.isBossLocked()) {
                SwordCraftOnline.logInfo("[Mob Manager] Attempted to spawn mob, " + mobName + ", outside of boss interface. Spawning abandoned.");
                return null;
            }

            ActiveMob am = mm.spawn(loc, level);
            //If mob is quest giver we set permenant location
            if(mm.canGiveQuests()) { am.setPermLocation(BukkitAdapter.adapt(loc)); }
            return am;
        }
        return null;
    }

    public ActiveMob spawnMob(String mobName, AbstractLocation loc) {
        return spawnMob(mobName, loc, 1, null);
    }

    public ActiveMob spawnMob(String mobName, Location loc, int level) {
        return spawnMob(mobName, BukkitAdapter.adapt(loc), level, null);
    }

    public ActiveMob spawnMob(String mobName, Location loc) {
        return spawnMob(mobName, BukkitAdapter.adapt(loc), 1, null);
    }

    /**Mob spawn method with single field */
    public ActiveMob spawnMob(String mobName, Location loc, int level, SpawnFields field) {
        List<SpawnFields> fields = new ArrayList<>();
        fields.add(field);
        return spawnMob(mobName, BukkitAdapter.adapt(loc), level, fields);
    }

    /**Mob spawn method with fields attribute */
    public ActiveMob spawnMob(String mobName, Location loc, int level, List<SpawnFields> fields) {
        return spawnMob(mobName, BukkitAdapter.adapt(loc), level, fields);
    }

    public MythicMob determineMobType(AbstractEntity l) {
        List<MetadataValue> list = l.getBukkitEntity().getMetadata("mobname");
        MythicMob mm = null;
        for(MetadataValue mv : list) {
            mm = this.mmList.get(mv);
            if(mm != null) { return mm; }
        }
        if(l.getCustomName() == null) { return null; }
        for(MythicMob MM : this.mmList.values()) {
            if(MM.getDisplayName() == null) { continue; }
            String search = MM.getDisplayName().toString();
            if(l.getCustomName().toString().matches(search) && MM.getHealth() <= l.getMaxHealth()) {
                l.setHealth(l.getMaxHealth());
                return MM;
            }
        }
        return null;
    }
    
    /**
     * @param ignorePersistent If true, all mobs including persistent types are removed
     * @return Number of mobs removed
     */
    public int removeAllMobs(boolean ignorePersistent) {
        int amount = 0;
        Iterator<ActiveMob> iterator = this.mobRegistry.values().iterator();
        while(iterator.hasNext()) {
            ActiveMob am = iterator.next();
            if(am.getType().isPersistent() && !ignorePersistent) { continue; }
            am.setDespawned();
            am.getEntity().remove();
            iterator.remove();
            amount++;
        }

        return amount;
    }

    public int despawnAllMobs() {
        int amount = 0;
        Iterator<ActiveMob> iterator = this.mobRegistry.values().iterator();
        while(iterator.hasNext()) {
            ActiveMob am = iterator.next();
            if((am.getType()).optionDespawn && !am.getType().isPersistent()) {
                am.setDespawnedSync();
                am.getEntity().remove();
                iterator.remove();
                amount++;
            }
        }
        return amount;
    }

    /**
     * @return List of all registered mobs that can give quests
     */
    public ArrayList<ActiveMob> getQuestGivers() {
        ArrayList<ActiveMob> ret = new ArrayList<>();
        for(ActiveMob am : mobRegistry.values()) {
            if(am.canGiveQuests()) {
                ret.add(am);
            }
        }
        return ret;
    }

     * Despawns a group of mobs
     * @param lis List of activemobs to be removed
     */
    public void despawnMobs(Collection<ActiveMob> lis) {
        for(ActiveMob am : lis) {
            if((am.getType()).optionDespawn && !am.getType().isPersistent()) {
                am.setDespawned();
                am.getEntity().remove();
            }
        }
    }

    public Collection<ActiveMob> getActiveMobs() {
        return this.mobRegistry.values();
    }

    /**
     * Registers mythic pet to mob mananger
     * @param mp Pet to be registered
     */
    public void registerPet(MythicPet mp) {
        this.petRegistry.put(mp.getActiveMob().getUUID(), mp);
    }

    /**Removes centipede from active centipede registry */
    public void removeCentiede(String name) {
        this.centipedeList.remove(name);
        SwordCraftOnline.logInfo("Removed " + name + " from Centipede Registry.");
    }

    /**Clears all centipede from centipede registry */
    public void removeAllCentipede() {
        Iterator<Centipede> iterator = this.centipedeList.values().iterator();
        while(iterator.hasNext()) {
            Centipede c = iterator.next();
            c.kill();
            SwordCraftOnline.logInfo("Removed " + c.getName() + " from Centipede Registry.");
            iterator.remove();
        }
    }

    /**Registers centipede */
    public void addCentipede(String name, Centipede c) { 
        this.centipedeList.put(name, c);
        SwordCraftOnline.logInfo("Registered " + name + " to Centipede Registry.");
    }

    /**Searches centipede registry for mob instance */
    public Centipede searchCentipede(Entity e) {
        for(Centipede c : getCentipedeList().values()) {
            for(Entity internal : c.getList()) {
                if(internal.equals(e)) {
                    return c;
                }
            }
        }
        return null;
    }

    /**
     * Searches MMDisplay for names. Consistent method to remove lvl name modifiers.
     * @param name Unmodified name of mob. Including Lvl modifiers.
     * @return MythicMob instance
     */
    public MythicMob searchMMDisplay(String name) {
        name = ChatColor.stripColor(name);
        name = StringUtils.substringBefore(name, " [");
        return getMMDisplay().get(name);
    }

    /**Actively refreshes all activemobs health bar displays */
    public void updateHealthBars() {
        Iterator<ActiveMob> itr = this.mobRegistry.values().iterator();
        while(itr.hasNext()) {
            ActiveMob am = itr.next();
            SwordCraftOnline.logDebug("Updating health bar of: " + am.getDisplayName() + " with health: " + String.valueOf(am.getHealth()));
            am.updateHealthBar();
        }
    }

    /**
     * Toggles mob run task on/off
     */
    public void toggleMobTask() {
        if(mobTask.isCancelled()) {
            this.mobTask = Bukkit.getServer().getScheduler().runTaskTimer(SwordCraftOnline.getPluginInstance(), this, 0, 20);
        } else {
            this.mobTask.cancel();
        }
    }

    /**
     * Enum constants used to assist in mob spawning variables
     */
    public enum SpawnFields {
        NONE, BOSS_INTERFACE, HERCULEAN;
    }
}