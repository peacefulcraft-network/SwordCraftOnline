package net.peacefulcraft.sco.swordskills;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.peacefulcraft.sco.swordskills.modules.BasicCombo;
import net.peacefulcraft.sco.swordskills.modules.BasicCombo.SwordSkillComboType;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;

public class PoorMansThirdStrikeSkill extends SwordSkill {

    public PoorMansThirdStrikeSkill(SwordSkillCaster c, SwordSkillProvider provider) {
        super(c, provider);

        this.useModule(new TimedCooldown(17000));
        this.useModule(new BasicCombo(this, SwordSkillComboType.CONSECUTIVE_HITS_WITHOUT_TAKING_DAMAGE, 3));

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
        evv.setDamage(evv.getDamage() * 1.8);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
