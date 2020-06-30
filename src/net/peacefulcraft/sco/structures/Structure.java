package net.peacefulcraft.sco.structures;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.utilities.Pair;
import net.peacefulcraft.sco.utilities.WeightedList;

public abstract class Structure {

    /** Block type structure will be made of */
    private Material material = null;

    public Material getMaterial() {
        return this.material;
    }

    public void setMaterial(Material m) {
        this.material = m;
    }

    /** Weighted map of block types for structure */
    private WeightedList<Material> matList = null;

    public WeightedList<Material> getMatList() {
        return this.matList.clone();
    }

    public void addToMatList(Material m, Double w) {
        this.matList.add(m, w);
    }

    /** Determines if structure is removed */
    private boolean toCleanup = true;

    public boolean toCleanup() {
        return this.toCleanup;
    }

    public void setToCleanup(boolean b) {
        this.toCleanup = b;
    }

    /** Given time in ticks before structure is removed */
    private int cleanupTimer = 200;

    public int getCleanupTimer() {
        return this.cleanupTimer;
    }

    public void setCleanupTimer(int i) {
        this.cleanupTimer = i;
    }

    /** If true, structure will use slow, time driven, clean up */
    private boolean advancedCleanup = false;

    public boolean isAdvancedCleanup() {
        return this.advancedCleanup;
    }

    public void setAdvancedCleanup(boolean b) {
        this.advancedCleanup = b;
    }

    /**
     * If true, structure will cleanup newest to oldest I.e. top down.
     */
    private boolean reverseCleanup = false;

    public boolean isReverseCleanup() {
        return this.reverseCleanup;
    }

    public void setReverseCleanup(boolean b) {
        this.reverseCleanup = b;
    }

    /** Contains locations in order to be reset */
    private ArrayList<Pair<Location, Material>> cleanupLis;

    public List<Pair<Location, Material>> getCleanupList() {
        return Collections.unmodifiableList(cleanupLis);
    }

    public void addCleanupList(Pair<Location, Material> p) {
        this.cleanupLis.add(p);
    }

    /** Time in seconds before structure is created after start */
    private int delay = 0;

    public int getDelay() {
        return this.delay;
    }

    public void setDelay(int i) {
        this.delay = i;
    }

    /** Targeted entity for structure */
    private LivingEntity targetEntity = null;

    public LivingEntity getTargetEntity() {
        return this.targetEntity;
    }

    public void setTargetEntity(LivingEntity e) {
        this.targetEntity = e;
    }

    /** Targeted location for structure */
    public Location targetLocation = null;

    public Location getTargetLocation() {
        return this.targetLocation;
    }

    public void setTargetLocation(Location l) {
        this.targetLocation = l;
    }

    /**
     * Extra effects called when entity is hit by replaced block Parameter must be
     * single living entity
     */
    private Method blockEffects = null;

    public Method getBlockEffects() {
        return this.blockEffects;
    }

    public void setBlockEffects(Method m) {
        this.blockEffects = m;
    }

    public Structure(Material mat, boolean toCleanup, int cleanupTimer) {
        this.material = mat;
        this.toCleanup = toCleanup;
        this.cleanupTimer = cleanupTimer;
        this.cleanupLis = new ArrayList<>();
    }

    public Structure(WeightedList<Material> matLis, boolean toCleanup, int cleanupTimer) {
        this.matList = matLis;
        this.toCleanup = toCleanup;
        this.cleanupTimer = cleanupTimer;
        this.cleanupLis = new ArrayList<>();
    }

    /** Validates strucutre is ready to be created */
    public boolean validate() {
        if ((targetEntity == null && targetLocation == null) || (targetEntity != null && targetLocation != null)) {
            return false;
        }
        if ((matList == null && material == null) || (matList != null && material != null)) {
            return false;
        }
        return true;
    }

    /** Fetches material type from weight list or single type */
    public Material getType() {
        if (matList == null) {
            return this.material;
        }
        return this.matList.getItem();
    }

    public void addMaterial(Material mat, Double weight) {
        this.matList.add(mat, weight);
    }

    /**
     * Resets all blocks created in structures Called by extending classes
     */
    public void cleanup() {
        if (!toCleanup) {
            return;
        }
        if (reverseCleanup) {
            Collections.reverse(cleanupLis);
        }

        if (advancedCleanup) {
            // Advanced cleanup. Iterates oldest to newest blocks replaced
            new BukkitRunnable() {
                @Override
                public void run() {
                    Pair<Location, Material> p = cleanupPop(0);
                    if (p == null) {
                        this.cancel();
                    }
                    p.getFirst().getBlock().setType(p.getSecond());
                }
            }.runTaskTimer(SwordCraftOnline.getPluginInstance(), cleanupTimer * 20, 10);
        } else {
            // Fast cleanup. Iterates through entire list without delay between blocks
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(),
                    new Runnable() {
                        public void run() {
                            if (reverseCleanup) {
                                Collections.reverse(cleanupLis);
                            }
                            for (Pair<Location, Material> p : cleanupLis) {
                                p.getFirst().getBlock().setType(p.getSecond());
                            }
                        }
                    }, cleanupTimer * 20);
        }
    }

    /**
     * Generic construct method that validates and calls task Called by extending
     * classes
     */
    public void construct() {
        if (!this.validate()) {
            SwordCraftOnline.logInfo("Attempted to construct Structure with invalid attributes.");
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), new Runnable() {
            @Override
            public void run() {
                _construct();
            }

        }, delay * 20);
        cleanup();
    }

    /** Helper function. Pops index from cleanupLis */
    private Pair<Location, Material> cleanupPop(int index) {
        if (cleanupLis.isEmpty()) {
            return null;
        }
        Pair<Location, Material> ret = cleanupLis.get(index);
        cleanupLis.remove(index);
        return ret;
    }

    /**
     * Fetches valid location of structure Called by extended classes
     */
    public Location getLocation() {
        if (targetEntity != null && targetLocation == null) {
            return targetEntity.getLocation();
        }
        if (targetLocation != null && targetEntity == null) {
            return targetLocation;
        }
        SwordCraftOnline.logInfo("Attempted to construct Structure with illegal location attributes.");
        return null;
    }

    /**
     * Used on block replace in construct. Checks if entity shares same location as
     * block and calls effects method on entities
     */
    public void blockCollisionEffects(Location loc) {
        if (this.blockEffects == null) {
            return;
        }
        for (Entity e : loc.getWorld().getNearbyEntities(loc, 2, 2, 2)) {
            if (!(e instanceof LivingEntity)) {
                continue;
            }
            Location temp = e.getLocation();
            int x = temp.getBlockX();
            int y = temp.getBlockY();
            int z = temp.getBlockZ();
            if (x == loc.getBlockX() && y == loc.getBlockY() && z == loc.getBlockZ()) {
                try {
                    Object[] parameters = new Object[1];
                    parameters[0] = (LivingEntity) e;
                    this.blockEffects.invoke(this, parameters);
                } catch (IllegalArgumentException ex) {
                    SwordCraftOnline.logInfo("Attempted to invoke 'blockEffects' method in Structure with illegal arguments.");
                    return;
                } catch (IllegalAccessException e1) {
                    SwordCraftOnline.logInfo("Attempted to invoke 'blockEffects' method in Structure with illegal access.");
                    return;
                } catch (InvocationTargetException e1) {
                    SwordCraftOnline.logInfo("Attempted to invoke 'blockEffects' method in Structure.");
                    return;
                }
            }
        }
    }

    /**Abstract method to contain each structures build task */
    public abstract void _construct();

}