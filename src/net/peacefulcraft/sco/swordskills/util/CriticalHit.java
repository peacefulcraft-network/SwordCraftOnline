package net.peacefulcraft.sco.swordskills.util;

import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

public class CriticalHit {
    private double damage;
        public double getDamage() { return DamageCalc(); }
        public void setDamage(double i) { damage = i; }

    private Entity vic;
        public Entity getVic() { return vic; }
        public void setVic(Entity e) { vic = e; }

    private Entity damager;
        public Entity getDamager() { return damager; }
        public void setDamager(Entity e) { damager = e; }

    public boolean isPlayer(Entity e) { return e instanceof Player; }
    public boolean isSCOPlayer(Player p) {
        SCOPlayer s = GameManager.findSCOPlayer(p);
        if(s == null) { return false; }
        return true;
    }

    public CriticalHit(EntityDamageByEntityEvent e) {
        this(e.getEntity(), e.getDamager(), e.getDamage());
    }

    public CriticalHit(Entity vic, Entity damager, double damage) {
        this.damager = damager;
        this.vic = vic;
        this.damage = damage;
    }

    /**
     * Handles critical hit chance and over criticals.
     * Over critical occurs on 100% chance intervals. For each
     * 100% over 100% damage is increased 1x.
     */
    public double DamageCalc() {
        double d = 0;
        if(isPlayer(damager) && isSCOPlayer((Player) damager)) {
            SCOPlayer s = GameManager.findSCOPlayer((Player) damager);

            Random rand = new Random();
            int chance = s.getCriticalChance();
            int mult = 1;
            if(chance > 100) {
                for(; chance > 100; chance-=100) {
                    mult++;
                }
            }

            if(rand.nextInt(100) <= chance) {
               d = mult * s.getCriticalMultiplier() * damage;
               ((Player) damager).sendMessage(ChatColor.BOLD + "" + ChatColor.RED + mult + "x Critical Hit!");
            } else if(mult > 1) {
                d = (mult - 1) * s.getCriticalMultiplier() * damage;
                ((Player) damager).sendMessage(ChatColor.BOLD + "" + ChatColor.RED + (mult - 1) + "x Critical Hit!");
            }
        }
        return d;
    }


}