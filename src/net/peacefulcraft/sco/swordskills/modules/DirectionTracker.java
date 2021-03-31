package net.peacefulcraft.sco.swordskills.modules;

import org.bukkit.event.Event;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillTrigger;
import net.peacefulcraft.sco.swordskills.utilities.DirectionalUtil;

public class DirectionTracker implements SwordSkillModule {

    private SCOPlayer s;

    public DirectionTracker(SCOPlayer s) {
        this.s = s;
        DirectionalUtil.addPlayer(s);
    }

    @Override
    public void executeSupportLifecycle(SwordSkillTrigger type, SwordSkill ss, Event ev) {

    }

    @Override
    public boolean beforeSkillPreconditions(SwordSkill ss, Event ev) {
        return true;
    }

    @Override
    public boolean beforeTriggerSkill(SwordSkill ss, Event ev) {
        return true;
    }

    @Override
    public void afterTriggerSkill(SwordSkill ss, Event ev) {

    }

    @Override
    public void onUnregistration(SwordSkill ss) {
        DirectionalUtil.removePlayer(s);
    }
    
}
