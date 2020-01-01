package net.peacefulcraft.sco.swordskills.skills;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Self-contained module containing all data necessary to track player's current
 * attack combo.
 * 
 * Upon combo ending, player gains the specificed type of experience in an
 * damage equal to the combo size minus one, plus an additional damage
 * proportional to the total damage dealt.
 * 
 * A new instance should be used for each new attack combo.
 */
public class Combo {
    
    /**Used to get correct Skill class from player */
    private final int skillId;
        public int getSkill() { return skillId; }

    /**Max combo size attainable by this instance of combo */
    private final int maxComboSize;
        public int getMaxNumHits() { return maxComboSize; }

    /**Upon initial hit, combo timer is set to this damage */
    private final int timeLimit;

    /**Current combo timer; combo ends when timer reaches zero. */
    private int comboTimer = 0;

    /**Set true when endCombo method called */
    private boolean isFinished = false;
        public boolean isFinished() { return isFinished; }

    /**List stores each hit's damage; combo size is inherent in the list */
    private final List<Double> damageList = new ArrayList<Double>();
        public int getNumHits() { return damageList.size(); }
        public List<Double> getDamageList() { return Collections.unmodifiableList(damageList); }

    /**Total damage inflicted during a combo */
    private double comboDamage = 0.0D;
        public double getDamage() { return comboDamage; }

    /**Last entity hit; use to check if consecutive hit counter inc or not. */
    private Entity lastEntityHit = null;
        public Entity getLastEntityHit() { return lastEntityHit; }

    /**Total number of consecutive hits on same entity */
    private int consectutiveHits = 0;
        public int getConsecutiveHits() { return consectutiveHits; }

    /**
     * New combo with specified combo size and time limit.
     */
    public Combo(Player p, SkillBase skill, int maxComboSize, int timeLimit) {
        this.skillId = skill.getId();
        this.maxComboSize = maxComboSize;
        this.timeLimit = timeLimit;
    }

    /**
     * Constructs new Combo with specified max combo size and time limit.
     */
    private Combo(int skillId, int maxComboSize, int timelimit) {
        this.skillId = skillId;
        this.maxComboSize = maxComboSize;
        this.timeLimit = timelimit;
    }

    /**Updates combo timer and triggers combo ending if timer hits 0 */
    public void onUpdate(Player p) {
        if(comboTimer > 0) {
            --comboTimer;
            if(comboTimer == 0) {
                endCombo(p);
            }
        }
    }

    /**
     * Increases combo size by one and adds the damage to the running total,
     * ending combo if the max size is reached.
     * @param target used to track consecutive hits on a single target
     */
    public void add(Player p, Entity target, double damage) {
        if(getNumHits() < maxComboSize && (comboTimer > 0 || getNumHits() == 0)) {
            if(target != null && target == lastEntityHit) {
                ++consectutiveHits;
            } else {
                lastEntityHit = target;
                consectutiveHits = (target != null ? 1 : 0);
            }
            damageList.add(damage);
            comboDamage += damage;
            if(getNumHits() == maxComboSize) {
                endCombo(p);
            } else {
                comboTimer = timeLimit;
            }
        } else {
            endCombo(p);
        }
    }

    /**Adds damage to combos total, without incrementing combo size */
    public void addDamageOnly(Player p, double damage) {
        if(!isFinished) {
            comboDamage += damage;
            if(getNumHits() == 0) {
                comboTimer = timeLimit;
            }
        }
    }

    /**Ends combo and grants xp */
    public void endCombo(Player p) {
        if(!isFinished) {
            isFinished = true;
            lastEntityHit = null;
            consectutiveHits = 0;
            //TODO:Grant xp methods
        }
    }
}