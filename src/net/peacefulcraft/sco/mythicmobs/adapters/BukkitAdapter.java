package net.peacefulcraft.sco.mythicmobs.adapters;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractBiome;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractEntity;

import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractItemStack;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractLocation;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractPlayer;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractVector;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractWorld;

public final class BukkitAdapter {
    HashMap<Entity, AbstractEntity> cachedEntities = new HashMap<>();
   
    public static AbstractEntity adapt(Entity entity) {
        if (entity == null) { return null; }
        return new BukkitEntity(entity);
    }
    
    public static Entity adapt(AbstractEntity entity) {
        if (entity == null) { return null; }
        return entity.getBukkitEntity();
    }
    
    public static AbstractPlayer adapt(Player player) {
        if (player == null) { return null; } 
        return new BukkitPlayer(player);
    }
    
    public static ItemStack adapt(AbstractItemStack item) {
        if (item == null) { return null; }
        return ((BukkitItemStack)item).build();
    }
    
    public static AbstractItemStack adapt(ItemStack item) {
        if (item == null) { return null; }
        return new BukkitItemStack(item);
    }
    
    public static Player adapt(AbstractPlayer player) {
        if (player == null) { return null; }
        return (Player)player.getBukkitEntity();
    }
    
    public static AbstractWorld adapt(World world) {
        return new BukkitWorld(world);
    }
    
    public static World adapt(AbstractWorld world) {
        if (world instanceof BukkitWorld) { return ((BukkitWorld)world).getBukkitWorld(); }

        World match = Bukkit.getServer().getWorld(world.getName());
        if (match != null) { return match; }
  
        throw new IllegalArgumentException("Can't find a Bukkit world for " + world);
    }
    
    public static AbstractLocation adapt(Location location) {
        if (location == null) { return null; }
        return new AbstractLocation(adapt(location.getWorld()), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }
    
    public static Location adapt(AbstractLocation location) {
        return new Location(
            adapt(location.getWorld()), location
            .getX(), location.getY(), location.getZ(), location
            .getYaw(), location
            .getPitch());
    }
    
    public static AbstractVector adapt(Vector vector) {
        if (vector == null) { return null; }
        return new AbstractVector(vector.getX(), vector.getY(), vector.getZ());
    }
    
    public static Vector adapt(AbstractVector vector) {
        return new Vector(vector.getX(), vector.getY(), vector.getZ());
    }

    public static AbstractBiome adapt(Biome b) {
        if (b == null)
        return null; 
        return new AbstractBiome(b.toString());
    }
}