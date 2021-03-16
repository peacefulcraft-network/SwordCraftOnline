package net.peacefulcraft.sco.gamehandle.announcer;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.utilities.Pair;

/**
 * Static class to handle messaging players
 * relative information for sword skill effects
 * 
 * Contains static methods to be called during skill trigger
 * for constant skill messaging support
 */
public class SkillAnnouncer {
    
    /**
     * Modifier user version of simple
     * sword skill message
     */
    public static void messageSkill(ModifierUser mu, String message, String skillName, ItemTier tier) {
        if(mu instanceof SCOPlayer) {
            messageSkill((SCOPlayer)mu, message, skillName, tier);
        }
    }

    /**
     * Modifier user version of true
     * damage dealt sword skill message
     */
    public static void messageSkill(ModifierUser mu, Double damage, String message, String skillName, ItemTier tier) {
        if(mu instanceof SCOPlayer) {
            messageSkill((SCOPlayer)mu, damage, message, skillName, tier);
        }
    }

    /**
     * Modifier user version of single changed stat
     * sword skill message
     */
    public static void messageSkill(ModifierUser mu, Pair<String, Double> changedStat, String skillName, ItemTier tier) {
        if(mu instanceof SCOPlayer) {
            messageSkill((SCOPlayer)mu, changedStat, skillName, tier);
        }
    }

    /**
     * Modifier user version of single
     * potion effect sword skill message
     */
    public static void messageSkill(ModifierUser mu, String skillName, ItemTier tier, Pair<String, Integer> potionEffect) {
        if(mu instanceof SCOPlayer) {
            messageSkill((SCOPlayer)mu, skillName, tier, potionEffect);
        }
    }

    /**
     * Modifier user version of multiple
     * potion effect sword skill message
     */
    public static void messageSkill(ModifierUser mu, String skillName, ItemTier tier, List<Pair<String, Integer>> potionEffects) {
        if(mu instanceof SCOPlayer) {
            messageSkill((SCOPlayer)mu, skillName, tier, potionEffects);
        }
    }

    /**
     * SCOPlayer version of multiple changed
     * stat sword skill message
     */
    public static void messageSkill(SCOPlayer s, List<Pair<String, Double>> changedStats, String skillName, ItemTier tier) {
        messageSkill(s, changedStats, "", skillName, tier);
    } 

    /**
     * SCOPlayer version of single changed stat
     * sword skill message
     */
    public static void messageSkill(SCOPlayer s, Pair<String, Double> changedStat, String skillName, ItemTier tier) {
        List<Pair<String, Double>> pairs = new ArrayList<>();
        pairs.add(changedStat);
        messageSkill(s, pairs, skillName, tier);
    }

    /**
     * SCOPlayer version of multiple changed stat
     * sword skill message w/ opening message
     */
    public static void messageSkill(SCOPlayer s, List<Pair<String, Double>> changedStats, String message, String skillName, ItemTier tier) {
        for (Pair<String, Double> pair : changedStats) {
            String stat = pair.getFirst().replace("GENERIC", "").replace("Generic", "");
            message += stat + " changed by: " + pair.getSecond() + ", ";
        }
        message = message.substring(0, message.length() - 2) + ".";
        messageSkill(s, message, skillName, tier);
    }

    /**
     * SCOPlayer version of true damage dealt
     * sword skill message w/ opening message
     */
    public static void messageSkill(SCOPlayer s, Double damage, String message, String skillName, ItemTier tier) {
        messageSkill(s, message + ", True damage dealt: " + damage, skillName, tier);
    }

    /**
     * SCOPlayer version of true damage dealt
     * sword skill message
     */
    public static void messageSkill(SCOPlayer s, Double damage, String skillName, ItemTier tier) {
        messageSkill(s, "True damage dealt: " + damage, skillName, tier);
    }

    /**
     * SCOPlayer version of potion effect
     * sword skill message
     */
    public static void messageSkill(SCOPlayer s, String skillName, ItemTier tier, Pair<String, Integer> potionEffect) {
        List<Pair<String, Integer>> lis = new ArrayList<>();
        lis.add(potionEffect);
        messageSkill(s, skillName, tier, lis);
    }

    /**
     * SCOPlayer version of multiple potion effect
     * sword skill message
     */
    public static void messageSkill(SCOPlayer s, String skillName, ItemTier tier, List<Pair<String, Integer>> potionEffects) {
        String message = "";
        for(Pair<String, Integer> pair : potionEffects) {
            String temp = isEffectPositive(pair.getFirst()) ? "Recieved " : "Inflicted ";
            message += temp + pair.getFirst() + " for " + pair.getSecond() + " seconds, ";
        }
        message = message.substring(0, message.length() - 2) + ".";
        messageSkill(s, message, skillName, tier);
    }

    /**
     * SCOPlayer version of simple skill message
     * without relative item tier
     */
    public static void messageSkill(SCOPlayer s, String message, String skillName) {
        messageSkill(s, message, skillName, ItemTier.COMMON);
    }

    /**
     * SCOPlayer version of simple
     * sword skill message
     * 
     * Base line function. All other static
     * functions in skill announcer calls
     * this function
     */
    public static void messageSkill(SCOPlayer s, String message, String skillName, ItemTier tier) {
        if(!s.doesReceiveSkillMessages()) { return; }
        String prefix = ChatColor.DARK_RED + "[" + ItemTier.getTierColor(tier) + skillName + ChatColor.DARK_RED + "]";
        s.getPlayer().sendMessage(prefix + ChatColor.DARK_GRAY + message);
    }

    private static boolean isEffectPositive(String effect) {
        PotionEffectType type = PotionEffectType.getByName(effect);
        if(type == null) { return true; }

        return positives.contains(type);
    }

    private static List<PotionEffectType> positives = new ArrayList<>();
    static {
        positives.add(PotionEffectType.SPEED);
        positives.add(PotionEffectType.FAST_DIGGING);
        positives.add(PotionEffectType.INCREASE_DAMAGE);
        positives.add(PotionEffectType.HEAL);
        positives.add(PotionEffectType.JUMP);
        positives.add(PotionEffectType.REGENERATION);
        positives.add(PotionEffectType.DAMAGE_RESISTANCE);
        positives.add(PotionEffectType.FIRE_RESISTANCE);
        positives.add(PotionEffectType.WATER_BREATHING);
        positives.add(PotionEffectType.INVISIBILITY);
        positives.add(PotionEffectType.NIGHT_VISION);
        positives.add(PotionEffectType.HEALTH_BOOST);
        positives.add(PotionEffectType.ABSORPTION);
        positives.add(PotionEffectType.SATURATION);
        positives.add(PotionEffectType.GLOWING);
        positives.add(PotionEffectType.LUCK);
        positives.add(PotionEffectType.SLOW_FALLING);
        positives.add(PotionEffectType.CONDUIT_POWER);
        positives.add(PotionEffectType.DOLPHINS_GRACE);
        positives.add(PotionEffectType.HERO_OF_THE_VILLAGE);
    }
}