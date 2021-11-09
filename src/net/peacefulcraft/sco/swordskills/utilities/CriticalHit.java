package net.peacefulcraft.sco.swordskills.utilities;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.announcer.Announcer;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser.CombatModifier;

/**
 * Handles critical hit calculations on modifier users
 */
public class CriticalHit {

    /**
     * Handles critical hit chance and over criticals.
     * Over critical occurs on 100% chance intervals. For each
     * 100% over 100% damage is increased 1x.
     * @param chanceModifier Any temporary crit chance value for this calculation
     * @param damageModifier Any temporary crit damage value for this calculation
     */
    public static double damageCalc(ModifierUser damMu, double damage, int chanceModifier, double multModifier) {
        double d = damage;

        int chance = (int) (damMu.getCombatModifier(CombatModifier.CRITICAL_CHANCE) + chanceModifier);
        int mult = 1;
        
        // Over Critical calculations
        if(chance > 100) {
            for(; chance > 100; chance -= 100) {
                mult++;
            }
        }

        double multiplier = damMu.getCombatModifier(CombatModifier.CRITICAL_MULTIPLIER) + multModifier;
        if(SwordCraftOnline.r.nextInt(100) <= chance) {
            d = mult * multiplier * damage;
            if(damMu instanceof SCOPlayer) {
                Announcer.messagePlayer((SCOPlayer)damMu, ChatColor.BOLD + "" + ChatColor.RED + mult + "x Critical Hit!", 0);
            }
        } else if(mult > 1) {
            d = (mult - 1) * multiplier * damage;
            if(damMu instanceof SCOPlayer) {
                Announcer.messagePlayer((SCOPlayer)damMu, ChatColor.BOLD + "" + ChatColor.RED + (mult - 1) + "x Critical Hit!", 0);
            }
        }
        return d;
    }

    /**
     * Calculates a flat rate multiplier to be put against damage
     */
    public static double calculateMultiplier(ModifierUser damagerMu, int chanceModifier, double multModifier) {
        int chance = (int) (damagerMu.getCombatModifier(CombatModifier.CRITICAL_CHANCE) + chanceModifier);
        int mult = 1;
        
        if (chance > 100) {
            for (; chance > 100; chance -= 100) {
                mult++;
            }
        }
        double multiplier = damagerMu.getCombatModifier(CombatModifier.CRITICAL_MULTIPLIER) + multModifier;
        if (SwordCraftOnline.r.nextInt(100) <= chance) {
            return mult * multiplier;
        } else if(mult > 1) {
            return (mult - 1) * multiplier;
        }
        return 1;
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