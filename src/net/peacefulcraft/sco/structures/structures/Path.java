package net.peacefulcraft.sco.structures.structures;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.BlockIterator;
import org.bukkit.Location;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.structures.Structure;
import net.peacefulcraft.sco.swordskills.utilities.DirectionalUtil;
import net.peacefulcraft.sco.utilities.WeightedList;

public class Path extends Structure {

    /**Width of path */
    private int width;

    /**Max length of path */
    private int length;

    public Path(int width, int length, Material mat, boolean toCleanup, int cleanupTimer) {
        this.width = width;
        this.length = length;
        this.material = mat;
        this.toCleanup = toCleanup;
        this.cleanupTimer = cleanupTimer;
        this.cleanupMap = new HashMap<Location, Material>();
    }

    public Path(int width, int length, Material mat) {
        this(width, length, mat, false, 0);
    }

    public Path(int width, int length, WeightedList<Material> lis, boolean toCleanup, int cleanupTimer) {
        this.width = width;
        this.length = length;
        this.matList = lis;
        this.toCleanup = toCleanup;
        this.cleanupTimer = cleanupTimer;
        this.cleanupMap = new HashMap<Location, Material>();
    }

    public Path(int width, int length, WeightedList<Material> lis) {
        this(width, length, lis, false, 0);
    }

    @Override
    public void _construct() {
        if(targetEntity == null) {
            SwordCraftOnline.logInfo("Attempted to construct Path with no TargetEntity.");
            return;
        }

        int halfWidth = width/2;
        Location start = targetEntity.getLocation().add(0, -1, 0);
        Location end = targetEntity.getTargetBlock((Set<Material>) null, length).getLocation();

        BlockIterator iter = new BlockIterator(start, 0, (int)start.distance(end));
        while(iter.hasNext()) {
            Block block = iter.next();

            for(int i = -halfWidth; i <=halfWidth; i++) {
                Block side = block.getRelative(DirectionalUtil.getSideDirections(targetEntity), i);
                
                if(toCleanup) {
                    cleanupMap.put(side.getLocation(), side.getType());
                }
                side.setType(getMaterial());
            }
        }
    }
    
}