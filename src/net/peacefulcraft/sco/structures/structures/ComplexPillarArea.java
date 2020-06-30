package net.peacefulcraft.sco.structures.structures;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.structures.Structure;
import net.peacefulcraft.sco.utilities.WeightedList;

public class ComplexPillarArea extends Structure {

    /**Radius of area effected */
    private int radius;
        public int getRadius() { return this.radius; }
        public void setRadius(int i) { this.radius = i; }

    /**Minimum height of pillars */
    private int height;
        public int getHeight() { return this.height; }
        public void setHeight(int i) { this.height = i; }

    /**Maximum height of pillars */
    private int maxHeight;
        public int getMaxHeight() { return this.maxHeight; }
        public void setMaxHeight(int i) { this.maxHeight = i; }

    /**1/n of cylinder area where pillars are constructed */
    private int pillarDensity;
        public int getPillarDensity() { return this.pillarDensity; }
        public void setPillarDensity(int i) { this.pillarDensity = i; }

    public ComplexPillarArea(int radius, int height, int maxHeight, Material mat, boolean toCleanup, int cleanupTimer) {
        super(mat, toCleanup, cleanupTimer);

        this.radius = radius;
        this.height = height;
        this.maxHeight = maxHeight;
        this.pillarDensity = 4;
    }

    public ComplexPillarArea(int radius, int height, int maxHeight, Material mat) {
        this(radius, height, maxHeight, mat, false, 0);
    }

    public ComplexPillarArea(int radius, int height, int maxHeight, WeightedList<Material> matLis, boolean toCleanup, int cleanupTimer) {
        super(matLis, toCleanup, cleanupTimer);

        this.radius = radius;
        this.height = height;
        this.maxHeight = maxHeight;
        this.pillarDensity = 4;
    }

    public ComplexPillarArea(int radius, int height, int maxHeight, WeightedList<Material> matLis) {
        this(radius, height, maxHeight, matLis, false, 0);
    }

    @Override
    public void _construct() {
        Material material = getMaterial();
        WeightedList<Material> matList = getMatList();

        Cylinder cyl;
        if(material != null && matList == null) {
            cyl = new Cylinder(1, radius, false, material, toCleanup(), getCleanupTimer());
        } else if(material == null && matList != null) {
            cyl = new Cylinder(1, radius, false, matList, toCleanup(), getCleanupTimer());
        } else {
            SwordCraftOnline.logInfo("Attempted to construct ComplexPillarArea with invalid material attributes.");
            return;
        }
        
        cyl.targetLocation = getLocation();
        cyl.construct();

        /**Constructing pillars on 1/n area of the cylinder created */
        int density = (int) (3.14 * (radius * radius)) / pillarDensity;
        for(int i = 0; i <= density; i++) {
            int r = SwordCraftOnline.r.nextInt(radius);
            int x = 0;
            if(r != 0) { x += SwordCraftOnline.r.nextInt(r); }
            int z = (int)Math.sqrt(Math.pow(r, 2) - Math.pow(x,2));
            if(SwordCraftOnline.r.nextBoolean()) { x *= -1; }
            if(SwordCraftOnline.r.nextBoolean()) { z *= -1; }

            Location loc = getLocation().clone().add(x, 0, z);
            
            int addHeight = SwordCraftOnline.r.nextInt(maxHeight - height);
            Pillar pill;
            if(material != null && matList == null) {
                pill = new Pillar(height + addHeight, 1, material, toCleanup(), getCleanupTimer());
            } else if(material == null && matList != null) {
                pill = new Pillar(height + addHeight, 1, matList, toCleanup(), getCleanupTimer());
            } else {
                SwordCraftOnline.logInfo("Attempted to constructed ComplexPillarArea with invalid material attributes.");
                return;
            }

            pill.targetLocation = loc;
            pill.construct();
        }
    }
    
}