package net.peacefulcraft.sco.structures.structures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.BlockIterator;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.structures.Structure;
import net.peacefulcraft.sco.utilities.TeleportUtil;
import net.peacefulcraft.sco.utilities.WeightedList;

public class HollowCuboid extends Structure {

    /**Side length of cuboid */
    private int size;

    /**Height of cuboid */
    private int height;

    //Corners of region created
    private int x1;

    private int x2;

    private int z1;

    private int z2;

    /**Determines if cuboid has top layer */
    private boolean hasLid = true;
        /**
         * Sets lid attribute variable. This removes the top
         * layer of the cuboid
         */
        public void setHasLid(boolean b) { this.hasLid = b; }
        public boolean hasLid() { return this.hasLid; }

    /**
     * List of safe edge locations around cuboid
     * Used primarily in duel system
     */
    private List<Location> safeEdgeLocations;

    public HollowCuboid(int size, int height, Material mat, boolean toCleanup, int cleanupTimer) {
        super(mat, toCleanup, cleanupTimer);
        
        this.size = size;
        this.height = height-1;
    }

    public HollowCuboid(int size, int height, Material mat) {
        this(size, height, mat, false, 0);
    }

    public HollowCuboid(int size, int height, WeightedList<Material> lis, boolean toCleanup, int cleanupTimer) {
        super(lis, toCleanup, cleanupTimer);

        this.size = size;
        this.height = height-1;
    }

    public HollowCuboid(int size, int height, WeightedList<Material> lis){
        this(size, height, lis, false, 0);
    }

    @Override
    public void _construct() {
        Location loc = getLocation();

        int halfSize = size/2;

        //Fetching corners of cuboid created
        this.x1 = loc.getBlockX() - halfSize;
        this.x2 = loc.getBlockX() + halfSize;
        this.z1 = loc.getBlockZ() - halfSize;
        this.z2 = loc.getBlockZ() + halfSize;

        //Going from half size to center cuboid around location
        for(int x = -halfSize; x < halfSize; x++) {
            for(int z = -halfSize; z < halfSize; z++) {
                for(int y = 0; y < height; y++) {
                    // If height is 1 below and we don't want lid.
                    if(y == height-1 && !this.hasLid) {
                        continue;
                    }
                    if(x == 0 || y == 0 || z == 0 || x == size-1 || y == height-1 || z == size-1) {
                        Location temp = loc.clone().add(x, y, z);
                        safeAddToCleanup(temp.getBlock().getType(), temp);

                        temp.getBlock().setType(getType());
                        blockCollisionEffects(temp);
                    }
                }
            }
        }

        getEdgeLocations();
    }

    /**
     * Checks if location is within the cuboid
     * @param loc Location to be checked
     * @return True if location is inside cuboid
     */
    public boolean isInCuboid(Location loc) {
        int locX = loc.getBlockX();
        int locZ = loc.getBlockZ();

        //(x1,y1) -> (x2,y1)
        boolean test1 = _isInRegion(x1, x2, z1, z1, locX, locZ);
        //(x2,y1) -> (x2,y2)
        boolean test2 = _isInRegion(x2, x2, z1, z2, locX, locZ);
        //(x2,y2) -> (x1,y2)
        boolean test3 = _isInRegion(x2, x1, z2, z2, locX, locZ);
        //(x1,y2) -> (x1,y1)
        boolean test4 = _isInRegion(x1, x1, z2, z1, locX, locZ);

        if(test1 && test2 && test3 && test4) { return true; }
        return false;
    }
    
    /**Helper function used to check sides of rectangle bounds */
    private boolean _isInRegion(int x1, int x2, int z1, int z2, int pointX, int pointY) {
        double A = -(z2 - z1);
        double B = x2 - x1;
        double C = -(A * x1 + B * z1);

        double D = (A * pointX) + (B * pointY) + C;
        if(D > 0) { return false; }
        return true;
    }

    /**
     * Scans area around cuboid for safe location
     * Saves safe locations in attribute list
     */
    private void getEdgeLocations() {
        /**
         * Original region coords extended by 1
         */
        int edgeX1 = x1 - 1;
        int edgeX2 = x2 + 1;
        int edgeZ1 = z1 - 1;
        int edgeZ2 = z2 + 2;
        
        this.safeEdgeLocations = new ArrayList<>();
        //(x1,z1) -> (x2,z1)
        safeEdgeLocations.addAll(findSafeOnEdge(edgeX1, edgeX2, edgeZ1, edgeZ2));
        //(x2,z1) -> (x2,z2)
        safeEdgeLocations.addAll(findSafeOnEdge(edgeX2, edgeX2, edgeZ1, edgeZ2));
        //(x2,z2) -> (x1,z2)
        safeEdgeLocations.addAll(findSafeOnEdge(edgeX2, edgeX1, edgeZ2, edgeZ2));
        //(x1,z2) -> (x1,z1)
        safeEdgeLocations.addAll(findSafeOnEdge(edgeX1, edgeX1, edgeZ2, edgeZ1));
    }

    /**Helper function used to find all safe teleport locations along edge */
    private List<Location> findSafeOnEdge(int x1, int x2, int z1, int z2) {
        int y = getLocation().getBlockY();
        World w = getLocation().getWorld();
        
        Location loc1 = new Location(w, x1, y, z1);
        Location loc2 = new Location(w, x2, y, z2);
        
        List<Location> out = new ArrayList<>();
        BlockIterator iter = new BlockIterator(loc1, 0, (int)loc1.distance(loc2));
        while(iter.hasNext()) {
            Location loc = iter.next().getLocation();
            if(TeleportUtil.safeTeleport(loc)) {
                out.add(loc);
            }
        }
        return out;
    }

    /**
     * @return Random safe location along edge of cuboid
     */
    public Location getSafeEdgeLocation() {
        return this.safeEdgeLocations.get(SwordCraftOnline.r.nextInt(this.safeEdgeLocations.size()-1));
    }

    /**
     * @return All possible safe edge locations along side of cuboid
     */
    public List<Location> getAllSafeEdgeLocations() {
        return Collections.unmodifiableList(this.safeEdgeLocations);
    }

}