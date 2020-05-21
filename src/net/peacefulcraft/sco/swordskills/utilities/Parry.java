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

    public boolean ParryCalc() {
        if(vic instanceof Player) {
            SCOPlayer s = GameManager.findSCOPlayer((Player) vic);
            if(s == null) { return false; }

            if(SwordCraftOnline.r.nextInt(100) <= s.getParryChance()) {
                ((Player) vic).sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "Attack Parried!");
                if(damager instanceof Player && GameManager.findSCOPlayer((Player)damager) != null) {
                    ((Player) damager).sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "Attack Parried!");
                }
                return true;
            }
        } else {
            ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().get(vic.getUniqueId());
            if(am == null) { return false; }

            if(SwordCraftOnline.r.nextInt(100) <= am.getParryChance()) {
                if(damager instanceof Player && GameManager.findSCOPlayer((Player)damager) != null) {
                    ((Player) damager).sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "Attack Parried!");
                }
                return true;
            }
        }
        return false;
    }
}