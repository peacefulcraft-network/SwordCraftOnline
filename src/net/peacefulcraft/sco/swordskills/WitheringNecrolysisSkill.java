package net.peacefulcraft.sco.swordskills;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class WitheringNecrolysisSkill extends SwordSkill {

    private int witherModifier;

    public WitheringNecrolysisSkill(SwordSkillCaster c, int witherModifier, SwordSkillProvider provider) {
        super(c, provider);
        this.witherModifier = witherModifier;

        this.listenFor(SwordSkillTrigger.ENTITY_DAMAGE_ENTITY_GIVE);
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
        EntityDamageByEntityEvent evv = (EntityDamageByEntityEvent)ev;
        ModifierUser mu = ModifierUser.getModifierUser(evv.getEntity());
        if(mu == null) { return; }

        mu.getLivingEntity().addPotionEffect(
            new PotionEffect(PotionEffectType.WITHER, witherModifier, 5)
        );
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
