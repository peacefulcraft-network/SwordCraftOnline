package net.peacefulcraft.sco.swordskills;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.CustomDataHolder;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.items.WeaponAttributeHolder;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class GodsConditionHostilitySkill extends SwordSkill implements Runnable {

    private HashMap<Long, LivingEntity> vicMap = new HashMap<>();

    private BukkitTask vicTask;
    private ItemTier tier;

    public GodsConditionHostilitySkill(SwordSkillCaster c, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.tier = tier;
        
        this.listenFor(SwordSkillTrigger.ENTITY_DAMAGE_ENTITY_GIVE);
        this.vicTask = Bukkit.getServer().getScheduler().runTaskTimer(
            SwordCraftOnline.getPluginInstance(),
            this,
            20,
            40
        );
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
        EntityDamageByEntityEvent evv = (EntityDamageByEntityEvent)ev;
        ModifierUser mu = ModifierUser.getModifierUser(evv.getEntity());
        if(mu == null) { return; }

        if(vicMap.containsValue(mu.getLivingEntity())) { return; }
        vicMap.put(System.currentTimeMillis() + 35000, mu.getLivingEntity());
        setSlow(mu.getLivingEntity());
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {
        this.vicTask.cancel();
        Iterator<Entry<Long, LivingEntity>> iter = vicMap.entrySet().iterator();
        while(iter.hasNext()) {
            Entry<Long, LivingEntity> entry = iter.next();
            entry.getValue().removePotionEffect(PotionEffectType.SLOW);
        }
        vicMap.clear();
    }

    @Override
    public void run() {
        Iterator<Entry<Long, LivingEntity>> iter = vicMap.entrySet().iterator();
        while(iter.hasNext()) {
            Entry<Long, LivingEntity> entry = iter.next();

            // Checking if entry has been released from cooldown
            if(entry.getKey() <= System.currentTimeMillis()) {
                entry.getValue().removePotionEffect(PotionEffectType.SLOW);
                iter.remove();
            }
            ModifierUser mu = ModifierUser.getModifierUser(entry.getValue());
            if(mu == null) { continue; }

            ItemIdentifier item = ItemIdentifier.resolveItemIdentifier(mu.getLivingEntity().getEquipment().getItemInMainHand());
            if(item == null || item.getMaterial().equals(Material.AIR) 
            || !(item instanceof WeaponAttributeHolder) 
            || !(item instanceof CustomDataHolder)) { 
                setSlow(entry.getValue());    
                continue; 
            }
            CustomDataHolder weapon = (CustomDataHolder)item;
            if(!weapon.getCustomData().get("weapon").getAsString().equalsIgnoreCase("sword")) { 
                setSlow(entry.getValue());
                continue; 
            }

            // Mob has valid sword in hand. Remove potion effect
            entry.getValue().removePotionEffect(PotionEffectType.SLOW);
        }
    }
    
    private void setSlow(LivingEntity liv) {
        liv.addPotionEffect(new PotionEffect(
            PotionEffectType.SLOW,
            999999,
            10
        ));
        if(liv instanceof Player) {
            SCOPlayer s = GameManager.findSCOPlayer((Player)liv);
            if(s == null) { return; }
            SkillAnnouncer.messageSkill(
                s, 
                "You must now meet my condition, inflicted slowness X", 
                "Gods Condition: Hostility", 
                tier);
        }
    }
}
