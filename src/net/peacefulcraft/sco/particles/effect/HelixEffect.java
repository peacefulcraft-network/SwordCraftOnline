package net.peacefulcraft.sco.particles.effect;

import org.bukkit.Location;
import org.bukkit.Particle;

import net.peacefulcraft.sco.particles.Effect;
import net.peacefulcraft.sco.particles.EffectManager;
import net.peacefulcraft.sco.particles.EffectType;

public class HelixEffect extends Effect {

    /**
     * Particle to form the helix
     */
    public Particle particle = Particle.FLAME;

    /**
     * Amount of strands
     */
    public int strands = 8;

    /**
     * Particles per strand
     */
    public int particles = 80;

    /**
     * Radius of helix
     */
    public float radius = 10;

    /**
     * Factor for the curves. Negative values reverse rotation.
     */
    public float curve = 10;

    /**
     * Rotation of the helix (Fraction of PI)
     */
    public double rotation = Math.PI / 4;

    public HelixEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 10;
        iterations = 8;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        for (int i = 1; i <= strands; i++) {
            for (int j = 1; j <= particles; j++) {
                float ratio = (float) j / particles;
                double angle = curve * ratio * 2 * Math.PI / strands + (2 * Math.PI * i / strands) + rotation;
                double x = Math.cos(angle) * ratio * radius;
                double z = Math.sin(angle) * ratio * radius;
                location.add(x, 0, z);
                display(particle, location);
                location.subtract(x, 0, z);
            }
        }
    }

}