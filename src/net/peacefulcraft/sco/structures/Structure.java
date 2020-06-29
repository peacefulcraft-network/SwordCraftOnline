package net.peacefulcraft.sco.structures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.utilities.Pair;
import net.peacefulcraft.sco.utilities.WeightedList;

public abstract class Structure  {

    /**Block type structure will be made of */
    public Material material = null;

    /**Weighted map of block types for structure */
    public WeightedList<Material> matList = null;

    /**Determines if structure is removed */
    public boolean toCleanup = true;

    /**Given time in ticks before structure is removed */
    public int cleanupTimer = 200;

    /**If true, structure will use slow, time driven, clean up */
    public boolean advancedCleanup = false;

    /**
     * If true, structure will cleanup newest to oldest
     * I.e. top down.
     */
    public boolean reverseCleanup = false;

    /**Contains locations in order to be reset */
    public ArrayList<Pair<Location, Material>> cleanupLis;

    /**Time in ticks before structure is created after start */
    public int delay = 0;

    /**Targeted entity for structure */
    public LivingEntity targetEntity = null;

    /** Targeted location for structure*/
    public Location targetLocation = null;

    public Structure() {}

    public LivingEntity getTargetEntity() {
        return targetEntity == null ? null : targetEntity;
    }

    public Location getTargetLocation() {
        return targetLocation == null ? null : targetLocation;
    }

    /**Validates strucutre is ready to be created */
    public boolean validate() {
        if((targetEntity == null && targetLocation == null) || (targetEntity != null && targetLocation != null)) {
            return false;
        }
        if((matList == null && material == null) || (matList != null && material != null)) {
            return false;
        }
        return true;
    }

    /**Fetches material type from weight list or single type*/
    public Material getMaterial() {
        if(matList == null) {
            return this.material;
        }
        return this.matList.getItem();
    }

    public void addMaterial(Material mat, Double weight) {
        this.matList.add(mat, weight);
    }

    /**
     * Resets all blocks created in structures 
     * Called by extending classes
     */
    public void cleanup() {
        if(!toCleanup) { return; }
        if(reverseCleanup) { Collections.reverse(cleanupLis); }

        if(advancedCleanup) {
            //Advanced cleanup. Iterates oldest to newest blocks replaced
            new BukkitRunnable(){
                @Override
                public void run() {
                    Pair<Location, Material> p = cleanupPop(0);
                    if(p == null) { this.cancel(); }
                    p.getFirst().getBlock().setType(p.getSecond());
                }
            }.runTaskTimer(SwordCraftOnline.getPluginInstance(), cleanupTimer * 20, 10);
        } else {
            //Fast cleanup. Iterates through entire list without delay between blocks
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), new Runnable() {
                public void run() {
                    if(reverseCleanup) { Collections.reverse(cleanupLis); }
                    for(Pair<Location, Material> p : cleanupLis) {
                        p.getFirst().getBlock().setType(p.getSecond());
                    }
                }
            }, cleanupTimer * 20);
        }
    }

    /**
     * Generic construct method that validates and calls task
     * Called by extending classes
     */
    public void construct() {
        if(!this.validate()) {
            SwordCraftOnline.logInfo("Attempted to construct Structure with invalid attributes.");
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), new Runnable() {
            @Override
            public void run() {
                _construct();
            }

        }, delay);
        cleanup();
    }

    /**Helper function. Pops index from cleanupLis */
    private Pair<Location, Material> cleanupPop(int index) {
        if(cleanupLis.isEmpty()) { return null; }
        Pair<Location, Material> ret = cleanupLis.get(index);
        cleanupLis.remove(index);
        return ret;
    }

    /**
     * Fetches valid location of structure
     * Called by extended classes
     */
    public Location getLocation() {
        if(targetEntity != null && targetLocation == null) {
            return targetEntity.getLocation();
        }
        if(targetLocation != null && targetEntity == null) {
            return targetLocation;
        }
        SwordCraftOnline.logInfo("Attempted to construct Structure with illegal location attributes.");
        return null;
    }

    /**Abstract method to contain each structures build task */
    public abstract void _construct();

}