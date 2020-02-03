package net.peacefulcraft.sco.mythicmobs.adapters;

import java.util.HashMap;

import org.bukkit.Color;


public class BukkitColor {
    private static HashMap<String, Color> colors;
    static {
        colors = new HashMap<String, Color>();
        colors.put("AQUA", Color.AQUA);
        colors.put("BLACK", Color.BLACK);
        colors.put("BLUE", Color.BLUE);
        colors.put("FUCHSIA", Color.FUCHSIA);
        colors.put("GRAY", Color.GRAY);
        colors.put("GREEN", Color.GREEN);
        colors.put("LIME", Color.LIME);
        colors.put("MAROON", Color.MAROON);
        colors.put("NAVY", Color.NAVY);
        colors.put("OLIVE", Color.OLIVE);
        colors.put("ORANGE", Color.ORANGE);
        colors.put("PURPLE", Color.PURPLE);
        colors.put("RED", Color.RED);
        colors.put("SILVER", Color.SILVER);
        colors.put("TEAL", Color.TEAL);
        colors.put("WHITE", Color.WHITE);
        colors.put("YELLOW", Color.YELLOW);
    }

    /**Get Bukkit color from string */
    public static Color getColor(String color) {
        return colors.get(color.toUpperCase());
    }

    /**Decode color from hex */
    public static Color decode(String color) {
        int hex = 0x123456;
        int r = (hex & 0xFF0000) >> 16;
        int g = (hex & 0xFF00) >> 8;
        int b = (hex & 0xFF);
        return Color.fromBGR(r, g, b);
    }
}