package net.peacefulcraft.sco.swordskills.skills;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

public class BasicCombo extends SwordSkill implements ICombo {

    private SCOPlayer s;

    private boolean isActive = false;

    private Entity currentTarget = null;

    private Entity prevTarget;

    private Combo combo = null;

    public BasicCombo(SCOPlayer s) {
        super(s, -1, "Basic Combo");

        this.s = s;
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
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onHurtTarget(Player p, EntityDamageByEntityEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPlayerHurt(Player p, EntityDamageByEntityEvent e) {
        if(isComboInProgress() && ((Player) e.getEntity()) == p) {
            combo.endCombo(p);
        }
    }

    @Override
    public boolean skillSignature(Event ev) {
        if(!(s.getSkills().contains(this))) { return false; }

        return true;
    }

    @Override
    public void triggerSkill(Event ev) {
        // TODO Auto-generated method stub

    }

}