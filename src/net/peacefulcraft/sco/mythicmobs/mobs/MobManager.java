package net.peacefulcraft.sco.mythicmobs.mobs;

import java.io.File;
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
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.MetadataValue;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.log.Banners;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.mythicmobs.adapters.BukkitAdapter;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractEntity;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractLocation;
import net.peacefulcraft.sco.mythicmobs.io.IOHandler;
import net.peacefulcraft.sco.mythicmobs.io.IOLoader;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.listeners.MobSpawnHandler;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntity;
import net.peacefulcraft.sco.mythicmobs.mobs.entities.MythicEntityType;

public class MobManager {
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

    

    public MobManager(SwordCraftOnline s) {
        this.s = s;
        loadMobs();
    }

    public void loadMobs() {

        this.mmList.clear();
        this.mmDisplay.clear();
        this.mmDefault.clear();
        this.mobRegistry.clear();
        MobSpawnHandler.clearVanillaMobs();

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
        SwordCraftOnline.logInfo("[Mob Manager] Loading complete!");
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
        mm.applyMobVolatileOptions(am);
        return am;
    }

    public ActiveMob registerActiveMob(ActiveMob am) {
        this.mobRegistry.put(am.getUUID() , am);
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
        mm.applyMobVolatileOptions(am);
        return am;
    }

    public void unregisterActiveMob(UUID u) {
        this.mobRegistry.remove(u);
    }

    public void unregisterActiveMob(ActiveMob am) {
        this.mobRegistry.remove(am.getEntity().getUniqueId());
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

    public ActiveMob spawnMob(String mobName, AbstractLocation loc, int level) {
        if(BukkitAdapter.adapt(loc).getWorld().getDifficulty().equals(Difficulty.PEACEFUL)) {
            SwordCraftOnline.logInfo("[Mob Manager] World difficult is peaceful. Spawning abdandoned.");
            return null;
        }
        MythicMob mm = SwordCraftOnline.getPluginInstance().getMobManager().getMythicMob(mobName);
        if(mm != null) {
            return mm.spawn(loc, level);
        }
        return null;
    }

    public ActiveMob spawnMob(String mobName, AbstractLocation loc) {
        return spawnMob(mobName, loc, 1);
    }

    public ActiveMob spawnMob(String mobName, Location loc, int level) {
        return spawnMob(mobName, BukkitAdapter.adapt(loc), level);
    }

    public ActiveMob spawnMob(String mobName, Location loc) {
        return spawnMob(mobName, BukkitAdapter.adapt(loc), 1);
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
    
    public int removeAllMobs() {
        int amount = 0;
        Iterator<ActiveMob> iterator = this.mobRegistry.values().iterator();
        while(iterator.hasNext()) {
            ActiveMob am = iterator.next();
            if(am.getType().isPersistent()) { continue; }
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

    public Collection<ActiveMob> getActiveMobs() {
        return this.mobRegistry.values();
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
}