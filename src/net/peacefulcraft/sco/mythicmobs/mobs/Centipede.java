package net.peacefulcraft.sco.mythicmobs.mobs;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.utilities.MathUtil;
import net.peacefulcraft.sco.utilities.TeleportUtil;

public class Centipede implements Runnable {

    private final double movementBounds = 1 * 1;

    private EntityType type;

    private LinkedList<Entity> list;
        public List<Entity> getList() { return Collections.unmodifiableList(this.list); }

    private int size;

    private String name;
        public String getName() { return this.name; }

    private Location headLocation;

    private BukkitTask moveTask;

    public Centipede(String name, int size) {
        this(name, size, 20);
    }

    public Centipede(String name, int size, int repetition) {
        this.list = new LinkedList<Entity>();
        this.size = size;
        this.name = name;

        this.headLocation = null;
        this.moveTask = Bukkit.getServer().getScheduler().runTaskTimer(SwordCraftOnline.getPluginInstance(), this, 5, repetition);
    }

    /**
     * Spawns centipede at given location with 1 block offset.
     */
    public void spawn(Location loc) {
        for(int i = 0; i < this.size; i++) {
            Entity temp = loc.getWorld().spawnEntity(loc.add(0 + i, 0, 0), EntityType.ZOMBIE);

            Zombie z = (Zombie)temp;
            z.setHealth(1);
            z.setBaby(true);
            z.setSilent(true);
            z.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 1));

            EntityEquipment ee = z.getEquipment();
            ee.setHelmet(new ItemStack(Material.BLACK_BED));
            
            this.list.add(temp);
            if(i != 0) { 
                ((Mob)temp).setAI(false);
                temp.setInvulnerable(true);
                temp.setMetadata("centipede", new FixedMetadataValue(SwordCraftOnline.getPluginInstance(), 0));
            } 
            if(i ==0) {
                this.headLocation = temp.getLocation();
                temp.setMetadata("centipede", new FixedMetadataValue(SwordCraftOnline.getPluginInstance(), 1));
            }
        }
        SwordCraftOnline.getPluginInstance().getMobManager().addCentipede(this.name, this);
    }

    /**
     * Mobs movement task logic.
     * Teleports mobs sequentially behind each other.
     */
    @Override
    public void run() {
        Location previous = this.headLocation;
        for(int i = 1; i < this.list.size(); i++) {

            Entity curr = this.list.get(i);
            Location currLoc = curr.getLocation();
            
            //Teleporting and setting head direction towards next location. Forward.
            previous.add(currLoc.getDirection().multiply(-0.6));
            previous.setY(previous.getBlockY());
            if(!previous.getBlock().isPassable()) {
                previous.add(0, 1, 0);
            }
            curr.teleport(previous);

            previous = currLoc.clone();
        }
        this.headLocation = this.list.getFirst().getLocation();
    }

    /**
     * Cancels mobs repeating movement task.
     */
    public void cancel() {
        this.moveTask.cancel();
    }

    public boolean setTarget(Entity e) {
        return setTarget((LivingEntity)e);
    }

    public boolean setTarget(Player p) {
        return setTarget((LivingEntity)p);
    }

    public boolean setTarget(LivingEntity e) {
        if(this.list.isEmpty()) { return false; }

        ((Mob)this.list.get(0)).setTarget(e);
        return true;
    }

    /**
     * Returns index of entity in centipede.
     * Returns -1 if mob not found
     */
    public int search(Entity e) {
        for(int i = 0; i < this.list.size(); i++) {
            if(this.list.get(i).equals(e)) {
                return i;
            } 
        }
        return -1;
    }

    /**
     * Handles damaging centipede.
     */
    public void segmentDamage(double damage) {
        Mob m = (Mob)this.list.getLast();
        if(m.getHealth() - damage <= 0) {
            m.setHealth(0);
            this.list.removeLast();
            this.size -= 1;
            return;
        }
        m.setHealth(m.getHealth() - damage);
    }

    /**
     * Kills centipede and ends movement tasks.
     * Does not remove centipede from mob registry.
     */
    public void kill() {
        this.cancel();

        ListIterator<Entity> iter = this.list.listIterator();
        while(iter.hasNext()) {
            Mob m = (Mob)iter.next();
            m.setHealth(0);
            iter.remove();
        }
    }
}