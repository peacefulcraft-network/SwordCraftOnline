package net.peacefulcraft.sco.swordskills;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.swordskills.modules.BasicCombo;
import net.peacefulcraft.sco.swordskills.modules.BasicCombo.SwordSkillComboType;

/**
 * SwordSkillTest
 */
public class SwordSkillTest extends SwordSkill {

    public SwordSkillTest(SwordSkillCaster c, SkillProvider provider) {
        super(c, provider);
    }

    @Override
    public void registerSkill() {
        this.manager.registerListener(SwordSkillType.PLAYER_INTERACT, this);
        useModule(new BasicCombo(SwordSkillComboType.CONSECUTIVE_HITS_WITHOUT_TAKING_DAMAGE, 4));
        s.getPlayer().sendMessage("[DEBUG] - SwordSkillTest Instantiated");
    }

    @Override
    public boolean skillSignature(Event ev) {
        s.getPlayer().sendMessage("[DEBUG] - SwordSkillTest Signature:");
        if (this.s != null) {
            s.getPlayer().sendMessage("Match");
        } else {
            s.getPlayer().sendMessage("No match");
        }
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
        this.manager.unregisterListener(SwordSkillType.PLAYER_INTERACT, this);
        if (this.s != null) {
            s.getPlayer().sendMessage("[DEBUG] - SwordSkillTest Skill Unregistered");
        }
    }
}