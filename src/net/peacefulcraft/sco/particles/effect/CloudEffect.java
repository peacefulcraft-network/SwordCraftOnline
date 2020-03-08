package net.peacefulcraft.sco.particles.effect;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import net.peacefulcraft.sco.particles.Effect;
import net.peacefulcraft.sco.particles.EffectManager;
import net.peacefulcraft.sco.particles.EffectType;
import net.peacefulcraft.sco.particles.util.RandomUtils;

public class CloudEffect extends Effect {

    /*
     * Particle of the cloud
     */
    public Particle cloudParticle = Particle.CLOUD;
    public Color cloudColor = null;

    /*
     * Particle of the rain/snow
     */
    public Particle mainParticle = Particle.DRIP_WATER;

    /*
     * Size of the cloud
     */
    public float cloudSize = .7f;

    /*
     * Radius of the rain/snow
     */
    public float particleRadius = cloudSize - .1f;

    /*
     * Y-Offset from location
     */
    public double yOffset = .8;

    public CloudEffect(EffectManager manager) {
        super(manager);
        type = EffectType.REPEATING;
        period = 5;
        iterations = 50;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        location.add(0, yOffset, 0);
        for (int i = 0; i < 50; i++) {
            Vector v = RandomUtils.getRandomCircleVector().multiply(RandomUtils.random.nextDouble() * cloudSize);
            display(cloudParticle, location.add(v), cloudColor, 0, 7);
            location.subtract(v);
        }
        Location l = location.add(0, .2, 0);
        for (int i = 0; i < 15; i++) {
            int r = RandomUtils.random.nextInt(2);
            double x = RandomUtils.random.nextDouble() * particleRadius;
            double z = RandomUtils.random.nextDouble() * particleRadius;
            l.add(x, 0, z);
            if (r != 1) {
                display(mainParticle, l);
            }
            l.subtract(x, 0, z);
            l.subtract(x, 0, z);
            if (r != 1) {
                display(mainParticle, l);
            }
            l.add(x, 0, z);
        }
    }

}