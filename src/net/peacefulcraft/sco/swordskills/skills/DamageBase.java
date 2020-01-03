package net.peacefulcraft.sco.swordskills.skills;

import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

/**
 * Base damage event for all SCOPlayer damage dealing. Is effected by players
 * critical chance and damage.
 */
public class DamageBase implements Listener {

    @EventHandler
    public void DamageEvent(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            
            SCOPlayer s = GameManager.findSCOPlayer(p);
            if(s == null) { return; }

            Random rand = new Random();
            if(rand.nextInt(100) <= s.getCriticalChance()) {
                e.setDamage(s.getCriticalMultiplier() * e.getDamage());
                p.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "Critical Hit!");
            }
            
            if(e.getEntity() instanceof Player) {
                Player vic = (Player) e.getEntity();

                SCOPlayer SCOvic = GameManager.findSCOPlayer(vic);
                if(SCOvic == null) { return; }

                if(rand.nextInt(100) <= SCOvic.getParryChance()) {
                    vic.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "Damage Parried!");
                    p.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "Damage Parried!");
                    e.setCancelled(true);
                }
            }
        }
    }
}