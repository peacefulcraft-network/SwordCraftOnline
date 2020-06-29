package net.peacefulcraft.sco.structures.structures;

import java.util.ArrayList;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Location;

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
        this.height = height;
        this.width = width;
        this.material = mat;
        this.toCleanup = toCleanup;
        this.cleanupTimer = cleanupTimer;
        this.cleanupLis = new ArrayList<>();

        this.reverseCleanup = true;
    }

    public Wall(int height, int width, Material mat) {
        this(height, width, mat, false, 0);
    }

    /**Constructor for weighted list material walls */
    public Wall(int height, int width, WeightedList<Material> lis, boolean toCleanup, int cleanupTimer) {
        this.height = height;
        this.width = width;
        this.matList = lis;
        this.toCleanup = toCleanup;
        this.cleanupTimer = cleanupTimer;
        this.cleanupLis = new ArrayList<>();

        this.reverseCleanup = true;
    }

    public Wall(int height, int width, WeightedList<Material> lis) {
        this(height, width, lis, false, 0);
    }

    @Override
    public void _construct() {
        Block upBlock = targetEntity.getTargetBlock((Set<Material>) null, 4).getRelative(BlockFace.UP);
        int halfWidth = width/2;

        if(upBlock.getType() != null) {
            for(int y=0; y <= height; y++) {
				Block replace = upBlock.getRelative(BlockFace.UP, y);
                
                //Iterating through blocks half width to either side of center block
				for(int i=-halfWidth; i <= halfWidth; i++) {
					Block side = replace.getRelative(DirectionalUtil.getSideDirections(targetEntity), i);
					if(i == 0) {
						continue;
					}
					if(side.getType().equals(Material.AIR) && side != null) {
                        if(toCleanup) {
                            cleanupLis.add(new Pair<Location,Material>(side.getLocation(), side.getType()));
                        }
						side.setType(getMaterial());
					} 
				}
				
				if(replace.getType().equals(Material.AIR) && replace != null) {
                    if(toCleanup) {
                        cleanupLis.add(new Pair<Location,Material>(replace.getLocation(), replace.getType()));
                    }
                    replace.setType(getMaterial());
				} else {
					continue; //Was break
				}
			}
        }
    }
    

}