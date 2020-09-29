package net.peacefulcraft.sco.structures;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;

/**
 * Handles safe block tracking
 */
public class StructureManager {
    
    private static HashMap<Location, Material> blocks = new HashMap<>();

    public StructureManager() {}

    /**@return true if map contains location*/
    public static boolean checkBlocks(Location loc) {
        if(blocks.get(loc) != null) { return true; }
        return false;
    }

    public static void addBlock(Location loc, Material mat) {
        if(checkBlocks(loc)) { return; }

        blocks.put(loc, mat);
    }

    public static void removeBlock(Location loc) {
        blocks.remove(loc);
    }

}