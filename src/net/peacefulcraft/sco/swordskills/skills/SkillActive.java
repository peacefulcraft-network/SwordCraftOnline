package net.peacefulcraft.sco.swordskills.skills;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

public abstract class SkillActive extends SkillBase {
    
    /**
     * Constructs the first instance of a skill and stores it in skill list
     * @param name Should not contain spaces
     */
    protected SkillActive(String name) {
        super(name, true);
    }

    protected SkillActive(SkillBase skill) {
        super(skill);
    }

    protected boolean allowUserActivation() {
        return true;
    }
    
    /**Returns true if this skill is currently active
     * Defined by child class
     */
    public abstract boolean isActive();

    /**Amount of exhaustion added to the player each time skill is used */
    protected abstract double getExhaustion();

    /**Return true automatically add exhaustion amount on activation */
    protected boolean autoAddExhaustion() {
        return true;
    }

    @Override
    protected void levelUp(SCOPlayer p) {}
    
    /**
     * Returns true if this skill can be used by the player
     * @return Default return true
     */
    public boolean canUse(SCOPlayer p) {
        return (level > 0);
    }
    
    /**
     * Called after skilled is activated.
     * 
     * Anything that needs to happen when skill is activated should be done here,
     * setting timers, etc.
     * @return Return true to have this skill added to currently active skills list
     */
    protected abstract boolean onActivated(World world, Player p);

    /**
     * Called when the skill is forcefully deactivated
     * @param world
     * @param p
     */
    protected abstract void onDeactivated(World world, Player p);

    /**
     * Called on player activating skill directly.
     * @return false if skill is not activated
     */
    public final boolean activate(World world, SCOPlayer p) {
        return (allowUserActivation() ? trigger(world, p, false) : false);
    }

    /**
     * Call to ensure skill is deactivated
     * @throws IllegalStateException
     */
    public final void deactivate(Player p) throws IllegalStateException {
        if(isActive()) {
            onDeactivated(p.getWorld(), p);
            if(isActive()) {
                //TODO:Log skill still active
            }
        }
    }

    public final boolean trigger(World world, SCOPlayer SCOp, boolean wasTriggered) {
        Player p = SCOp.getPlayer();
        if(canUse(SCOp)) {
            if(autoAddExhaustion() && p.getGameMode() != GameMode.CREATIVE) {
                //TODO:Fix addExhaustion
                //p.addExhaustion(getExhaustion());
            }
            return onActivated(world, SCOp.getPlayer());
        } else {
            return false;
        }
    }
}