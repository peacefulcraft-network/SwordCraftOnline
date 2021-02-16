package net.peacefulcraft.sco.swordskills;

import org.bukkit.attribute.Attribute;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;

public class SoulsOfTheFallenSkill extends SwordSkill {

    private int levelModifier;

    public SoulsOfTheFallenSkill(SwordSkillCaster c, int levelModifier, SwordSkillProvider provider) {
        super(c, provider);
        this.levelModifier = levelModifier;

        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
        this.useModule(new Trigger(SwordSkillType.SWORD));
        this.useModule(new TimedCooldown(60000));
    }

    @Override
    public boolean skillSignature(Event ev) {
        return true;
    }

    @Override
    public boolean skillPreconditions(Event ev) {
        if(c instanceof SCOPlayer) { return true; }
        return false;
    }

    @Override
    public void triggerSkill(Event ev) {
        SCOPlayer s = (SCOPlayer)c;
        double damage = s.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        damage += ((0.2 + (0.1 * this.levelModifier)) * s.getPlayerKills());

        s.queueChange(
            Attribute.GENERIC_ATTACK_DAMAGE, 
            damage, 
            15);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
