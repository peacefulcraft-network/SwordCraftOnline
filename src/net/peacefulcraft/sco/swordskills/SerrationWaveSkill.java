package net.peacefulcraft.sco.swordskills;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlot;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.PlayerInventory;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.utilities.ItemAttribute;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;

public class SerrationWaveSkill extends SwordSkill {

    private double multiplier;
    private int cooldown;

    public SerrationWaveSkill(SwordSkillCaster c, double multiplier, int cooldown,SwordSkillProvider provider) {
        super(c, provider);
        this.multiplier = multiplier;
        this.cooldown = cooldown;

        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
        this.useModule(new TimedCooldown(this.cooldown));
    }

    @Override
    public boolean skillSignature(Event ev) {
        return true;
    }

    @Override
    public boolean skillPreconditions(Event ev) {
        if(this.c instanceof SCOPlayer) {
            SCOPlayer s = (SCOPlayer)this.c;

            PlayerInventory pi = s.getPlayerInventory();
            HashMap<String, Integer> weapons = pi.getHotbarWeapons();
            
            if(weapons.get("sword") == 0) { return false; }
        }
        return true;
    }

    @Override
    public void triggerSkill(Event ev) {
        List<Entity> entities = new ArrayList<Entity>();
        double damage = 1;
        if(this.c instanceof SCOPlayer) {
            damage = 6;
            SCOPlayer s = (SCOPlayer)this.c;
            ItemIdentifier weapon = s.getPlayerInventory().getHotbarWeapon("sword");
            if(weapon != null) {
                double tempDamage = ItemAttribute.getAttribute(weapon, Attribute.GENERIC_ATTACK_DAMAGE, EquipmentSlot.HAND);
                damage += tempDamage;
            }
            damage *= s.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
            entities = s.getPlayer().getNearbyEntities(5, 5, 5);

            for(Entity e : entities) {
                if(!(e instanceof LivingEntity)) { continue; }
    
                ActiveMob am = SwordCraftOnline.getPluginInstance()
                    .getMobManager()
                    .getMobRegistry()
                    .get(((LivingEntity)e).getUniqueId());

                am.multiplyAttribute(Attribute.GENERIC_MOVEMENT_SPEED, 0.8, 6);
                
                am.setHealth((int) (am.getHealth() - damage));
                am.updateHealthBar();
            }
        } else if(this.c instanceof ActiveMob) {
            ActiveMob am = (ActiveMob)this.c;
            damage *= am.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
            entities = am.getLivingEntity().getNearbyEntities(5, 5, 5);

            for(Entity e : entities) {
                if(!(e instanceof Player)) { continue; }
                SCOPlayer s = GameManager.findSCOPlayer((Player)e);
                if(s == null) { continue; }

                s.multiplyAttribute(Attribute.GENERIC_ATTACK_SPEED, 0.8, 6);
                s.setHealth((int) (s.getHealth() - damage));
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
