package net.peacefulcraft.sco.gamehandle.listeners;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.gamehandle.player.Teleports;

public class EnterDungeon implements Listener {
    
    @EventHandler
    public void onBlockClick(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = e.getClickedBlock();
            if(block.getType() == Material.GOLD_BLOCK) {
                if(block.getRelative(BlockFace.DOWN).getType() == Material.BLACK_WOOL && block.getRelative(
                    BlockFace.UP).getType() == Material.BLACK_WOOL) {
                    
                    SCOPlayer s = GameManager.findSCOPlayer((Player) e);
                    ArrayList<SCOPlayer> ls = findNearbyPlayers(s.getFloor());

                    SwordCraftOnline.getDungeonManager().joinDungeon(ls, s.getFloor());
                }
            }
        }
    }

    /**Finds SCOPlayers near arena entrance locations */
    public ArrayList<SCOPlayer> findNearbyPlayers(int index) {
        ArrayList<SCOPlayer> back = new ArrayList<SCOPlayer>();
        //frange is squared distance from location to be scanned.
        int frange = 25;
        GameManager.getPlayers().values().forEach((s) -> {
            double d = s.getPlayer().getLocation().distanceSquared(Teleports.getDungeonEntrance(index));
            if (d <= frange) {
                 back.add(s);
            }
        });
        return back;
    }
}