package net.peacefulcraft.sco.mythicmobs.mobs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.utilities.LocationUtil;

public class TentacleMob implements Runnable {
    private String name;
        public String getName() { return this.name; }

    private int tentacleNum;

    private ActiveMob head;

    private List<Centipede> tentacles;

    private BukkitTask moveTask;

    private int repetition = 2;

    private int tentacleOffset;

    public TentacleMob(String name, int tentacleNum) {
        this.name = name;
        this.tentacleNum = tentacleNum;
        this.tentacles = new ArrayList<>();

        this.moveTask = Bukkit.getServer().getScheduler().runTaskTimer(
            SwordCraftOnline.getPluginInstance(), 
            this, 
            5, 
            repetition);

        this.tentacleOffset = 360 / tentacleNum;
        if (tentacleNum % 2 == 0) { tentacleOffset += tentacleOffset / 2; }

        SwordCraftOnline.logDebug("[Tentacle Mob] Tentacle offset: " + tentacleOffset);
    }

    public void spawn(Location loc) {
        head = SwordCraftOnline.getPluginInstance().getMobManager().spawnMob("TentacleMob", loc);
        if(head == null) { 
            SwordCraftOnline.logDebug("[Tentacle Mob] Failed to load Gordo.");
            return;
        }

        SwordCraftOnline.logDebug("[Tentacle Mob] Spawning Gordo at: " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ", Yaw: " + loc.getYaw());

        Location copyLoc = loc.clone();
        List<Location> locs = LocationUtil.getCylinderLocations(loc, 3, tentacleOffset);      
        for(int i = 0; i < tentacleNum; i++) {
            
            Location currLoc = locs.get(i);

            currLoc = LocationUtil.faceLocation(currLoc, copyLoc);

            Centipede tentacle = new Centipede(name + "Tentacle" + i, true);
            tentacle.cancel();
            this.tentacles.add(tentacle);

            tentacle.spawn(currLoc);
            SwordCraftOnline.logDebug("[Tentacle Mob] Spawning " + tentacle.getName() + " at: " + currLoc.getX() + ", " + currLoc.getY() + ", " + currLoc.getZ()+ ", Yaw: " + currLoc.getYaw());
        }

        SwordCraftOnline.getPluginInstance().getMobManager().addTentacle(this.name, this);
    }

    @Override
    public void run() {
        // Assign and pass main mob location to tentacle heads
        // TODO: Move this to private function
        Location copyLoc = head.getBukkitLocation().clone();
        Location conformed = LocationUtil.conform(copyLoc);
        List<Location> locs = LocationUtil.getCylinderLocations(copyLoc, 3, tentacleOffset);

        for (int i = 0; i < tentacles.size(); i++) {

            Location currLoc = locs.get(i);

            currLoc = LocationUtil.faceLocation(
                currLoc, 
                conformed);

            Centipede curr = tentacles.get(i);
            curr._run_(currLoc);
        }       
    }

    public void cancel() {
        this.moveTask.cancel();
    }

    public void kill() {
        this.cancel();

        SwordCraftOnline.getPluginInstance().getMobManager().unregisterActiveMob(head);
        head.getLivingEntity().remove();
        for (Centipede tentacle : tentacles) {
            tentacle.kill();
        }
    }
}
