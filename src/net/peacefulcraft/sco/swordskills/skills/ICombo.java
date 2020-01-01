package net.peacefulcraft.sco.swordskills.skills;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public interface ICombo {

    /** Returns the combo instance for class */
    public Combo getCombo();

    /** Assign instance of Combo retrieved from getCombo() to argument combo */
    public void setCombo(Combo combo);

    /**Returns true if a combo is currently in progress */
    public boolean isComboInProgress();

    /**
     * Called every time player attacks
     * If a combo should end when the player misses, this is the place to handle that.
     * @return true if the attack should continue as normal
     */
    public boolean onAttack(Player p);

    /**
     * Called when Player actively using a combo damages an entity. Create
     * new combo if necessary. Use combo.add() or combo.addDamageOnly().
     */
    public void onHurtTarget(Player p, EntityDamageByEntityEvent e);

    /**
     * Called when Player using combo recieves damage. Useful for ending a combo
     * when damage exceeds threshold.
     */
    public void onPlayerHurt(Player p, EntityDamageByEntityEvent e);
}