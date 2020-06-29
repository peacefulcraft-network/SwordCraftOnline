package net.peacefulcraft.sco.structures.structures;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import net.peacefulcraft.sco.structures.Structure;
import net.peacefulcraft.sco.utilities.WeightedList;

public class Circle extends Structure {

    /**Radius of cricle */
    public int radius;

    public Circle(int radius, Material mat, boolean toCleanup, int cleanupTimer) {
        this.radius = radius;
        this.material = mat;
        this.toCleanup = toCleanup;
        this.cleanupTimer = cleanupTimer;
        this.cleanupLis = new ArrayList<>();
    }

    public Circle(int radius, Material mat) {
        this(radius, mat, false, 0);
    }

    public Circle(int radius, WeightedList<Material> matLis, boolean toCleanup, int cleanupTimer) {
        this.radius = radius;
        this.matList = matLis;
        this.toCleanup = toCleanup;
        this.cleanupTimer = cleanupTimer;
        this.cleanupLis = new ArrayList<>();
    }

    public Circle(int radius, WeightedList<Material> matLis) {
        this(radius, matLis, false, 0);
    }

    @Override
    public void _construct() {
        Location loc = getLocation();
        if(loc == null) { return; }

        int x;
        int y = loc.getBlockY();
        int z;

        for (double i = 0.0; i < 360.0; i += 0.1) {
            double angle = i * Math.PI / 180;
            x = (int)(loc.getX() + radius * Math.cos(angle));
            z = (int)(loc.getZ() + radius * Math.sin(angle));
        
            loc.getWorld().getBlockAt(x, y, z).setType(material);
        }
    }
    
}