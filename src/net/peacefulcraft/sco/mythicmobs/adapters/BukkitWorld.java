package net.peacefulcraft.sco.mythicmobs.adapters;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractEntity;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractLocation;

import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractPlayer;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractWorld;

public class BukkitWorld implements AbstractWorld {

    private transient WeakReference<World> worldRef;
    
    private UUID uuid;
    
    private String name;
    
    public BukkitWorld() {}
    
    public BukkitWorld(World w) {
      this.worldRef = new WeakReference<>(w);
      this.uuid = w.getUID();
      this.name = w.getName();
    }
    
    public BukkitWorld(String world) {
      World w = Bukkit.getWorld(world);
      this.worldRef = new WeakReference<>(w);
      if (this.worldRef != null && this.worldRef.get() != null)
        this.uuid = w.getUID(); 
      this.name = world;
    }
    
    public World getBukkitWorld() {
      if (this.worldRef.get() == null)
        if (Bukkit.getWorld(this.name) != null) {
          this.worldRef = new WeakReference<>(Bukkit.getWorld(this.name));
        } else {
          return null;
        }  
      return this.worldRef.get();
    }
    
    public boolean isLoaded() {
      return (getBukkitWorld() != null);
    }
    
    public List<AbstractEntity> getLivingEntities() {
      List<AbstractEntity> wl = new ArrayList<>();
      if (getBukkitWorld() == null)
        return wl; 
      for (LivingEntity e : getBukkitWorld().getLivingEntities())
        wl.add(new BukkitEntity((Entity)e)); 
      return wl;
    }
    
    public String getName() {
      if (getBukkitWorld() == null)
        return this.name; 
      return getBukkitWorld().getName();
    }
    
    public UUID getUniqueId() {
      if (getBukkitWorld() == null)
        return this.uuid; 
      return getBukkitWorld().getUID();
    }
    
    public boolean equals(Object other) {
      if (other == null)
        return false; 
      if (other instanceof BukkitWorld) {
        if (((BukkitWorld)other).getBukkitWorld() == null)
          return false; 
        return ((BukkitWorld)other).getBukkitWorld().equals(getBukkitWorld());
      } 
      if (other instanceof AbstractWorld)
        return ((AbstractWorld)other).getName().equals(getName()); 
      return false;
    }
    
    public String toString() {
      return getName();
    }
    
    public int hashCode() {
      if (getBukkitWorld() == null)
        return -1; 
      return getBukkitWorld().hashCode();
    }
    
    public int getMaxY() {
      return getBukkitWorld().getMaxHeight() - 1;
    }
    
    public void createExplosion(AbstractLocation l, float f) {
      if (getBukkitWorld() == null)
        return; 
      getBukkitWorld().createExplosion(BukkitAdapter.adapt(l), f);
    }
    
    public void createExplosion(AbstractLocation l, float yield, boolean fire, boolean blockdamage) {
      if (getBukkitWorld() == null)
        return; 
      getBukkitWorld().createExplosion(l.getX(), l.getY(), l.getZ(), yield, fire, blockdamage);
    }
    
    public List<AbstractPlayer> getPlayers() {
      List<AbstractPlayer> pl = new ArrayList<>();
      for (Player p : getBukkitWorld().getPlayers())
        pl.add(BukkitAdapter.adapt(p)); 
      return pl;
    }
    
    public void setStorm(boolean b) {
      getBukkitWorld().setStorm(b);
    }
    
    public void setThundering(boolean b) {
      getBukkitWorld().setThundering(b);
    }
    
    public void setWeatherDuration(int duration) {
      getBukkitWorld().setWeatherDuration(duration);
    }
    
    private static final Map<Integer, Effect> effects = new HashMap<>();
    
    static {
      for (Effect effect : Effect.values())
        effects.put(Integer.valueOf(effect.getId()), effect); 
    }
    
    public boolean playEffect(AbstractLocation location, int type) {
      return playEffect(location, type, 0);
    }
    
    public boolean playEffect(AbstractLocation location, int type, int data) {
      World world = getBukkitWorld();
      Effect effect = effects.get(Integer.valueOf(type));
      if (effect == null)
        return false; 
      world.playEffect(BukkitAdapter.adapt(location), effect, data);
      return true;
    }
    
    public int getBlockLightLevel(AbstractLocation l) {
      return getBukkitWorld().getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ()).getLightLevel();
    }
    
    public AbstractLocation getSpawnLocation() {
      if (getBukkitWorld() == null)
        return null; 
      return BukkitAdapter.adapt(getBukkitWorld().getSpawnLocation());
    }
    
    public boolean isChunkLoaded(int x, int z) {
      if (getBukkitWorld() == null)
        return false; 
      return getBukkitWorld().isChunkLoaded(x, z);
    }
    
    public boolean isLocationLoaded(AbstractLocation abstractLocation) {
      if (getBukkitWorld() == null)
        return false; 
      Location loc = BukkitAdapter.adapt(abstractLocation);
      int x = loc.getBlockX() >> 4;
      int z = loc.getBlockZ() >> 4;
      return getBukkitWorld().isChunkLoaded(x, z);
    }
    
    public List<AbstractPlayer> getPlayersNearLocation(AbstractLocation location, int radius) {
      List<AbstractPlayer> pl = new ArrayList<>();
      Location l = BukkitAdapter.adapt(location);
      for (Player p : getBukkitWorld().getPlayers()) {
        if (p.getLocation().distanceSquared(l) <= Math.pow(radius, 2.0D));
        pl.add(BukkitAdapter.adapt(p));
      } 
      return pl;
    }
    
    /*
    public AbstractBiome getLocationBiome(AbstractLocation abstractLocation) {
      Location l = BukkitAdapter.adapt(abstractLocation);
      return BukkitAdapter.adapt(l.getBlock().getBiome());
    }
    */
    
    public long getFullTime() {
      if (getBukkitWorld() == null)
        return 0L; 
      return getBukkitWorld().getFullTime();
    }

}