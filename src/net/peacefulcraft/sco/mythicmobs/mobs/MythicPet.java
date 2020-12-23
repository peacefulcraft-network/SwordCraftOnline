package net.peacefulcraft.sco.mythicmobs.mobs;

import org.apache.commons.lang.ObjectUtils.Null;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.adapters.BukkitAdapter;
import net.peacefulcraft.sco.mythicmobs.mobs.MobManager.SpawnFields;

/**
 * ActiveMob instance that acts as a players pet Logic involves protecting the
 * player and following, teleporting, etc.
 */
public class MythicPet implements Runnable {

    /**MythicMob loaded name to be spawned */
    private String mobName;

    /**ActiveMob instance of this pet */
    private ActiveMob am;
        public ActiveMob getActiveMob() { return am; }

    /**Owner of the pet */
    private SCOPlayer owner;

    /**Sets targets and AI logic */
    private BukkitTask petTask;

    /**Max distance before pet is teleported to player */
    private final int PET_DISTANCE = 400;

    /**
     * Constructs pet instance
     * @param mobName Name of mythic mob to be spawned
     * @param owner Owner of this pet
     */
    public MythicPet(String mobName, SCOPlayer owner) {
        this.mobName = mobName;
        this.owner = owner;
    }

    /**
     * Spawns pet at location at given level
     * @param loc Location to be spawned at
     * @param level Level of pet
     */
    public MythicPet spawn(Location loc, int level) {
        ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().spawnMob(this.mobName, loc, level);
        if(am == null) { return null; }

        // Register the pet to mob manager
        try{
            SwordCraftOnline.getPluginInstance().getMobManager().registerPet(this);
        }catch(NullPointerException ex) {
            SwordCraftOnline.logInfo(ex.getMessage());
        }

        this.am = am;
        this.petTask = Bukkit.getServer().getScheduler().runTaskTimer(SwordCraftOnline.getPluginInstance(), this, 0, 10);
        owner.setPet(this);

        am.setTarget(owner.getLivingEntity());

        return this;
    }

    @Override
    public void run() {
        // If mob is dead we kill task
        if(am.isDead()) {
            if(owner.getPet() == this) { owner.setPet(null); }

            this.petTask.cancel();
            return;
        }

        LivingEntity ole = owner.getLivingEntity();

        // If pet is farther than 20 blocks away
        if(BukkitAdapter.adapt(am.getLocation()).distanceSquared(owner.getLocation()) >= PET_DISTANCE) {
            am.setTarget(ole);
            am.getLivingEntity().teleport(owner.getLocation());
            return;
        }

        // If target is not owner and target is dead
        LivingEntity target = am.getTarget();
        if(target != null && target != ole && target.isDead()) {
            // If owner has living LCOD
            if(owner.getLastCauseOfDamage() != null) {
                am.setTarget(owner.getLastCauseOfDamage().getLivingEntity());
            } else {
                am.setTarget(ole);
            }
        }
    }
    
}
