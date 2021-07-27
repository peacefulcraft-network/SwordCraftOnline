package net.peacefulcraft.sco.utilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.BlockIterator;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.swordskills.utilities.DirectionalUtil;

/**
 * Location util static class
 * 
 * Holds methods regarding getting shapes, permutations around a location
 * I.e. All blocks near a location in X radius
 */
public class LocationUtil {

    /**
     * Given two locations find a random location within them
     * @param loc1
     * @param loc2
     * @return
     */
    public static Location getLocationsWithinSquare(Location loc1, Location loc2) {
        int xMax;
        int xMin;
        int yMax;
        int yMin;
        int zMax;
        int zMin;

        // X min max
        if (loc1.getBlockX() < loc2.getBlockX()) {
            xMax = loc2.getBlockX();
            xMin = loc1.getBlockX();
        } else {
            xMax = loc1.getBlockX();
            xMin = loc2.getBlockX();
        }

        // Y min max
        if (loc1.getBlockY() < loc2.getBlockY()) {
            yMax = loc2.getBlockY();
            yMin = loc1.getBlockY();
        } else {
            yMax = loc1.getBlockY();
            yMin = loc2.getBlockY();
        }

        // Z min max
        if (loc1.getBlockZ() < loc2.getBlockZ()) {
            zMax = loc2.getBlockZ();
            zMin = loc1.getBlockZ();
        } else if (loc1.getBlockZ() > loc2.getBlockZ()) {
            zMax = loc1.getBlockZ();
            zMin = loc2.getBlockZ();
        } else {
            zMax = loc1.getBlockZ();
            zMin = loc1.getBlockZ();
        }

        return new Location(
            loc1.getWorld(), 
            SwordCraftOnline.r.nextInt(xMax - xMin) + xMin, 
            SwordCraftOnline.r.nextInt(yMax - yMin) + yMin, 
            SwordCraftOnline.r.nextInt(zMax - zMin) + zMin);
    }
    
    /**
     * Compresses list of locations to list of blocks
     * @param lis Input 
     * @return Converted list of blocks
     */
    public static List<Block> locationsToBlocks(List<Location> lis) {
        List<Block> out = new ArrayList<>();
        for(Location loc : lis) {
            Block temp = loc.getBlock();
            if(out.contains(temp)) { continue; }
            out.add(temp);
        }
        return out;
    }

    /**
     * Given location, calculates and logs nearby locations in square shape
     * @param sideLength Side length of square
     * @param loc Location to be checked
     * @return List of locations
     */
    public static List<Location> getSquareLocations(Location loc, int sideLength) {
        List<Location> locations = new ArrayList<>();
        int halfSize = sideLength/2;

        //Iterating over every location within square bounds
        for(int x = -halfSize; x < halfSize; x++) {
            for(int z = -halfSize; z < halfSize; z++) {
                locations.add(loc.clone().add(x, 0, z));
            }
        }

        return locations;
    }

    /**
     * Given location and target, returns all locations associated between the two.
     * @param origin Origin of pathway
     * @param target Target location
     * @param length Max length of path. Set to -1 to have none.
     * @param width Width of pathway
     * @param conform If true, locations will be set to match surrounding terrain
     * @return List of locations
     */
    public static List<Location> getPathLocations(Location origin, Location target, int length, int width, boolean conform) {
        List<Location> locations = new ArrayList<>();
        
        int halfWidth = width/2;
        BlockFace sideFace = DirectionalUtil.getSideDirections(origin, target);

        BlockIterator iter = new BlockIterator(origin, 0, (int)target.distance(target));
        int i = 0;
        while(iter.hasNext()) {
            // If we conform and reach max length. 
            if(i >= length && conform) { break; }
            Block block = iter.next();
            
            // Iterating over width blocks
            for(int j = -halfWidth; j <= halfWidth; j++) {
                Block side = block.getRelative(sideFace, j);
                locations.add(conform(side.getLocation()));
            }
            i++;
        }
        return locations;
    }

    /**
     * Given locations, returns all locations in pillar shape of given dimensions
     * @param loc Location to be checked
     * @param height Height of pillar
     * @param length Side length of pillar
     * @return List of locations
     */
    public static List<Location> getPillarLocations(Location loc, int height, int length) {
        List<Location> locations = new ArrayList<>();

        int halfLength = length/2;

        // Iterating over locations in pillar
        for(int x = (int) loc.getX() - halfLength; x <= loc.getX() + halfLength; x++) {
            for(int y = (int) loc.getY(); y <= (int) loc.getY() + height; y++) {
                for(int z = (int) loc.getZ() - halfLength; z <= (int) loc.getZ() + halfLength; z++) {
                    Location temp = new Location(loc.getWorld(), x, y ,z);
                    locations.add(temp);
                }
            }
        }
        return locations;
    }

    /**
     * Given location, returns all locations in cylinder shape of given dimensions
     * @param loc Location to be checked
     * @param height Height of cylinder
     * @param radius Radius of cylinder
     * @param hollow If true, cylinder will be a hollow ring
     * @return List of locations
     */
    public static List<Location> getCylinderLocations(Location loc, int height, int radius, boolean hollow) {
        List<Location> locations = new ArrayList<Location>();
        if(hollow) {
            int x;
            int y = loc.getBlockY();
            int z;
            for(int i = 0; i <= height; i++) {
                for(double j = 0.0d; j< 360.0; j += 0.1) {
                    double angle = j * Math.PI / 180;
                    x = (int)(loc.getX() + radius * Math.cos(angle));
                    z = (int)(loc.getZ() + radius * Math.sin(angle));

                    Location current = new Location(loc.getWorld(), x, y ,z);
                    locations.add(current);
                }
            }
        } else {
            Block center = loc.getBlock();
            for(int currentHeight = 0; currentHeight < height; currentHeight++) {
                for(int x = -radius; x < radius; x++) {
                    for(int z = -radius; z < radius; z++) {
                        Block current = center.getRelative(x, currentHeight, z);
                        Location temp = current.getLocation();

                        locations.add(temp);
                    }
                }
            }
        }
        return locations;
    }

    /**
     * Given location, returns random locations in radius around it
     * @param loc Location to be checked
     * @param radius Radius of area checked
     * @param amount Number of locations in list
     * @return List of locations found
     */
    public static List<Location> getRandomLocations(Location loc, int radius, int amount) {
        List<Location> locations = new ArrayList<Location>();
        for(int i = 0; i <= amount; i++) {
            int r = SwordCraftOnline.r.nextInt(radius);
            int x = 0;
            if(r != 0) { x += SwordCraftOnline.r.nextInt(r); }
            int z = (int)Math.sqrt(Math.pow(r, 2) - Math.pow(x,2));
            if(SwordCraftOnline.r.nextBoolean()) { x *= -1; }
            if(SwordCraftOnline.r.nextBoolean()) { x *= -1; }

            Location temp = loc.clone().add(x, 0, z);
            locations.add(conform(temp));
        }
        return locations;
    }

    /**
     * Helper method. Conforms location to ground
     * @return Location set to ground
     */
    private static Location conform(Location loc) {
        Block block = loc.getBlock();
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
        return block.getLocation();
    }

}
