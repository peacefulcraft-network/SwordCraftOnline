package net.peacefulcraft.sco.swordskills;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.gamehandle.announcer.Announcer;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser.CombatModifier;

public class KillerSenseSkill extends SwordSkill {

    private int levelModifier;

    public KillerSenseSkill(SwordSkillCaster c, int levelModifier, SwordSkillProvider provider) {
        super(c, provider);
        
        this.levelModifier = levelModifier;
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
        ModifierUser mu = (ModifierUser)c;
        
        for(Entity e : mu.getLivingEntity().getNearbyEntities(10, 10, 10)) {
            ModifierUser muu = ModifierUser.getModifierUser(e);
            if(muu == null) { continue; }

            muu.queueChange(
                Attribute.GENERIC_ARMOR, 
                -1 - levelModifier, 
                7);
            muu.queueChange(
                CombatModifier.PARRY_CHANCE, 
                -0.05, 
                7);

            if(e instanceof Player) {
                Player p = (Player)e;
                Announcer.messagePlayer(p, "A killer is nearby...", 0);
            }
        }
    }

    @Override
    public void skillUsed() {
        // TODO Auto-generated method stub

    }

    @Override
    public void unregisterSkill() {
        // TODO Auto-generated method stub

    }
    
}
