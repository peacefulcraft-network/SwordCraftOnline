package net.peacefulcraft.sco.structures.structures;

import org.bukkit.Location;
import org.bukkit.Material;

import net.peacefulcraft.sco.structures.Structure;
import net.peacefulcraft.sco.utilities.WeightedList;

public class Pillar extends Structure {

    /**Side length of pillar*/
    private int length;

    /**Height of pillar */
    public int height;

    public Pillar(int height, int length, Material mat, boolean toCleanup, int cleanupTimer) {
        super(mat, toCleanup, cleanupTimer);

        this.setReverseCleanup(true);
        this.height = height;
        this.length = length;
    }

    public Pillar(int height, int length, Material mat) {
        this(height, length, mat, false, 0);
    }

    public Pillar(int height, int length, WeightedList<Material> matLis, boolean toCleanup, int cleanupTimer) {
        super(matLis, toCleanup, cleanupTimer);

        this.setReverseCleanup(true);
        this.height = height;
        this.length = length;
    }

    public Pillar(int height, int length, WeightedList<Material> matLis) {
        this(height, length, matLis, false, 0);
    }

    @Override
    public void _construct() {
        Location loc = getLocation();
        if(loc == null) { return; }

        int halfLength = length/2;

        for(int x = (int) loc.getX() - halfLength; x <= (int) loc.getX() + halfLength; x++) {
            for(int y = (int) loc.getY(); y <= (int) loc.getY() + height; y++) {
                 for(int z = (int) loc.getZ() - halfLength; z <= (int) loc.getZ() + halfLength; z++) {
                      loc.getWorld().getBlockAt(x, y, z).setType(this.getType());
                      blockCollisionEffects(loc);
                 }
            }
       }
    }
    
}