package net.peacefulcraft.sco.mythicmobs.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractEntity;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractLocation;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractPlayer;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.AbstractWorld;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.ServerInterface;
import net.peacefulcraft.sco.mythicmobs.adapters.abstracts.boss.AbstractBossBar;

public class BukkitServer implements ServerInterface {
    public List<AbstractWorld> getWorlds() {
        List<AbstractWorld> w1 = new ArrayList<>();
        for(World w : Bukkit.getWorlds()) {
            w1.add(new BukkitWorld(w));
        }
        return w1;
    }

    public AbstractPlayer getPlayer(UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);
        if(p == null) { return null; }
        return BukkitAdapter.adapt(p);
    }

    public AbstractPlayer getPlayer(String name) {
        Player p = Bukkit.getPlayer(name);
        if(p == null) { return null;}
        return BukkitAdapter.adapt(p);
    }

    public AbstractEntity getEntity(UUID uuid) {
        Entity e = Bukkit.getEntity(uuid);
        if(e == null) { return null; }
        return BukkitAdapter.adapt(e);
    }

    public AbstractWorld getWorld(String name) {
        World w = Bukkit.getWorld(name);
        if(w == null) { return null; }
        return new BukkitWorld(w);
    }

    public AbstractWorld getWorld(UUID uuid) {
        World world = Bukkit.getWorld(uuid);
        if(world == null) { return null; }
        return BukkitAdapter.adapt(world);
    }

    public List<AbstractPlayer> getOnlinePlayers() {
        List<AbstractPlayer> p = new ArrayList<>();
        for(Player pl : Bukkit.getServer().getOnlinePlayers()) {
            p.add(BukkitAdapter.adapt(pl));
        }
        return p;
    }

    public AbstractLocation newLocation(AbstractWorld w, double x, double y, double z) {
        Location l = new Location(BukkitAdapter.adapt(w), x, y, z);
        return BukkitAdapter.adapt(l);        
    }

    public AbstractBossBar createBossBar(String title, BarColor color, BarStyle style) {
        return new BukkitBossBar(title, color, style);
    }

    public boolean isValidBiome(Object o) {
        return false;
    }
}