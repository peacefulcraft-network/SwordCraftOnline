package net.peacefulcraft.sco.structures.structures;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.structures.Structure;
import net.peacefulcraft.sco.swordskills.utilities.DirectionalUtil;
import net.peacefulcraft.sco.utilities.WeightedList;

public class WallWave extends Structure {

    /**Height of walls created */
    private int height;

    /**Width of walls created */
    private int width;

    /**Length of wall wave */
    private int length;

    /**Number of blocks between walls */
    private int wallGap = 0;
        public void setWallGap(int i) { this.wallGap = i; }

    public WallWave(int height, int width, int length, Material mat, boolean toCleanup, int cleanupTimer) {
        super(mat, toCleanup, cleanupTimer);
        
        this.height = height;
        this.width = width;
        this.length = length;
    }

    public WallWave(int height, int width, int length, Material mat) {
        this(height, width, length, mat, false, 0);
    }

    public WallWave(int height, int width, int length, WeightedList<Material> matLis, boolean toCleanup, int cleanupTimer) {
        super(matLis, toCleanup, cleanupTimer);

        this.height = height;
        this.width = width;
        this.length = length;
    }

    public WallWave(int height, int width, int length, WeightedList<Material> matLis) {
        this(height, width, length, matLis, false, 0);
    }

    @Override
    public void _construct() {
        LivingEntity target = getTargetEntity();
        if(target == null) {
            SwordCraftOnline.logInfo("Attempted to construct WallWave with no target entity.");
            return;
        }

        BlockFace sideFace = DirectionalUtil.getRelativeBlockFace(target, 90);
        Location start = target.getLocation().add(0, -1, 0);
        Location end = target.getTargetBlock((Set<Material>) null, length).getLocation();
    
        BlockIterator iter = new BlockIterator(start, 0, (int)start.distance(end));
        iter.next();

        //Predetermining which blocks are used for walls.
        ArrayList<Block> blockList = new ArrayList<Block>();
        int gapCheck = 0;
        while(iter.hasNext()) {
            if(wallGap != 0 && wallGap == gapCheck) {
                gapCheck = 0;
            } else {
                try {
                    Block next = iter.next();
                    Location conformed = conformToTerrain(next.getLocation());
                    blockList.add(conformed.getBlock());
                } catch(NoSuchElementException ex) {
                    return;
                }
            }
        }

        new BukkitRunnable(){
            int i = 0;
            @Override
            public void run() {
                try{
                    Block block = blockList.get(i);

                    Wall wall;
                    if(getMaterial() != null && getMatList() == null) {
                        wall = new Wall(height, width, getMaterial(), toCleanup(), getCleanupTimer());
                    } else if(getMaterial() == null && getMatList() != null) {
                        wall = new Wall(height, width, getMatList(), toCleanup(), getCleanupTimer());
                    } else {
                        SwordCraftOnline.logInfo("Attempted to construct WallWave with illegal material attributes.");
                        return;
                    }

                    wall.setWallWave(sideFace, block.getLocation());
                    wall.setAdvancedCleanup(true);

                    wall.construct();
                    i++;
                } catch(IndexOutOfBoundsException ex) {
                    this.cancel();
                }
            }
        }.runTaskTimer(SwordCraftOnline.getPluginInstance(), 0, 5);
    }
    
}