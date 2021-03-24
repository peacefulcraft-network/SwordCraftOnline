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

    private HashMap<Location, Long> fireMap = new HashMap<>();

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
        if(!loc.getBlock().getType().equals(Material.AIR)) { return false; }
        loc.getBlock().setType(Material.FIRE);
        
        long cooldownEnd = System.currentTimeMillis() + cooldownDelay + SwordCraftOnline.r.nextInt(3000);
        fireMap.put(loc, cooldownEnd);
        //SwordCraftOnline.logDebug("[Fire Manager] Fire placed at: " + loc.toString());
        return true;
    }

    @Override
    public void run() {
        Iterator<Entry<Location, Long>> iter = fireMap.entrySet().iterator();
        while(iter.hasNext()) {
            // Remove if time expires
            // or block is no longer fire
            Entry<Location, Long> entry = iter.next();
            if(entry.getValue() <= System.currentTimeMillis()) {
                entry.getKey().getBlock().setType(Material.AIR);
                SwordCraftOnline.logDebug("[Fire Manager] Cooldown. Fire Removed.");
                iter.remove();
            } else if(!entry.getKey().getBlock().getType().equals(Material.FIRE)) {
                SwordCraftOnline.logDebug("[Fire Manager] Defaulted. Fire Removed.");
                iter.remove();
            }
        }
    }
}
