package net.peacefulcraft.sco.swordskills;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class BackPedalSkill extends SwordSkill {

    private Double vectorMultiplier;
    private ItemTier tier;

    public BackPedalSkill(SwordSkillCaster c, Double vectorMultiplier, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.vectorMultiplier = vectorMultiplier;
        this.tier = tier;

        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
        this.listenFor(SwordSkillTrigger.ENTITY_DAMAGE_ENTITY_GIVE);

        this.useModule(new Trigger(SwordSkillType.SWORD));
        this.useModule(new TimedCooldown(10000, (ModifierUser)c, "Back Pedal", this.tier, null, "PlayerInteractEvent"));
    }

    @Override
    public boolean skillSignature(Event ev) {
        return true;
    }

    @Override
    public boolean skillPreconditions(Event ev) {
        return true;
    }

    @Override
    public void triggerSkill(Event ev) {
        if(ev instanceof EntityDamageByEntityEvent) {
            ModifierUser mu = (ModifierUser)c;
            mu.getLivingEntity().setVelocity(
                mu.getLivingEntity().getLocation().getDirection()
                    .multiply(-1 * vectorMultiplier).add(new Vector(0, 0.1, 0))
            );
        }
    }

    @Override
    public void skillUsed() {
        // No need to mark
    }

    @Override
    public void unregisterSkill() {

    }
    
}
