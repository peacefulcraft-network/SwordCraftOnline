package net.peacefulcraft.sco.swordskills.util;

import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

public class Parry {
    private double damage;
        public double getDamage() { return damage; }

    private Entity vic;
        public Entity getVic() { return vic; }

    private Entity damager;
        public Entity getDamager() { return damager; }

    public boolean isPlayer(Entity e) { return e instanceof Player; }
    public boolean isSCOPlayer(Player p) {
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) { return false; }
        return true;
    }

    public Parry(EntityDamageByEntityEvent e) {
        this(e.getEntity(), e.getDamager(), e.getDamage());
    }

    public Parry(Entity vic, Entity damager, double damage) {
        this.damager = damager;
        this.vic = vic;
        this.damage = damage;
    }

    public boolean ParryCalc() {
        if(isPlayer(vic) && isSCOPlayer((Player) vic)) {
            SCOPlayer s = GameManager.findSCOPlayer((Player) vic);

            Random rand = new Random();
            if(rand.nextInt(100) <= s.getParryChance()) {
                ((Player) vic).sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "Attack Parried!");
                if(isPlayer(damager) && isSCOPlayer((Player) damager)) {
                    ((Player) damager).sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "Attack Parried!");
                }
                return true;
            }
        }
        return false;
    }
}