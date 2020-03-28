package net.peacefulcraft.sco.swordskills;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.modules.TimedCombo;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.BasicCombo.SwordSkillComboType;

/**
 * SwordSkillTest
 */
public class SwordSkillTest extends SwordSkill {

    public SwordSkillTest(SwordSkillCaster c, SkillProvider provider) {
        super(c, provider);
        this.listenFor(SwordSkillTrigger.ENTITY_DAMAGE_ENTITY_GIVE);
        this.useModule(new TimedCooldown(5000L));
        this.useModule(new TimedCombo(this, SwordSkillComboType.CUMULATIVE_DAMAGE_WITHOUT_TAKING_DAMAGE, 10, 200L));
        s.getPlayer().sendMessage("[DEBUG] - SwordSkillTest Instantiated");
    }

    @Override
    public boolean skillSignature(Event ev) {
        s.getPlayer().sendMessage("[DEBUG] - SwordSkillTest Signature:");
        return true;
    }

    @Override
    public boolean skillPreconditions(Event ev) {
        if (this.s != null) {
            s.getPlayer().sendMessage("[DEBUG] - SwordSkillTest Preconditions Pass");
        }
        return true;
    }

    @Override
    public void triggerSkill(Event ev) {
        if (this.s != null) {
            s.getPlayer().sendMessage("[DEBUG] - SwordSkillTest Skill Triggered");
        }
    }

    @Override
    public void skillUsed() {
        if (this.s != null) {
            s.getPlayer().sendMessage("[DEBUG] - SwordSkillTest Skill Used");
        }
    }

    @Override
    public void unregisterSkill() {
        this.manager.unregisterListener(SwordSkillTrigger.PLAYER_INTERACT, this);
        if (this.s != null) {
            s.getPlayer().sendMessage("[DEBUG] - SwordSkillTest Skill Unregistered");
        }
    }
}