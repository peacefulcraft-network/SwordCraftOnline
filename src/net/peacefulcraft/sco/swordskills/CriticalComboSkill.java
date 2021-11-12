package net.peacefulcraft.sco.swordskills;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.modules.BasicCombo;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.BasicCombo.SwordSkillComboType;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser.CombatModifier;

public class CriticalComboSkill extends SwordSkill {

    public CriticalComboSkill(SwordSkillCaster c, SwordSkillProvider provider) {
        super(c, provider);
        
        this.useModule(new BasicCombo(this, SwordSkillComboType.CONSECUTIVE_HITS_WITHOUT_TAKING_DAMAGE, 3));
        this.useModule(new TimedCooldown(15000));
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
        ModifierUser mu = (ModifierUser)c;
        double mult = mu.getMultiplier(ModifierType.LIGHTNING, false);

        mu.queueChange(
            CombatModifier.CRITICAL_CHANCE, 
            mu.getCombatModifier(CombatModifier.CRITICAL_CHANCE) * mult,
            4);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
