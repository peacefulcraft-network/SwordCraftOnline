package net.peacefulcraft.sco.gamehandle.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.duel.Duel;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicPet;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

/**
 * Main death logic for SCOPlayers
 */
public class SCOPlayerDamageListener implements Listener {
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerDeath(EntityDamageEvent e) {
        Entity ent = e.getEntity();
        if(!(ent instanceof Player)) { return; }

        Player p = (Player)ent;
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) { return; }

        // Checking players damage cause and setting LCOD
        if(e instanceof EntityDamageByEntityEvent) {

            // Cancelling pet damage
            Entity damager = ((EntityDamageByEntityEvent)e).getDamager();
            if(checkPet(s, damager)) {
                e.setCancelled(true);
                return;
            }
            
            ModifierUser mu = checkType(damager);
            if(mu != null) {
                s.setLastCauseOfDamage(mu);
            }
        }

        //If player died
        if(p.getHealth() - e.getFinalDamage() <= 0) {
            if(s.isInDuel()) {
                //Triggering players duel lifecycle
                Duel d = s.getDuel();
                d.setLoser(s);
                d.deleteLifeCycle();

                e.setCancelled(true);
            }
        }
    }

    /**
     * Helper function
     * Checks scoplayers pet against entity
     * @param s SCOPlayer
     * @param e Entity to check
     * @return True if pet and entity match, false otherwise
     */
    private boolean checkPet(SCOPlayer s, Entity e) {
        ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().get(e.getUniqueId());
        if(am == null) { return false; }

        MythicPet pet = s.getPet();
        if(pet == null) { return false; }

        if(pet.getActiveMob() == am) { return true; }
        return false;
    }

    /**
     * Helper function
     * Checks mob type against modifer users
     * @param e Entity we are checking
     * @return Modifier user if entity is valid. Null otherwise
     */
    private ModifierUser checkType(Entity e) {
        ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().get(e.getUniqueId());
        if(am == null && !(e instanceof Player)) {
            // Activemob is null and entity isn't player. Don't care
            return null;
        } else if(am != null && !(e instanceof Player)) {
            // Valid ActiveMob and not player
            return am;
        }

        // Entity is player. We check game manager
        return GameManager.findSCOPlayer((Player)e);
    }
}