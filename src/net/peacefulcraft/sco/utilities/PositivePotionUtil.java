package net.peacefulcraft.sco.utilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.potion.PotionEffectType;

/**
 * Utility class that determines if potion effect
 * is positive or negative
 */
public class PositivePotionUtil {
    
    private static boolean isEffectPositive(PotionEffectType effect) {
        return positives.contains(effect);
    }

    public static boolean isEffectPositive(String effect) {
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
