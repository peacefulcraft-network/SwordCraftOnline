package net.peacefulcraft.sco.gamehandle.regions;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.scheduler.BukkitTask;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;

public class FarmRegion extends Region implements Runnable {

    private BukkitTask farmTask;
        public void initializeTask() { this.farmTask = Bukkit.getServer().getScheduler().runTaskTimer(SwordCraftOnline.getPluginInstance(), this, 20, 20); } 
        public void cancelTask() { this.farmTask.cancel(); }

    private ArrayList<Location> farmland = new ArrayList<>();

    private Material crop;
        public Material getCropType() { return crop; }

    /**
     * Represetns 1/n amount of farmland we check to replant each cycle
     */
    private final int CROP_FRACTION = 3;

    public FarmRegion(String file, String internalName, MythicConfig mc) {
        super(file, internalName, mc);
        
        initializeTask();
        this.silentParentTransfer = true;

        initializeFarmland();

        this.crop = Material.valueOf(mc.getString("Crop", "Wheat").toUpperCase());
    }

    @Override
    public void run() {
        checkFarmland();
    }

    @Override
    public Boolean isFarm() {
        return true;
    }

    /**
     * Helper method
     * Initializes all farmland in region
     */
    private void initializeFarmland() {
        for(int x = x1; x <= x2; x++) {
            for(int z = z1; z <= z2; z++) {
                for(int y = 0; y <= 255; y++) {
                    Location loc = new Location(Bukkit.getServer().getWorld(world), x, y, z);
                    if(loc.getBlock().getType().equals(Material.FARMLAND)) {
                        this.farmland.add(loc);
                    }
                }
            }
        }
    }

    /**
     * Helper method
     * Checks 1/CROP_FRACTION farmland locations and either ticks or plants
     */
    private void checkFarmland() {
        for(int i = 0; i < this.farmland.size() / CROP_FRACTION; i++) {
            Location loc = farmland.get(SwordCraftOnline.r.nextInt(farmland.size())).clone();
            Location up = loc.clone().add(0, 1, 0);
            Material upType = up.getBlock().getType();

            if(upType.equals(Material.AIR) || upType.equals(Material.CAVE_AIR) || upType.equals(null)) {
                up.getBlock().setType(crop);

                Ageable age = (Ageable) up.getBlock().getBlockData();
                age.setAge(0);
            } else if(upType.equals(crop)) {
                Ageable age = (Ageable) up.getBlock().getBlockData();
                if(age.getAge() == age.getMaximumAge()) { continue; }
                age.setAge(age.getAge() + 1);
            }
        }
    }
    
}
