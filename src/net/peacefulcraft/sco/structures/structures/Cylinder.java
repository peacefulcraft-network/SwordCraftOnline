package net.peacefulcraft.sco.structures.structures;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import net.peacefulcraft.sco.structures.Structure;
import net.peacefulcraft.sco.utilities.Pair;
import net.peacefulcraft.sco.utilities.WeightedList;

public class Cylinder extends Structure {
    
    /**Radius of cylinder */
    private int radius;

    /**Height of cylinder */
    private int height;

    /**If cylinder is hollow */
    private boolean hollow;

    public Cylinder(int height, int radius, boolean hollow, Material mat, boolean toCleanup, int cleanupTimer) {
        super(mat, toCleanup, cleanupTimer);

        this.setReverseCleanup(true);
        this.height = height;
        this.radius = radius;
        this.hollow = hollow;
    }

    public Cylinder(int height, int radius, boolean hollow, Material mat) {
        this(height, radius, hollow, mat, false, 0);
    }

    public Cylinder(int height, int radius, boolean hollow, WeightedList<Material> lis, boolean toCleanup, int cleanupTimer) {
        super(lis, toCleanup, cleanupTimer);

        this.setReverseCleanup(true);
        this.height = height;
        this.radius = radius;
        this.hollow = hollow;
    }

    public Cylinder(int height, int radius, boolean hollow, WeightedList<Material> lis) {
        this(height, radius, hollow, lis, false, 0);
    }

    @Override
    public void _construct() {
        Location loc = getLocation();
        if(loc == null) { return; }

        if(hollow) {
            Block center = loc.getBlock();
            for(int currentHeight = 0; currentHeight<height; currentHeight++) {
                for(int x = -radius; x<radius; x++) {
                    for(int z = -radius; z<radius; z++) {
                        Block current = center.getRelative(x, currentHeight, z);
                        if(this.toCleanup()) {
                            this.addCleanupList(new Pair<Location,Material>(current.getLocation(), current.getType()));
                        }
                        current.setType(getType());
                    }
                }
            }
        } else {
            int x;
            int y = loc.getBlockY();
            int z;
            for(int j = 0; j <= height; j++) {
                for (double i = 0.0; i < 360.0; i += 0.1) {
                    double angle = i * Math.PI / 180;
                    x = (int)(loc.getX() + radius * Math.cos(angle));
                    z = (int)(loc.getZ() + radius * Math.sin(angle));
                
                    loc.getWorld().getBlockAt(x, y, z).setType(this.getType());
                }
                y += 1;
            }
        }
    }
}