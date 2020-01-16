package net.peacefulcraft.sco.mythicmobs.adapters.abstracts.boss;

import java.util.HashMap;

import org.bukkit.boss.BarStyle;

public class AbstractBarStyle {
    private static HashMap<String, BarStyle> styles;
    static{
        styles = new HashMap<String, BarStyle>();
        styles.put("SOLID", BarStyle.SOLID);
        styles.put("SEGMENTED_6", BarStyle.SEGMENTED_6);
        styles.put("SEGMENTED_10", BarStyle.SEGMENTED_10);
        styles.put("SEGMENTED_12", BarStyle.SEGMENTED_12);
        styles.put("SEGMENTED_20", BarStyle.SEGMENTED_20);
    }

    public static BarStyle getBarStyle(String name) {
        return styles.get(name.toUpperCase());
    }
}