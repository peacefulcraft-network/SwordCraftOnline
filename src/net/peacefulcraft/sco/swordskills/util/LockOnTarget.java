package net.peacefulcraft.sco.swordskills.util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface LockOnTarget {
    
    /**Returns true if player is targeting entity */
    public boolean isLockedOn();

    /**Returns entity currently locked on to, null if not */
    public Entity getCurrentTarget();

    public void setCurrentTarget(Player p, Entity entity);

    /**Should find and return the next valid target or null */
    public void getNextTarget(Player p);
}