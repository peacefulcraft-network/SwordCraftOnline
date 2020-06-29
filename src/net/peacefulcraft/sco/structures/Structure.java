package net.peacefulcraft.sco.structures;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;

import net.peacefulcraft.sco.SwordCraftOnline;
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

    /**Time in ticks before structure is created after start */
    public int delay = 0;

    /**Targeted entity for structure */
    public LivingEntity targetEntity = null;

    /** Targeted location for structure*/
    public Location targetLocation = null;

    /**Contains locations to be reset */
    public HashMap<Location, Material> cleanupMap;

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

    /**Resets all blocks created in structure1 */
    public void cleanup() {
        if(!toCleanup) { return; }
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), new Runnable() {
            public void run() {
                for(Location loc : cleanupMap.keySet()) {
                    loc.getBlock().setType(cleanupMap.get(loc));
                }
            }
        }, cleanupTimer * 20);
    }

    /**Generic construct method that validates and calls task */
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

    /**Abstract method to contain each structures build task */
    public abstract void _construct();

}