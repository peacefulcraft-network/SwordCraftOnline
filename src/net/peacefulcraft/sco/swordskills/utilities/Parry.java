package net.peacefulcraft.sco.swordskills.utilities;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.announcer.Announcer;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.mobs.MythicMob;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser.CombatModifier;

public class Parry {

    /**
     * Handles parry logic for damage dampening
     * @param chanceModifier Any parry chance modifiers for calculation
     * @return True if attack was parried
     */
    public static boolean parryCalc(ModifierUser damMu, ModifierUser vicMu, int chanceModifier) {

        int chance = (int) vicMu.getCombatModifier(CombatModifier.PARRY_CHANCE) + chanceModifier;
        if(SwordCraftOnline.r.nextInt(100) <= chance) {
            if(vicMu instanceof SCOPlayer) {
                Announcer.messagePlayer((SCOPlayer)vicMu, ChatColor.BOLD + "" + ChatColor.RED + "Attack Parried!", 0);
            }
            if(damMu != null && damMu instanceof SCOPlayer) {
                Announcer.messagePlayer((SCOPlayer)damMu, ChatColor.BOLD + "" + ChatColor.RED + "Attack Parried!", 0);
            }

            return true;
        }
        return false;
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