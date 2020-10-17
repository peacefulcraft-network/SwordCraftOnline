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
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser.CombatModifier;

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

    /**Does parry calculations with no modifiers */
    public double parryCalc() {
        return parryCalc(0, 0);
    }

    /**
     * Handles parry logic for damage dampening
     * @param chanceModifier Any parry chance modifiers for calculation
     * @param multModifier Any parry mulitiplier modifiers for calculation. Consider negative values as mult is dampening effect.
     * @return Damage to be set on victim
     */
    public double parryCalc(int chanceModifier, double multModifier) {
        if(vic instanceof Player) {
            SCOPlayer s = GameManager.findSCOPlayer((Player) vic);
            if(s == null) { return this.damage; }

            int chance = (int) s.getCombatModifier(CombatModifier.PARRY_CHANCE) + chanceModifier;
            if(SwordCraftOnline.r.nextInt(100) <= chance) {
                ((Player) vic).sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "Attack Parried!");
                if(damager instanceof Player && GameManager.findSCOPlayer((Player)damager) != null) {
                    ((Player) damager).sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "Attack Parried!");
                }
                return this.damage * (s.getCombatModifier(CombatModifier.PARRY_MULTIPLIER) + multModifier);
            }
        } else {
            ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().getMobRegistry().get(vic.getUniqueId());
            if(am == null) { return this.damage; }

            int chance = (int) am.getCombatModifier(CombatModifier.PARRY_CHANCE) + chanceModifier;
            if(SwordCraftOnline.r.nextInt(100) <= chance) {
                if(damager instanceof Player && GameManager.findSCOPlayer((Player)damager) != null) {
                    ((Player) damager).sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "Attack Parried!");
                }
                return this.damage * (am.getCombatModifier(CombatModifier.PARRY_MULTIPLIER) + multModifier);
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