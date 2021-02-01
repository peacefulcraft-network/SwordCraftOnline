package net.peacefulcraft.sco.swordskills;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class SwiftSwimSkill extends SwordSkill {

    private boolean rainBoosted;

    public SwiftSwimSkill(SwordSkillCaster c, SwordSkillProvider provider) {
        super(c, provider);
        
        this.listenFor(SwordSkillTrigger.PLAYER_MOVE);
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
        PlayerMoveEvent evv = (PlayerMoveEvent)ev;
        ModifierUser mu = ModifierUser.getModifierUser(evv.getPlayer());
        if(mu == null) { return; }

        Location loc = evv.getTo();
        if(loc.getWorld().hasStorm() && !rainBoosted) {
            mu.addToAttribute(
                Attribute.GENERIC_MOVEMENT_SPEED, 
                ModifierUser.getBaseGenericMovement(mu) * 0.2, 
                -1);
            rainBoosted = true;
        } else if(!loc.getWorld().hasStorm() && rainBoosted) {
            mu.addToAttribute(
                Attribute.GENERIC_MOVEMENT_SPEED, 
                -(ModifierUser.getBaseGenericMovement(mu) * 0.2), 
                -1);
            rainBoosted = false;
        }
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
