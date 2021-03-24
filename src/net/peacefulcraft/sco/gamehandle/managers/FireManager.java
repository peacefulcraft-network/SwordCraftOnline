package net.peacefulcraft.sco.gamehandle.managers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitTask;

import net.peacefulcraft.sco.SwordCraftOnline;

public class FireManager implements Runnable {
    
    private BukkitTask fireTask;
        public void toggleFireTask() {
            if(fireTask.isCancelled()) {
                this.fireTask = Bukkit.getServer().getScheduler().runTaskTimer(
                    SwordCraftOnline.getPluginInstance(), 
                    this, 
                    20, 
                    40);
            } else {
                this.fireTask.cancel();
            }
        }

    private HashMap<Long, Location> fireMap = new HashMap<>();

    public FireManager() {
        this.fireTask = Bukkit.getServer().getScheduler().runTaskTimer(
            SwordCraftOnline.getPluginInstance(), 
            this, 
            20, 
            40);
    }

    /**
     * Register fire block location
     * @param cooldownDelay of fire being extinguished
     * @param loc of fire
     * @return true if successful, false otherwise.
     */
    public boolean registerFire(long cooldownDelay, Location loc) {
        if(!loc.getBlock().getType().equals(Material.AIR) ||
            !loc.getBlock().getType().equals(Material.CAVE_AIR)) { return false; }
        loc.getBlock().setType(Material.FIRE);
        
        long cooldownEnd = System.currentTimeMillis() + cooldownDelay;
        fireMap.put(cooldownEnd, loc);
        SwordCraftOnline.logDebug("[Fire Manager] Fire placed at: " + loc.toString());
        return true;
    }

    @Override
    public void run() {
        Iterator<Entry<Long, Location>> iter = fireMap.entrySet().iterator();
        while(iter.hasNext()) {
            // Remove if time expires
            // or block is no longer fire
            Entry<Long, Location> entry = iter.next();
            if(entry.getKey() <= System.currentTimeMillis()) {
                entry.getValue().getBlock().setType(Material.AIR);
                iter.remove();
            } else if(!entry.getValue().getBlock().getType().equals(Material.FIRE)) {
                iter.remove();
            }
        }
    }
}
