package net.peacefulcraft.sco.particles.effect;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import net.peacefulcraft.sco.particles.Effect;
import net.peacefulcraft.sco.particles.EffectManager;
import net.peacefulcraft.sco.particles.EffectType;
import net.peacefulcraft.sco.particles.util.RandomUtils;

/**
 * Creates dome around location.
 * Modifiers: particle type, radius, particles to displau,
 * sphere.
 */
public class ShieldEffect extends Effect {

    /**
     * ParticleType of spawned particle
     */
    public Particle particle = Particle.FLAME;

    /**
     * Radius of the shield
     */
    public double radius = 3;

    /**
     * Particles to display
     */
    public int particles = 50;

    /**
     * Set to false for a half-sphere and true for a complete sphere
     */
    public boolean sphere = false;

    public ShieldEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        iterations = 500;
        period = 1;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        for (int i = 0; i < particles; i++) {
            Vector vector = RandomUtils.getRandomVector().multiply(radius);
            if (!sphere) {
                vector.setY(Math.abs(vector.getY()));
            }
            location.add(vector);
            display(particle, location);
            location.subtract(vector);
        }
    }

}