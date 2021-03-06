package net.peacefulcraft.sco.particles.effect;

import org.bukkit.Location;
import org.bukkit.Particle;

import net.peacefulcraft.sco.particles.Effect;
import net.peacefulcraft.sco.particles.EffectManager;
import net.peacefulcraft.sco.particles.EffectType;

/**
 * Creates music effect over location.
 */
public class MusicEffect extends Effect {
    public Particle particle = Particle.NOTE;

    /**
     * Radials to spawn next note.
     */
    public double radialsPerStep = Math.PI / 8;

    /**
     * Radius of circle above head
     */
    public float radius = .4f;

    /**
     * Current step. Works as a counter
     */
    protected float step = 0;

    public MusicEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        iterations = 400;
        period = 1;
    }

    @Override
    public void reset() {
        this.step = 0;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        location.add(0, 1.9f, 0);
        location.add(Math.cos(radialsPerStep * step) * radius, 0, Math.sin(radialsPerStep * step) * radius);

        display(particle, location);
        step++;
    }

}