package net.peacefulcraft.sco.particles.math;

import org.bukkit.configuration.ConfigurationSection;

public class EchoTransform implements Transform {
    @Override
    public void load(ConfigurationSection parameters) {
    }

    @Override
    public double get(double t) {
        return t;
    }
}