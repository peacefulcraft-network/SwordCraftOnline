package net.peacefulcraft.sco.swordskills;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
import net.peacefulcraft.sco.swordskills.modules.Trigger;

public class GuardianSkill extends SwordSkill {

    public double armorModifier;

    public GuardianSkill(SwordSkillCaster c, double armorModifier, SwordSkillProvider provider) {
        super(c, provider);
        this.armorModifier = armorModifier;
        
        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
        this.useModule(new Trigger(SwordSkillType.SECONDARY));
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
        if(c instanceof SCOPlayer) {
            for(Entity entity : ((SCOPlayer)c).getLivingEntity().getNearbyEntities(10, 10, 10)) {
                if(entity instanceof Player) {
                    SCOPlayer s = GameManager.findSCOPlayer((Player)entity);
                    if(s == null) { continue; }
                    s.queueChange(Attribute.GENERIC_ARMOR, this.armorModifier, 20);
                }
            }
        } else if (c instanceof ActiveMob) {
            for(Entity entity : ((ActiveMob)c).getLivingEntity().getNearbyEntities(10, 10, 10)) {
                if(entity instanceof LivingEntity) {
                    ActiveMob am = SwordCraftOnline.getPluginInstance()
                        .getMobManager()
                        .getMobRegistry()
                        .get(entity.getUniqueId());
                    if(am == null) { continue; }
                    am.queueChange(Attribute.GENERIC_ARMOR, this.armorModifier, 20);
                }
            }
        }
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
