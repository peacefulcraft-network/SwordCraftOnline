package net.peacefulcraft.sco.swordskills;

import java.util.UUID;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;

public class RedDawnSkill extends SwordSkill {

    private int levelModifier;
    private UUID healthChange;
    private SCOPlayer s;

    public RedDawnSkill(SwordSkillCaster c, int levelModifier, SwordSkillProvider provider) {
        super(c, provider);
        this.levelModifier = levelModifier;

        if(!(c instanceof SCOPlayer)) {
            throw new RuntimeException("Failed to register RedDawnSkill. Caster not SCOPlayer.");
        }

        this.s = (SCOPlayer)c;
        this.listenFor(SwordSkillTrigger.PASSIVE);
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
        double mult = mu.getMultiplier(ModifierType.MENTAL, false);

        healthChange = s.queueChange(
            (int)(s.getMaxHealth() * (1.2 + (0.1 * this.levelModifier) * s.getPlayerKills()) * mult), 
            -1);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {
        s.dequeueChange(healthChange);
    }
    
}
