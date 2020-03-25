package net.peacefulcraft.sco.particles.effect;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import net.peacefulcraft.sco.particles.EffectManager;
import net.peacefulcraft.sco.particles.util.BaseImageEffect;

public class ColoredImageEffect extends BaseImageEffect {
    public ColoredImageEffect(EffectManager effectManager) {
        super(effectManager);
    }

    protected void display(BufferedImage image, Vector v, Location location, int pixel) {
        Color color = new Color(pixel);
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        display(particle, location.add(v), org.bukkit.Color.fromRGB(r, g, b));
        location.subtract(v);
    }
}