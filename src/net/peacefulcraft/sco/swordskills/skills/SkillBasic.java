package net.peacefulcraft.sco.swordskills.skills;

import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.Util.TargetUtils;
import net.peacefulcraft.sco.swordskills.skills.Combo;
import net.peacefulcraft.sco.swordskills.skills.ICombo;
import net.peacefulcraft.sco.swordskills.skills.LockOnTarget;
import net.peacefulcraft.sco.swordskills.skills.SkillActive;

/**
 * Foundational skill for all other skills
 */
public class SkillBasic extends SkillActive implements ICombo, LockOnTarget {
    private boolean isActive = false;

    private Entity currentTarget = null;

    private Entity prevTarget;

    /** Set to a new instance each time a combo begins */
    private Combo combo = null;

    public SkillBasic(String name) {
        super(name);
    }

    private SkillBasic(SkillBasic skill) {
        super(skill);
    }

    private final int getRange() {
        return (6 + level);
    }

    private final int getComboTimeLimit() {
        return (20 + (level * 2));
    }

    private final int getMaxComboSize() {
        return (2 + level);
    }

    @Override
    public SkillBasic newInstance() {
        return new SkillBasic(this);
    }

    @Override
    public boolean isLockedOn() {
        return currentTarget != null;
    }

    @Override
    public Entity getCurrentTarget() {
        return currentTarget;
    }

    @Override
    public void setCurrentTarget(Player p, Entity entity) {
        if(entity instanceof Entity) {
            currentTarget = entity;
        } else {
            deactivate(p);
        }
    }

    @Override
    public void getNextTarget(Player p) {
        Entity nextTarget = null;
        double dTarget = 0;
        List<Entity> list = p.getNearbyEntities(getRange(), getRange(), getRange());
        for(Entity entity : list) {
            if(entity instanceof Player) { continue; }
            if(entity != currentTarget && entity != prevTarget) {
                if(nextTarget == null) {
                    dTarget = TargetUtils.getDistanceSq(p, entity);
                    nextTarget = entity;
                } else {
                    double distance = TargetUtils.getDistanceSq(p, entity);
                    if(distance < dTarget) {
                        nextTarget = entity;
                        dTarget = distance;
                    }
                }
            }
        }
        if(nextTarget != null) {
            prevTarget = currentTarget;
            currentTarget = nextTarget;
        } else {
            nextTarget = currentTarget;
            currentTarget = prevTarget;
            prevTarget = nextTarget;
        }
    }

    private boolean isTargetValid(Player p, Entity target) {
        return (target != null && !target.isDead() && ((LivingEntity) target).getHealth() > 0F && 
        TargetUtils.getDistanceSq(p, target) < getRange());
    }

    private boolean updateTargets(Player p) {
        if(!isTargetValid(p, prevTarget)) {
            prevTarget = null;
        }
        if(!isTargetValid(p, currentTarget)) {
            currentTarget = null;
            if(SwordCraftOnline.getSCOConfig().autoTargetEnabled()) {
                getNextTarget(p);
            }
        }
        return isTargetValid(p, currentTarget);
    }

    @Override
    public Combo getCombo() {
        return combo;
    }

    @Override
    public void setCombo(Combo combo) {
        this.combo = combo;
    }

    @Override
    public boolean isComboInProgress() {
        return (combo != null && !combo.isFinished());
    }

    @Override
    public boolean onAttack(Player p) {
        Entity mouseOver = TargetUtils.getMouseOver(p);
        return isLockedOn() && mouseOver != null && TargetUtils.canReachTarget(p, mouseOver);
    }

    @Override
    public void onHurtTarget(Player p, EntityDamageByEntityEvent e) {
        if(!isLockedOn()) { return; }
        if(combo == null || combo.isFinished()) {
            combo = new Combo(p, this, getMaxComboSize(), getComboTimeLimit());
        }
        
    }

    /*
    private boolean isValidComboDamage(Player p, EntityDamageByEntityEvent e) {
        
    }*/

    @Override
    public void onPlayerHurt(Player p, EntityDamageByEntityEvent e) {
        //TODO:Check damage recieved by event is greater than 0.5D * level
        if(isComboInProgress()) {
            combo.endCombo(p);
        }
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    protected double getExhaustion() {
        return 0.0;
    }

    @Override
    protected boolean onActivated(World world, Player p) {
        if(isActive) {
            onDeactivated(world, p);
        } else {
            isActive = true;
            if(!isComboInProgress()) {
                combo = null;
            }
            currentTarget = TargetUtils.acquireLookTarget(p, getRange(), getRange(), true);
        }
        return true;
    }

    @Override
    protected void onDeactivated(World world, Player p) {
        isActive = false;
        currentTarget = null;
        //TODO: if world isRemote prevTarget null
    }

    @Override
    protected void onUpdate(Player p) {
        if (isActive()) {
            if (!updateTargets(p)) {
                deactivate(p);
            }
        }
        if (isComboInProgress()) {
            combo.onUpdate(p);
        }
    }
    
}