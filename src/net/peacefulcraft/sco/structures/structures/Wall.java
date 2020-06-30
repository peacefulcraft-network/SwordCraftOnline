package net.peacefulcraft.sco.structures.structures;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.Location;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.structures.Structure;
import net.peacefulcraft.sco.swordskills.utilities.DirectionalUtil;
import net.peacefulcraft.sco.utilities.Pair;
import net.peacefulcraft.sco.utilities.WeightedList;

public class Wall extends Structure {

    /**Height of wall */
    private int height;

    /**Width of wall */
    private int width;

    /**Constructor for single material walls */
    public Wall(int height, int width, Material mat, boolean toCleanup, int cleanupTimer) {
        super(mat, toCleanup, cleanupTimer);

        this.setReverseCleanup(true);
        this.height = height;
        this.width = width;
    }

    public Wall(int height, int width, Material mat) {
        this(height, width, mat, false, 0);
    }

    /**Constructor for weighted list material walls */
    public Wall(int height, int width, WeightedList<Material> lis, boolean toCleanup, int cleanupTimer) {
        super(lis, toCleanup, cleanupTimer);

        this.setReverseCleanup(true);
        this.height = height;
        this.width = width;
    }

    public Wall(int height, int width, WeightedList<Material> lis) {
        this(height, width, lis, false, 0);
    }

    @Override
    public void _construct() {
        LivingEntity target = getTargetEntity();
        if(target == null) {
            SwordCraftOnline.logInfo("Attempted to construct Wall with no target entity.");
            return;
        }

        Block upBlock = target.getTargetBlock((Set<Material>) null, 4).getRelative(BlockFace.UP);
        BlockFace sideFace = DirectionalUtil.getSideDirections(target);
        int halfWidth = width/2;

        if(upBlock.getType() != null) {
            for(int y=0; y <= height; y++) {
				Block replace = upBlock.getRelative(BlockFace.UP, y);
                
                //Iterating through blocks half width to either side of center block
				for(int i=-halfWidth; i <= halfWidth; i++) {
					Block side = replace.getRelative(sideFace, i);
					if(i == 0) {
						continue;
					}
					if(side.getType().equals(Material.AIR) && side != null) {
                        if(this.toCleanup()) {
                            this.addCleanupList(new Pair<Location,Material>(side.getLocation(), side.getType()));
                        }
						side.setType(getType());
					} 
				}
				
				if(replace.getType().equals(Material.AIR) && replace != null) {
                    if(this.toCleanup()) {
                        this.addCleanupList(new Pair<Location,Material>(replace.getLocation(), replace.getType()));
                    }
                    replace.setType(getType());
				} else {
					continue; //Was break
				}
			}
        }
    }
    

}