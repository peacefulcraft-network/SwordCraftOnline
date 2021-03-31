package net.peacefulcraft.sco.gamehandle.listeners;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerMoveEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.gamehandle.regions.FarmRegion;
import net.peacefulcraft.sco.gamehandle.regions.Region;
import net.peacefulcraft.sco.gamehandle.regions.RegionManager;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;

public class RegionCheckListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRegionEnter(PlayerMoveEvent e) {
        // Has player moved
        if (e.getFrom().distanceSquared(e.getTo()) == 0) {
            return;
        }

        // Is player in the game
        Player p = e.getPlayer();
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if (s == null) {
            return;
        }

        // Is players floor invalid
        if (s.getFloor() == 0) {
            return;
        }

        // Are there regions on the floor. Possible redundant check
        ArrayList<Region> regions = RegionManager.getFloorRegionsMap().get(s.getFloor());
        if (regions == null || regions.isEmpty()) {
            return;
        }

        Location locTo = e.getTo();
        for (Region r : regions) {
            // If player has entered a new region
            if (s.getRegion() != r && r.isInRegion(locTo)) {

                // Fetching child if it exists
                Region child = r.getChild(locTo);
                if (child != null) {
                    s.setRegion(child, r.isSilentParentTransfer());
                } else {
                    s.setRegion(r, false);
                }
                return;
            }
        }

        // Player is not in any region
        s.setRegion(null, true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockHit(PlayerAnimationEvent e) {
        Player p = e.getPlayer();
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) { return; }

        Region r = s.getRegion();
        if(r == null) { return; }

        if(!r.isFarm()) { return; }
        FarmRegion farm = (FarmRegion) r;

        PlayerAnimationType type = e.getAnimationType();
        if(!type.equals(PlayerAnimationType.ARM_SWING)) { return; }

        Block block = p.getTargetBlock(null, 4);
        if(!block.getType().equals(farm.getVanillaCropType())) { return; }

        block.setType(Material.AIR);
        
        Location loc = block.getLocation().clone();
        loc.getWorld().dropItemNaturally(loc, ItemIdentifier.generateItem(farm.getCrop(), ItemTier.COMMON, 1));
        
        /**
         * Bonus farming drop chances
         */
        int chance = s.getFarmingChance();
        int mult = 0;

        if(chance > 100) {
            for(; chance > 100; chance -= 100) {
                mult++;
            }
        }
        if(SwordCraftOnline.r.nextInt(100) <= chance) { mult++; }

        if(mult != 0) {
            loc.getWorld().dropItemNaturally(loc, ItemIdentifier.generateItem(farm.getCrop(), ItemTier.COMMON, mult));
        }
    }
}