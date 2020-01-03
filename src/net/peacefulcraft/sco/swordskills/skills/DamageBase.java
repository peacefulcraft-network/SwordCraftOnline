package net.peacefulcraft.sco.swordskills.skills;

import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.swordskills.util.CriticalHit;
import net.peacefulcraft.sco.swordskills.util.Parry;

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

            CriticalHit c = new CriticalHit(e);
            e.setDamage(c.getDamage());

            Parry par = new Parry(e);
            e.setCancelled(par.ParryCalc());
        }
    }
}