package net.peacefulcraft.sco.particles.effect;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.particles.Effect;
import net.peacefulcraft.sco.particles.EffectManager;
import net.peacefulcraft.sco.particles.EffectType;

/**
 * Creates parabolic arc between two locations.
 * Modifiers: Start/End Location, Height, Particle count, Target Player.
 */
public class ArcEffect extends Effect {

    /**
     * ParticleType of spawned particle
     */
    public Particle particle = Particle.FLAME;

    /**
     * Height of the arc in blocks
     */
    public float height = 2;

    /**
     * Particles per arc
     */
    public int particles = 100;

    /**
     * Internal counter
     */
    protected int step = 0;

    public ArcEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 1;
        iterations = 200;
    }

    @Override
    public void reset() {
        this.step = 0;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        Location target = getTarget();
        if (target == null) {
            cancel();
            SwordCraftOnline.logInfo("Attempted to spawn Arc Effect with invalid target location. Null.");
            return;
        }
        Vector link = target.toVector().subtract(location.toVector());
        float length = (float) link.length();
        float pitch = (float) (4 * height / Math.pow(length, 2));
        for (int i = 0; i < particles; i++) {
            Vector v = link.clone().normalize().multiply((float) length * i / particles);
            float x = ((float) i / particles) * length - length / 2;
            float y = (float) (-pitch * Math.pow(x, 2) + height);
            location.add(v).add(0, y, 0);
            display(particle, location);
            //getTargetPlayer().spawnParticle(this.particle, location, 1);
            location.subtract(0, y, 0).subtract(v);

            step++;
        }
    }

}