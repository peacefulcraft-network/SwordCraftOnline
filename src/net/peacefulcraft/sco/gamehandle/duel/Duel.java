package net.peacefulcraft.sco.gamehandle.duel;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.announcer.Announcer;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
import net.peacefulcraft.sco.mythicmobs.spawners.ActiveSpawner;
import net.peacefulcraft.sco.structures.structures.HollowCuboid;
import net.peacefulcraft.sco.utilities.GiveItemsUtil;

public class Duel {
    
    private SCOPlayer challenger;

    private SCOPlayer challenged;

    /**Barrier around players */
    private HollowCuboid cube;

    /**Spawners locked in setup */
    private List<ActiveSpawner> lockedSpawners;

    /**Determines if duel is in setup lifecycle */
    private Boolean inSetup = false;
        public Boolean isInSetup() { return this.inSetup; }

    private List<Location> wagerChests;

    /**Wagers placed by players on duel */
    private List<ItemStack> wagers;

    private SCOPlayer loser;

    private SCOPlayer winner;

    /**
     * Create base duel with two players opposing
     * @param s1
     * @param s2
     */
    public Duel(SCOPlayer challenger, SCOPlayer challenged) {
        this.challenger = challenger;
        this.challenged = challenged;

        //Marking both players in duel
        challenger.setDuel(this);
        challenged.setDuel(this);

        lockedSpawners = new ArrayList<>();
        wagerChests = new ArrayList<>();
    }

    /**
     * Begins primary checks for duel after duel has been
     * challenged and accepted.
     */
    public void setupLifeCycle() {
        this.inSetup = true;

        //Creating barrier walls && telporting players
        Location cubeCenter = challenger.getPlayer().getLocation();

        this.cube = new HollowCuboid(25, 30, Material.BARRIER);
        cube.setManualCleanup(true);
        cube.setTargetLocation(cubeCenter);
        cube.construct();

        challenger.getPlayer().teleport(cubeCenter.clone().add(5, 0, 0));
        challenged.getPlayer().teleport(cubeCenter.clone().add(-5, 0, 0));
        placeWagerChest(cubeCenter.clone().add(3, 0, 0));
        placeWagerChest(cubeCenter.clone().add(-3, 0, 0));

        //TODO: Prompt player to place wager items in chest

        /**
         * Teleporting players to safe locations around the barrier
         * Killing any non-persistent active mobs
         */
        for(Entity e : challenger.getPlayer().getNearbyEntities(15, 15, 15)) {
            if(e instanceof Player) {
                //Not in the game. We don't care
                Player p = (Player)e;
                SCOPlayer s = GameManager.findSCOPlayer(p);
                if(s == null) { continue; }

                //Player is not in barrier. We don't care
                if(!cube.isInCuboid(p.getLocation())) { continue; }
                //Teleporting player to safe edge location
                p.teleport(cube.getSafeEdgeLocation());
                
                //TODO: Send title card: "Duel is starting" "name vs. name"
            }

            if(e instanceof Mob) {
                //Mob is not activemob we don't care
                ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().get(e.getUniqueId());
                if(am == null) { continue; }

                am.setUnloaded();
            }
        }

        /**
         * Temporarily blocking spawners in area from running
         */
        for(ActiveSpawner as : SwordCraftOnline.getPluginInstance().getSpawnerManager().getRegistryList()) {
            //Not in barrier. We don't care
            if(!cube.isInCuboid(as.getLocation())) { continue; }
            as.setLocked(true);
            lockedSpawners.add(as);
        }

        //TODO: Disable non-passive sword skills

        /**
         * Collecting wagers and removing chests
         */
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SwordCraftOnline.getPluginInstance(), new Runnable(){
            @Override
            public void run() {
                for(Location loc : wagerChests) {
                    Chest chest = (Chest)loc.getBlock();
                    for(ItemStack i : chest.getBlockInventory().getContents()) {
                        wagers.add(i.clone());
                    }
                    loc.getBlock().setType(Material.AIR);
                    loc.clone().add(0, -1, 0).getBlock().setType(Material.AIR);
                }
                beginDuel();
            }
        }, 300);
    }

    private void beginDuel() {
        List<Player> temp = new ArrayList<>();
        temp.add(challenged.getPlayer());
        temp.add(challenger.getPlayer());
        Announcer.messageCountdown(temp, 3, "The duel is beginning in...", "BEGIN", true);

        this.inSetup = false;
    }

    /**
     * Begins the wrap up lifecycle of duel after
     * duel has been completed.
     */
    public void deleteLifeCycle() {
        /**
         * Deconstructing barrier
         */
        cube.cleanup();

        /**
         * Unlocking all locked spawners
         */
        for(ActiveSpawner as : this.lockedSpawners) {
            as.setLocked(false);
        }

        //TODO: Enable non-passive sword skills

        //TODO: Display victor title cards

        /**
         * Safely giving items to winner
         */
        GiveItemsUtil.giveItems(winner.getPlayer(), wagers);
    }

    /**
     * Sets loser and winner of duel
     * @param s The loser of the duel
     */
    public void setLoser(SCOPlayer s) { 
        this.loser = s; 
        if(s.equals(challenger)) {
            this.winner = challenged;
        } else {
            this.winner = challenger;
        }
    }

    /**Helper function: Places wager chests into world */
    private void placeWagerChest(Location loc) {
        loc.clone().add(0, 1, 0).getBlock().setType(Material.GOLD_BLOCK);
        
        Location temp = loc.clone().add(0, 2, 0);
        wagerChests.add(temp);
        temp.getBlock().setType(Material.CHEST);
    }

}