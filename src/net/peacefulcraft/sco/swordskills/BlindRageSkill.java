package net.peacefulcraft.sco.swordskills;

import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class BlindRageSkill extends SwordSkill {

    public BlindRageSkill(SwordSkillCaster c, SwordSkillProvider provider) {
        super(c, provider);
        
        this.useModule(new Trigger(SwordSkillType.SECONDARY));
        this.useModule(new TimedCooldown(25000));
        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
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
        mu.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2, 15));
        mu.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1, 15));
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
