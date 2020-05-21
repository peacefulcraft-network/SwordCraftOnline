package net.peacefulcraft.sco.swordskills.utilities;

import java.util.ArrayList;
import java.util.Arrays;
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
        if(damager instanceof Player) {
            SCOPlayer s = GameManager.findSCOPlayer((Player) damager);
            if(s == null) { return 0; }

            Random rand = new Random();
            int chance = s.getCriticalChance();
            int mult = 1;
            /**If critical hit chance is above 100% we over critical */
            if(chance > 100) {
                for(; chance > 100; chance-=100) {
                    mult++;
                }
            }

            /**Over critical logic: If not chance, guaranteed critical 1 level lower */
            if(rand.nextInt(100) <= chance) {
               d = mult * s.getCriticalMultiplier() * damage;
               ((Player) damager).sendMessage(ChatColor.BOLD + "" + ChatColor.RED + mult + "x Critical Hit!");
            } else if(mult > 1) {
                d = (mult - 1) * s.getCriticalMultiplier() * damage;
                ((Player) damager).sendMessage(ChatColor.BOLD + "" + ChatColor.RED + (mult - 1) + "x Critical Hit!");
            }
        } else {
            ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().get(damager.getUniqueId());
            if(am == null) { return 0; }

            int chance = am.getCriticalChance();
            int mult = 1;

            if(chance > 100) {
                for(; chance > 100; chance -= 100) {
                    mult++;
                }
            }

            if(SwordCraftOnline.r.nextInt(100) <= chance) {
                d = mult * am.getCriticalMultiplier() * this.damage;
            } else if(mult > 1) {
                d = (mult - 1) * am.getCriticalMultiplier() * damage;
            }
        }
        return d;
    }

    /**
     * Static method used to simulate critical hit between two mm files.
     * Called internally for console
     */
    public static double simulate(MythicMob damager, double damage) {
        int chance = damager.getCriticalChance();
        int mult = 1;
        if(chance > 100) {
            for(; chance > 100; chance -= 100) {
                mult++;
            }
        }
        if(SwordCraftOnline.r.nextInt(100) <= chance) {
            return mult * damager.getCriticalMultiplier() * damage;
        } else if(mult > 1) {
            return (mult - 1) * damager.getCriticalMultiplier() * damage;
        }
        return damage;
    }

}