package net.peacefulcraft.sco.swordskills;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.utilities.ItemAttribute;

public class SimplePunchSkill extends SwordSkill {

    private double damage;

    public SimplePunchSkill(SwordSkillCaster c, SwordSkillProvider provider) {
        super(c, provider);

        this.listenFor(SwordSkillTrigger.PASSIVE);
        this.listenFor(SwordSkillTrigger.ENTITY_DAMAGE_ENTITY_GIVE);
    }

    @Override
    public boolean skillSignature(Event ev) {
        return true;
    }

    @Override
    public boolean skillPreconditions(Event ev) {
        if(c instanceof SCOPlayer) {
            SCOPlayer s = (SCOPlayer)c;
            ItemIdentifier sword = s.getPlayerInventory().getHotbarWeapon("sword");
            ItemIdentifier knife = s.getPlayerInventory().getHotbarWeapon("knife");
            if(sword != null && !sword.getMaterial().equals(Material.AIR)) {
                double attrDamage = ItemAttribute.getAttribute(sword, Attribute.GENERIC_ATTACK_DAMAGE);
                if(attrDamage > 0) {
                    this.damage = 0.7 * attrDamage;
                    return true;
                }
            } else if(knife != null && !knife.getMaterial().equals(Material.AIR)) {
                double attrDamage = ItemAttribute.getAttribute(knife, Attribute.GENERIC_ATTACK_DAMAGE);
                if(attrDamage > 0) {
                    this.damage = 0.7 * attrDamage;
                    return true;
                }
            }
            return false;
        } 
        return false;
    }

    @Override
    public void triggerSkill(Event ev) {
        EntityDamageByEntityEvent evv = (EntityDamageByEntityEvent)ev;
        evv.setDamage(this.damage);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
