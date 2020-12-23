package net.peacefulcraft.sco.mythicmobs.mobs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;

import net.peacefulcraft.sco.SwordCraftOnline;

/**
 * ActiveMob instance that does not attack certain living entity instances
 */
public class Minion implements Runnable {
    
    /**
     * Entities not to be targeted
     */
    private List<LivingEntity> notTargets;

    /**Optional: target mob */
    private LivingEntity target;

    /** MythicMob name to be spawned */
    private String mobName;

    /**This minions active mobs instance */
    private ActiveMob am;

    /**Task to set targets */
    private BukkitTask targetTask;

    /**
     * Constructs minion instance
     * @param mobName Name of mythic mob to be spawned
     * @param notTarget Protected entity
     */
    public Minion(String mobName, LivingEntity notTarget) {
        notTargets = new ArrayList<>();
        notTargets.add(notTarget);
        this.target = null;

        this.mobName = mobName;
    }

    /**
     * Constructs minion instance
     * @param mobName Name of mythic mob to be spawned
     * @param notTargets Protected entities
     */
    public Minion(String mobName, List<LivingEntity> notTargets) {
        this.notTargets = notTargets;
        this.mobName = mobName;
        this.target = null;
    }

    /**
     * Spawns minion in area around given location
     * @param loc Location
     * @param level
     */
    public void spawn(Location loc, int level) {
        ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().spawnMob(mobName, loc, level);
        if(am == null) { return; }

        this.am = am;
        this.targetTask = Bukkit.getServer().getScheduler().runTaskTimer(SwordCraftOnline.getPluginInstance(), this, 0, 20);
    }

    @Override
    public void run() {
        // If mob is dead
        if(am.isDead()) {
            this.targetTask.cancel();
            return;
        }

        // Not targeting safe mob, we don't care.
        if(!notTargets.contains(am.getTarget())) { return; }

        LivingEntity living = am.getLivingEntity();

        List<LivingEntity> temp = new ArrayList<>();
        for(Entity e : living.getNearbyEntities(15, 15, 15)) {
            if(e instanceof LivingEntity) {
                temp.add((LivingEntity)e);
            }
        }

        // No nearby living entities
        if(temp.size() == 0) {
            am.setTarget(null);
        }

        if(this.target != null) { 
            // Setting designated target
            am.setTarget(this.target); 
        } else {
            // Picking random nearby entity
            am.setTarget(temp.get(SwordCraftOnline.r.nextInt(temp.size())));
        }
    }

    /**
     * Adds a mob to the not target list
     * @param e LivingEntity to be protected
     */
    public void addSafeEntity(LivingEntity e) {
        this.notTargets.add(e);
    }

    /**
     * Removes a mob from the not target list
     * @param e LivingEntity to be protected
     */
    public void removeSafeEntity(LivingEntity e) {
        this.notTargets.remove(e);
    }

    /**
     * Sets target mob for minion
     * @param e LivingEntity to target
     */
    public void setTarget(LivingEntity e) {
        this.target = e;
    }

    /**
     * Clears target mob
     */
    public void clearTarget() {
        this.target = null;
    }

}