package net.peacefulcraft.sco.structures.structures;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.structures.Structure;
import net.peacefulcraft.sco.utilities.WeightedList;

public class Viral extends Structure {

    /**Maximum genetic spread of viral spread */
    private int maxSpread;

    /**Chance a block has to spread */
    private int spreadChance = 1;

    /**If true, spread progression is slowed */
    private boolean slowSpread = false;
        public void setSlowSpread(boolean set) { slowSpread = set; }

    /**Time, in seconds, of slow spread */
    private int slowSpreadSpeed = 1;
        public void setSlowSpreadSpeed(int speed) { slowSpreadSpeed = speed; }

    public Viral(int maxSpread, Material mat, boolean toCleanup, int cleanupTimer) {
        super(mat, toCleanup, cleanupTimer);

        this.maxSpread = maxSpread;
    }

    public Viral(int maxSpread, Material mat) {
        this(maxSpread, mat, false, 0);
    }

    public Viral(int maxSpread, WeightedList<Material> matLis, boolean toCleanup, int cleanupTimer) {
        super(matLis, toCleanup, cleanupTimer);
        
        this.maxSpread = maxSpread;
    }

    public Viral(int maxSpread, WeightedList<Material> matLis) {
        this(maxSpread, matLis, false, 0);
    }

    @Override
    public void _construct() {
        Location loc = getLocation();
        if(loc == null) { return; }

        spread(loc, this.maxSpread, this.spreadChance);
    }

    /**
     * Recursive function
     * Spreads viral load to nearby blocks, reducing spread factor each time
     * @param loc
     * @param spread
     */
    private void spread(Location loc, int spread, int spreadChance) {
        if(spread == 0) { return; }

        Location conformed = conformToTerrain(loc);
        if(slowSpread) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), new Runnable()
            {
                @Override
                public void run() {
                    viralConstruct(conformed, spread, spreadChance);
                }
            }, slowSpreadSpeed * 20);
        } else {
            viralConstruct(conformed, spread, spreadChance);
        }
    }

    /**
     * Helper function
     * Holds build logic for viral spread
     * @param conformed Conformed location
     */
    private void viralConstruct(Location conformed, int spread, int spreadChance) {
        Block block = conformed.getBlock();
        safeAddToCleanup(block.getType(), conformed);

        block.setType(getType());
        blockCollisionEffects(conformed);

        // Recursive check each neighbor location
        Location north = conformed.clone().add(1, 0, 0);
        if(SwordCraftOnline.r.nextInt(spreadChance) == 0) { 
            spread(north, rollSpread(spread), rollSpreadChance(spreadChance)); 
        }
        Location south = conformed.clone().add(-1, 0, 0);
        if(SwordCraftOnline.r.nextInt(spreadChance) == 0) { 
            spread(south, rollSpread(spread), rollSpreadChance(spreadChance)); 
        }
        Location west = conformed.clone().add(0, 0, 1);
        if(SwordCraftOnline.r.nextInt(spreadChance) == 0) { 
            spread(west, rollSpread(spread), rollSpreadChance(spreadChance)); 
        }
        Location east = conformed.clone().add(0, 0, -1);
        if(SwordCraftOnline.r.nextInt(spreadChance) == 0) { 
            spread(east, rollSpread(spread), rollSpreadChance(spreadChance)); 
        }
    }
    
    /**
     * Helper function
     * Rolls chance for spread chance to be increased.
     * @param spreadChance Determinant factor in spreading
     * @return Modified spreadChance
     */
    private int rollSpreadChance(int spreadChance) {
        if(SwordCraftOnline.r.nextInt(3) == 0) {
            spreadChance++;
        }
        return spreadChance;
    }

    /**
     * Helper function
     * Rolls chance for spread to be decreased
     * @param spread Determinant spread
     * @return Modified spread
     */
    private int rollSpread(int spread) {
        if(SwordCraftOnline.r.nextInt(1) == 0) {
            spread--;
        }
        return spread;
    }

}
