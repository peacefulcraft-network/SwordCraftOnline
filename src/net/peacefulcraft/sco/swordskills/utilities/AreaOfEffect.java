package net.peacefulcraft.sco.swordskills.utilities;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.peacefulcraft.sco.swordskills.SwordSkill;

public class AreaOfEffect {

	private SwordSkill skill;
	private int range;
	
	public AreaOfEffect(SwordSkill skill) {
		this.skill = skill;
		range = skill.getProvider().getLevel();
	}
	
    private final int getRange() {
        return range;
    }

    public void execute(Player p, AreaOfEffectExecutor executor) {
        List<Entity> list = p.getNearbyEntities(getRange(), getRange(), getRange());
        for(Entity target : list) {
            if(target instanceof Player) { continue; }
            
        	double distanceToTarget = TargetUtils.getDistanceSq(p, target);
            executor.execute(skill, target, distanceToTarget);
        }
    }

    public interface AreaOfEffectExecutor {
    	void execute(SwordSkill skill, Entity target, double distanceToTarget);
    }
}
