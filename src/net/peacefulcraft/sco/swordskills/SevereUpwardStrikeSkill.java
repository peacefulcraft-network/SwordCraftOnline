package net.peacefulcraft.sco.swordskills;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;

public class SevereUpwardStrikeSkill extends SwordSkill {

    private int cooldown;
    private double vectorMultiplier;

    public SevereUpwardStrikeSkill(SwordSkillCaster c, double vectorMultiplier, int cooldown, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.vectorMultiplier = vectorMultiplier;
        this.cooldown = cooldown;

        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
        this.listenFor(SwordSkillTrigger.ENTITY_DAMAGE_ENTITY_GIVE);

        this.useModule(new Trigger(SwordSkillType.SWORD, (ModifierUser)c));
        this.useModule(new TimedCooldown(this.cooldown, (ModifierUser)c, "Severe Upward Strike", tier,null, "PlayerInteractEvent"));
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
        ModifierUser mu = (ModifierUser)c;
        double mult = mu.getMultiplier(ModifierType.PHYSICAL, false);

        if(ev instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent evv = (EntityDamageByEntityEvent)ev;
            if(evv.getEntity() instanceof LivingEntity) {
                LivingEntity liv = (LivingEntity)evv.getEntity();
    
                liv.setVelocity(
                    liv.getLocation().getDirection()
                        .multiply(-1 * (2 + this.vectorMultiplier) * mult).add(new Vector(0, 5, 0))
                );
            }
        }
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
