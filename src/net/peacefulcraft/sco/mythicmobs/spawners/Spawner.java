package net.peacefulcraft.sco.mythicmobs.spawners;

import java.util.List;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;

public class Spawner {
    
    /**Spawners isntance config */
    private MythicConfig mc;

    /**Spawners mob to be spawned */
    //private MythicMob mm;
    //    public MythicMob getMythicMob() { return this.mm; }
    //    public String getMythicInternal() { return this.mm.getInternalName(); }

    /**List of mobs to be spawned */
    private List<MythicMob> mobList;

    /**Spawners file name */
    private String name;
        public String getName() { return this.name; }

    /**Determines how many mobs spawn. */
    private int size;
        public int getSize() { return this.size; }

    /**Determines size + upperBound of mobs. */
    private int sizeUpperBound;
        public int getSizeUpper() { return this.sizeUpperBound; }

    /**Determines size - lowerbound of mobs. */
    private int sizeLowerBound;
        public int getSizeLower() { return this.sizeLowerBound; }

    /**Determines level of mobs being spawned */
    private int mobLevel;
        public int getMobLevel() { return this.mobLevel; }

    /**Determines mob level + upperbound of mobs */
    private int levelUpperBound;
        public int getMobLevelUpper() { return this.levelUpperBound; }

    /**Determines mob level - lower bound of mobs */
    private int levelLowerBound;
        public int getMobLevelLower() { return this.levelLowerBound; }

    /**Determines radius of location that mobs are spawned in */
    private int radius;
        public int getRadius() { return this.radius; }

    /**Determines range player must be within to spawn. */
    private int range;
        public int getRange() { return this.range; }

    /**Determines spawners cooldown in seconds */
    private int cooldown;
        public int getCooldown() { return this.cooldown; }

    /**Determines how many mobs of similar type can be nearby */
    private int nearbyBounds;
        public int getNearbyBounds() { return this.nearbyBounds; }

    /**Determines if spawner can be converted under nightwave conditions */
    private boolean allowNightwave;
        public boolean allowNightwave() { return this.allowNightwave; }

    /**Determines what time of day spawner can be triggered. */
    private DayCycle dayCycle;
        public DayCycle getDayCycle() { return this.dayCycle; }

    /**Determines if spawner is spawning passive mobs. */
    private boolean isPassive;
        public boolean isPassive() { return this.isPassive; }

    /**Determines if spawner is capable of spawning herculean level monsters */
    private boolean canSpawnHerculean;
        public boolean canSpawnHerculean() { return this.canSpawnHerculean; }
        
    /**Determines spawners chance to spawn a herculean level monster */
    private int herculeanChance;
        public int getHerculeanChance() { return this.herculeanChance; }

    public Spawner(String name, List<MythicMob> mobList, MythicConfig config) {
        this.name = name;
        this.mobList = mobList;
        this.mc = config;

        this.size = mc.getInteger("Size", 5);
        this.sizeUpperBound = mc.getInteger("SizeUpperBound", 0);
        this.sizeLowerBound = mc.getInteger("SizeLowerBound", 2);

        this.mobLevel = mc.getInteger("MobLevel", 1);
        this.levelUpperBound = mc.getInteger("MobLevelUpperBound", 0);
        this.levelLowerBound = mc.getInteger("MobLevelLowerBound", 2);

        this.radius = mc.getInteger("Radius", 5);
        this.range = mc.getInteger("Range", 3);
        this.cooldown = mc.getInteger("Cooldown", 7);
        this.nearbyBounds = mc.getInteger("NearbyBounds", 4);

        this.allowNightwave = mc.getBoolean("AllowNightwave", true);

        // New attribute by spawner. Default false
        this.isPassive = mc.getBoolean("IsPassive", false);

        this.canSpawnHerculean = mc.getBoolean("CanSpawnHerculean", false);
        this.herculeanChance = mc.getInteger("HerculeanChance", 1000);

        try{
            String tempCycle = mc.getString("DayCycle", "ALL").toUpperCase();
            this.dayCycle = DayCycle.valueOf(tempCycle);
        } catch(IllegalArgumentException ex) {
            this.dayCycle = DayCycle.ALL;
        } catch(NullPointerException ex) {
            this.dayCycle = DayCycle.ALL;
        }
    }

    public MythicMob getMythicMob() {
        return this.mobList.get(SwordCraftOnline.r.nextInt(this.mobList.size()));
    }

}