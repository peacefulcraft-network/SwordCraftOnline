package net.peacefulcraft.sco.mythicmobs.adapters.abstracts.boss;

import java.util.HashMap;

import org.bukkit.boss.BarColor;

public class AbstractBarColor {
    private static HashMap<String, BarColor> colors;
    static {
        colors = new HashMap<String, BarColor>();
        colors.put("PINK", BarColor.PINK);
        colors.put("BLUE", BarColor.BLUE);
        colors.put("RED", BarColor.RED);
        colors.put("GREEN", BarColor.GREEN);
        colors.put("YELLOW", BarColor.YELLOW);
        colors.put("PURPLE", BarColor.PURPLE);
        colors.put("PINK", BarColor.PINK);
    }

    public static BarColor getBarColor(String name) {
        return colors.get(name.toUpperCase());
    }
}