package net.peacefulcraft.sco.particles.effect;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import net.peacefulcraft.sco.particles.EffectManager;
import net.peacefulcraft.sco.particles.EffectType;
import net.peacefulcraft.sco.particles.util.RandomUtils;

public class BleedEffect extends net.peacefulcraft.sco.particles.Effect {

    /**
     * Play the Hurt Effect for the Player
     */
    public boolean hurt = true;

    /**
     * Height of the blood spurt
     */
    public double height = 1.75;

    /**
     * Color of blood. Default is red (152)
     */
    public int color = 152;

    public BleedEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 4;
        iterations = 25;
    }

    @Override
    public void onRun() {
        // Location to spawn the blood-item.
        Location location = getLocation();
        location.add(0, RandomUtils.random.nextFloat() * height, 0);
        location.getWorld().playEffect(location, Effect.STEP_SOUND, color);

        Entity entity = getEntity();
        if (hurt && entity != null) {
            entity.playEffect(org.bukkit.EntityEffect.HURT);
        }
    }
}