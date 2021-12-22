package net.peacefulcraft.sco.gamehandle.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

/**
 * SCO wide explosion manager for
 * reliable damage and death tracking measures
 */
public class ExplosionManager implements Listener {
    
    private HashMap<SCOExplosion, Long> explosionRegistry;
        protected Map<SCOExplosion, Long> getExplosionRegistry() { return Collections.unmodifiableMap(explosionRegistry); }

    /**
     * Explosion manager construct
     * Initialize all lists
     */
    public ExplosionManager() {
        explosionRegistry = new HashMap<>();
    }

    /**
     * Registers and creates an SCOExplosion with given owner
     * at given location
     * @param owner ModifierUser summoning the explosion
     * @param power Power of explosion
     * @param loc Location of the explosion
     */
    public void registerExplosion(ModifierUser owner, int power, Location loc) {
        SCOExplosion e = new SCOExplosion(owner, power, loc);
        explosionRegistry.put(e, System.currentTimeMillis());

        e.createExplosion();
    }

    @EventHandler
    public void damageListener(EntityDamageEvent e) {
        ModifierUser mu = ModifierUser.getModifierUser(e.getEntity());
        if (mu == null) { return; }

        if (e.getCause().equals(DamageCause.BLOCK_EXPLOSION) || e.getCause().equals(DamageCause.ENTITY_EXPLOSION)) {
            final Long currentTime = System.currentTimeMillis();
            final Location currentLoc = mu.getLivingEntity().getLocation();

            SwordCraftOnline.getPluginInstance().getServer().getScheduler().runTaskAsynchronously(SwordCraftOnline.getPluginInstance(), () -> {
                Long max = Long.MAX_VALUE;
                SCOExplosion maxExplosion = null;

                for (Entry<SCOExplosion, Long> entry : getExplosionRegistry().entrySet()) {
                    // Checking if explosion registry time + 1 second
                    // If explosion occurred more than a second ago we don't care.
                    if (entry.getValue() + 1000 < currentTime) { continue; }

                    Double dSq = entry.getKey().getLocation().distanceSquared(currentLoc);
                    
                    // If player was damaged by an explosion outside this instances
                    // Radius we don't care.
                    if (dSq > (entry.getKey().getRadiusSquared())) { continue; }

                    // If difference of current and entry is less than
                    // our tracked max we want to attribute the explosion
                    // to the lesser.
                    if (currentTime - entry.getValue() < max) {
                        max = (currentTime - entry.getValue());
                        maxExplosion = entry.getKey();
                    }
                }

                // TODO: Change log damage event call
                // TODO: Change this damage listener to be a method called in modifieruser damage listener
                //maxExplosion.logDamageEvent(ev);
            });
        }
    }

    /**
     * Explosion wrapper
     */
    private class SCOExplosion {

        private ModifierUser owner;
        private int power;
        private Location loc;

        private double radius;

        public SCOExplosion(ModifierUser owner, int power, Location loc) {
            this.owner = owner;
            this.power = power;
            this.loc = loc;

            // Radius equation pulled from https://minecraft.fandom.com/wiki/Explosion#Model_of_block_destruction
            this.radius = Math.floor(1.3 * ((double) power)/ 0.225) * 0.3;
        }

        /**
         * Fetches radius of this explosion
         * @return Calculated radius as double
         */
        public double getRadiusSquared() {
            return this.radius * this.radius;
        }

        public Location getLocation() {
            return this.loc;
        }

        /**
         * Creates an explosion with given parameters
         */
        public void createExplosion() {
            loc.getWorld().createExplosion(loc, power, false, false);
        }

        /**
         * Logs an attributed explosion damage event to the owner
         * @param ev Damage event
         */
        public void logDamageEvent(EntityDamageEvent ev) {

        }
    }

}
