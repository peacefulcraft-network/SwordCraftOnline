package net.peacefulcraft.sco.swordskills.utilities;

import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;

public class Parry {
    private double damage;
        public double getDamage() { return damage; }

    private Entity vic;
        public Entity getVic() { return vic; }

    private Entity damager;
        public Entity getDamager() { return damager; }

    public Parry(EntityDamageByEntityEvent e) {
        this(e.getEntity(), e.getDamager(), e.getDamage());
    }

    public Parry(Entity vic, Entity damager, double damage) {
        this.damager = damager;
        this.vic = vic;
        this.damage = damage;
    }

    public double ParryCalc() {
        if(vic instanceof Player) {
            SCOPlayer s = GameManager.findSCOPlayer((Player) vic);
            if(s == null) { return this.damage; }

            if(SwordCraftOnline.r.nextInt(100) <= s.getParryChance()) {
                ((Player) vic).sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "Attack Parried!");
                if(damager instanceof Player && GameManager.findSCOPlayer((Player)damager) != null) {
                    ((Player) damager).sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "Attack Parried!");
                }
                return this.damage * s.getParryMultiplier();
            }
        } else {
            ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().get(vic.getUniqueId());
            if(am == null) { return this.damage; }

            if(SwordCraftOnline.r.nextInt(100) <= am.getParryChance()) {
                if(damager instanceof Player && GameManager.findSCOPlayer((Player)damager) != null) {
                    ((Player) damager).sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "Attack Parried!");
                }
                return this.damage * am.getParryMultiplier();
            }
        }
        return this.damage;
    }

    /**
     * Static method used to simulate parry between two mm files
     * Called internally for console
     */
    public static double simulate(MythicMob vic, double damage) {
        if(SwordCraftOnline.r.nextInt(100) <= vic.getParryChance()) {
            return damage * vic.getParryMultiplier();
        }
        return damage;
    }
}