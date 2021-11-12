package net.peacefulcraft.sco.swordskills;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;
import net.peacefulcraft.sco.utilities.Pair;

public class SwiftSwimSkill extends SwordSkill {

    private ItemTier tier;
    private boolean rainBoosted;
    private UUID change1;

    public SwiftSwimSkill(SwordSkillCaster c, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.tier = tier;
        
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

        double mult = mu.getMultiplier(ModifierType.WATER, false);

        Location loc = evv.getTo();
        if(loc.getWorld().hasStorm() && !rainBoosted) {
            change1 = mu.queueChange(
                Attribute.GENERIC_MOVEMENT_SPEED, 
                ModifierUser.getBaseGenericMovement(mu) * 0.2 * mult, 
                -1);
            rainBoosted = true;
            SkillAnnouncer.messageSkill(
                (SCOPlayer)mu, 
                new Pair<String, Double>(Attribute.GENERIC_MOVEMENT_SPEED.toString(), 3.0),
                "Swift Swim", 
                tier);
        } else if(!loc.getWorld().hasStorm() && rainBoosted) {
            mu.dequeueChange(change1);
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
