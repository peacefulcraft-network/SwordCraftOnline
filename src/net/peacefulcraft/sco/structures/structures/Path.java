package net.peacefulcraft.sco.structures.structures;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BlockIterator;
import org.bukkit.Location;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.structures.Structure;
import net.peacefulcraft.sco.swordskills.utilities.DirectionalUtil;
import net.peacefulcraft.sco.utilities.Pair;
import net.peacefulcraft.sco.utilities.WeightedList;

public class Path extends Structure {

    /**Width of path */
    private int width;

    /**Max length of path */
    private int length;

    public Path(int width, int length, Material mat, boolean toCleanup, int cleanupTimer) {
        super(mat, toCleanup, cleanupTimer);

        this.width = width;
        this.length = length;
    }

    public Path(int width, int length, Material mat) {
        this(width, length, mat, false, 0);
    }

    public Path(int width, int length, WeightedList<Material> lis, boolean toCleanup, int cleanupTimer) {
        super(lis, toCleanup, cleanupTimer);

        this.width = width;
        this.length = length;
    }

    public Path(int width, int length, WeightedList<Material> lis) {
        this(width, length, lis, false, 0);
    }

    @Override
    public void _construct() {
        LivingEntity target = getTargetEntity();
        if(target == null) {
            SwordCraftOnline.logInfo("Attempted to construct Path with no TargetEntity.");
            return;
        }

        int halfWidth = width/2;
        BlockFace sideFace = DirectionalUtil.getSideDirections(target);
        Location start = target.getLocation();
        Location end = target.getTargetBlock((Set<Material>) null, length).getLocation();

        BlockIterator iter = new BlockIterator(start, 0, (int)start.distance(end));
        while(iter.hasNext()) {
            Block block = iter.next();
            if(block.getType().equals(Material.AIR) || block.getType() == null) {
                Location bLoc = block.getLocation();
                for(int i = 1; i <= 25; i++) {
                    Location temp = bLoc.clone().add(0, -i, 0);
                    if(!temp.getBlock().getType().equals(Material.AIR) && !temp.getBlock().isPassable()) {
                        block = temp.getBlock();
                        break;
                    }
                }
            } 
            if(!block.getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                Location bLoc = block.getLocation();
                for(int i = 1; i<=5; i++) {
                    Block temp = bLoc.clone().add(0, i, 0).getBlock();
                    if(temp.getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                        block = temp;
                        break;
                    }
                }
            }

            for(int i = -halfWidth; i <=halfWidth; i++) {
                Block side = block.getRelative(sideFace, i);
                
                safeAddToCleanup(side.getType(), side.getLocation());

                side.setType(getType());
                blockCollisionEffects(side.getLocation());
            }
        }
    }
    
}