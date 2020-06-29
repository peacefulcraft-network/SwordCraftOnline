package net.peacefulcraft.sco.structures.structures;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.structures.Structure;
import net.peacefulcraft.sco.utilities.Pair;
import net.peacefulcraft.sco.utilities.WeightedList;

public class Cylinder extends Structure {
    
    /**Radius of cylinder */
    public int radius;

    /**Height of cylinder */
    public int height;

    public Cylinder(int height, int radius, Material mat, boolean toCleanup, int cleanupTimer) {
        this.height = height;
        this.radius = radius;
        this.material = mat;
        this.toCleanup = toCleanup;
        this.cleanupTimer = cleanupTimer;
        this.cleanupLis = new ArrayList<>();

        this.reverseCleanup = true;
    }

    public Cylinder(int height, int radius, Material mat) {
        this(height, radius, mat, false, 0);
    }

    public Cylinder(int height, int radius, WeightedList<Material> lis, boolean toCleanup, int cleanupTimer) {
        this.height = height;
        this.radius = radius;
        this.matList = lis;
        this.toCleanup = toCleanup;
        this.cleanupTimer = cleanupTimer;
        this.cleanupLis = new ArrayList<>();

        this.reverseCleanup = true;
    }

    public Cylinder(int height, int radius, WeightedList<Material> lis) {
        this(height, radius, lis, false, 0);
    }

    @Override
    public void _construct() {
        Location loc = getLocation();

        Block center = loc.getBlock();
        for(int currentHeight = 0; currentHeight<height; currentHeight++) {
            for(int x = -radius; x<radius; x++) {
                for(int z = -radius; z<radius; z++) {
                    Block current = center.getRelative(x, currentHeight, z);
                    if(toCleanup) {
                        cleanupLis.add(new Pair<Location,Material>(current.getLocation(), current.getType()));
                    }
                    current.setType(getMaterial());
                }
            }
        }
    }
}