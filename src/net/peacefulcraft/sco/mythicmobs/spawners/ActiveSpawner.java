package net.peacefulcraft.sco.mythicmobs.spawners;

import java.util.Hashtable;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
import net.peacefulcraft.sco.mythicmobs.mobs.MobManager.SpawnFields;
import net.peacefulcraft.sco.utilities.TeleportUtil;

/**
 * Class for active spawner instances
 */
public class ActiveSpawner {

    private long cooldownTimer;

    private Spawner s;
        public Spawner getSpawner() { return this.s; }

    private int level;

    private Location loc;
        public Location getLocation() { return new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()); }

    private Boolean locked = false;
        public void setLocked(Boolean b) { this.locked = b; }

    private boolean isActivating;


    public ActiveSpawner(Spawner s, int level, Location loc) {
        this.s = s;
        this.level = level;
        this.loc = loc;

        this.cooldownTimer = 0;
    }

    /**Called when spawner is placed into world. */
    public void activate(boolean silent) {
        if(!silent) {
            Material old = getLocation().getBlock().getType();
            getLocation().getBlock().setType(Material.OBSIDIAN);
            this.isActivating = true;
            
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), new Runnable(){
                @Override
                public void run() {
                    getLocation().getBlock().setType(old);              
                }
            }, 100);
        }
    }

    /**Highlights block with glowstone temporarily */
    public void highlight() {
        if(this.isActivating) { return; }
        Material old = getLocation().getBlock().getType();
        getLocation().getBlock().setType(Material.GLOWSTONE);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), new Runnable(){
            @Override
            public void run() {
                getLocation().getBlock().setType(old);              
            }
        }, 200);
    }

    /**
     * Called when spawner is triggered to spawn 
     * Handles all logic related to blocking a spawner, spawner level, mob level, etc.
     */
    public void trigger() {
        //Checking if spawner is locked
        if(locked) { return; }
      
        /**Checking if spawners DayCycle matches server time of day */
        DayCycle tempCycle = this.s.getDayCycle();
        if(GameManager.isDay() && tempCycle.equals(DayCycle.NIGHT)) { return; }
        if(!GameManager.isDay() && tempCycle.equals(DayCycle.DAY)) { return; }

        /**Checking spawners cooldown */
        int offset = 0;
        if(this.level > 1) { offset = this.level * 10; }
        /**If world is in nightwave we reduce cooldown by 1 second*/
        if(SwordCraftOnline.getPluginInstance().getSpawnerManager().isNightwave() && this.s.allowNightwave()) { offset += 20; }
        if(this.cooldownTimer != 0 && getLocation().getWorld().getTime() - this.cooldownTimer < (s.getCooldown() * 20) - offset) { return; }
        this.cooldownTimer = getLocation().getWorld().getTime();

        boolean check = false;
        int similarCount = 0;
        /**Checking nearby entities for players. If none we dont spawn anything */
        for(Entity e : getLocation().getWorld().getNearbyEntities(getLocation(), this.s.getRange(), this.s.getRange(), this.s.getRange())) {
            /**If is player and player is in SCO and player is in adventure mode */
            if(e instanceof Player && GameManager.findSCOPlayer((Player)e) != null && ((Player)e).getGameMode().equals(GameMode.ADVENTURE)) {
                check = true;
                continue;
            }
            /**Comparing mob types of spawner and surrounding mobs */
            if(e.getType().toString().equalsIgnoreCase(s.getMythicMob().getStrMobType())) {
                similarCount += 1;
            }
        }
        /**If there is not a player or number of similar mobs nearby is too high */
        if(!check) { return; }
        if(similarCount > s.getNearbyBounds()) { return; }

        /**Determining how many spawn attempts we make */
        int iter = this.s.getSize() - SwordCraftOnline.r.nextInt(this.s.getSizeLower());
        if(s.getSizeUpper() != 0) { iter += SwordCraftOnline.r.nextInt(this.s.getSizeUpper()); }
        if(this.level > 1) { iter += this.level; }

        for(int i = 0; i <= iter; i++) {
            int r = SwordCraftOnline.r.nextInt(s.getRadius());
            int x = 0;
            if(r != 0) { x += SwordCraftOnline.r.nextInt(r); }
            int z = (int)Math.sqrt(Math.pow(r,2) - Math.pow(x,2));
            if(SwordCraftOnline.r.nextBoolean()) { x *= -1; }
            if(SwordCraftOnline.r.nextBoolean()) { z *= -1; }

            Location loc = getLocation().add(x, 0, z);
            if(!TeleportUtil.safeTeleport(loc)) { continue; }

            int level = this.s.getMobLevel();
            if(!SwordCraftOnline.getPluginInstance().getSpawnerManager().isNightwave()) {
                level -= SwordCraftOnline.r.nextInt(this.s.getMobLevelLower());
                if(this.s.getMobLevelUpper() != 0) { level += SwordCraftOnline.r.nextInt(this.s.getMobLevelUpper()); }
                if(this.level > 1) { level += this.level; }
            } else if(SwordCraftOnline.getPluginInstance().getSpawnerManager().isNightwave() && this.s.allowNightwave()) {
                level *= 2;
            }

            //If spawner is capable and we hit 1/n chance
            SpawnFields field = SpawnFields.NONE;
            if(this.s.canSpawnHerculean() && SwordCraftOnline.r.nextInt(this.s.getHerculeanChance()) == 1) {
                field = SpawnFields.HERCULEAN;
            }

            ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().spawnMob(this.s.getMythicInternal(), loc, level, field);
            if(am == null) {
                SwordCraftOnline.logInfo("[Spawner Manager] Error spawning mob in: " + this.s.getName());
            }
        }
    }

    /**@return True if chunk spawner is in is loaded */
    public boolean isLoaded() {
        Chunk chunk = getLocation().getChunk();
        if(chunk.isLoaded()) { return true; }
        return false;
    }

    /**Creates a config map to be stored in ./SpawnerConfig.yml */
    public Object getConfig() {
        Hashtable<String, Object> spawnerTable = new Hashtable<String, Object>();
        spawnerTable.put("world", this.loc.getWorld().getName());
        spawnerTable.put("x", "" + this.loc.getBlockX());
        spawnerTable.put("y", "" + this.loc.getBlockY());
        spawnerTable.put("z", "" + this.loc.getBlockZ());

        spawnerTable.put("level", this.level);

        return spawnerTable;
    }
}