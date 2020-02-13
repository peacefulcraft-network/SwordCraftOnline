package net.peacefulcraft.sco.gamehandle.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
/**
 * Handles pet spawning and sword skill effects on mob and player.
 */
public class PetTokenListener implements Listener {
    @EventHandler
    public void tokenUseEvent(PlayerInteractEvent ev) {
        Player p = ev.getPlayer();
        
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) { return; }

        String id = p.getUniqueId().toString();

        //Checking if items match and if players id is in pet list
        if(!p.getInventory().getItemInMainHand().getType().equals(Material.IRON_NUGGET)) { return; }
        if(!p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("Pet Token")) { return; }
        if(!SwordCraftOnline.getPluginInstance().getMobManager().getPetList().keySet().contains(id)) { return; }

        String petName = SwordCraftOnline.getPluginInstance().getMobManager().getPetList().get(id).getInternalName();

        ActiveMob am = check(petName);
        if(am != null && s.isPetActive()) {
            SwordCraftOnline.getPluginInstance().getMobManager().unregisterActiveMob(am);
            s.setPetActive(false);
            SwordCraftOnline.logInfo("Despawned " + p.getName());
        } else if(am == null && !s.isPetActive()) {
            SwordCraftOnline.getPluginInstance().getMobManager().spawnMob(petName, p.getLocation());
            SwordCraftOnline.logInfo("Spawned " + p.getName() + "'s mob on player location");
            s.setPetActive(true);
        }    
        //TODO: Handle swordskill pet modifiers.
    }

    /**
     * Iterates through ActiveMobs
     * @return true if mob is active
     */
    private ActiveMob check(String name) {
        for(ActiveMob am : SwordCraftOnline.getPluginInstance().getMobManager().getActiveMobs()) {
            if(am.getDisplayName().equals(name)) { return am; }
        }
        return null;
    }
}