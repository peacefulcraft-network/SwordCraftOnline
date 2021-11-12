package net.peacefulcraft.sco.swordskills;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
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
import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;
import net.peacefulcraft.sco.utilities.RomanNumber;

public class GodsConditionHostilitySkill extends SwordSkill implements Runnable {

    private HashMap<Long, UUID> vicMap = new HashMap<>();

    private BukkitTask vicTask;
    private ItemTier tier;

    public GodsConditionHostilitySkill(SwordSkillCaster c, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.tier = tier;
        
        this.listenFor(SwordSkillTrigger.ENTITY_DAMAGE_ENTITY_GIVE);
        this.listenFor(SwordSkillTrigger.PLAYER_DEATH);
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
        if(ev instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent evv = (EntityDamageByEntityEvent)ev;
            ModifierUser mu = ModifierUser.getModifierUser(evv.getEntity());
            if(mu == null) { return; }
    
            if(vicMap.containsValue(mu.getLivingEntity().getUniqueId())) { return; }
            vicMap.put(System.currentTimeMillis() + 35000, mu.getLivingEntity().getUniqueId());
        } else if(ev instanceof PlayerDeathEvent) {
            PlayerDeathEvent evv = (PlayerDeathEvent)ev;
            
            Iterator<Entry<Long,UUID>> iter = vicMap.entrySet().iterator();
            while(iter.hasNext()) {
                Entry<Long, UUID> entry = iter.next();
                if(entry.getValue().equals(evv.getEntity().getUniqueId())) { iter.remove(); }
            }
        }
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {
        this.vicTask.cancel();
        Iterator<Entry<Long, UUID>> iter = vicMap.entrySet().iterator();
        while(iter.hasNext()) {
            Entry<Long, UUID> entry = iter.next();
            
            LivingEntity liv = (LivingEntity)Bukkit.getEntity(entry.getValue());
            liv.removePotionEffect(PotionEffectType.SLOW);
        }
        vicMap.clear();
    }

    @Override
    public void run() {
        Iterator<Entry<Long, UUID>> iter = vicMap.entrySet().iterator();
        while(iter.hasNext()) {
            Entry<Long, UUID> entry = iter.next();
            LivingEntity liv = (LivingEntity)Bukkit.getEntity(entry.getValue());

            // Checking if entry has been released from cooldown
            if(entry.getKey() <= System.currentTimeMillis()) {
                liv.removePotionEffect(PotionEffectType.SLOW);
                iter.remove();
                //SwordCraftOnline.logDebug("[Gods Condition: Hostility] Removed.");
            }
            ModifierUser mu = ModifierUser.getModifierUser(liv);
            if(mu == null) { continue; }

            if(liv.hasPotionEffect(PotionEffectType.SLOW)) { continue; }

            ItemIdentifier item = ItemIdentifier.resolveItemIdentifier(mu.getLivingEntity().getEquipment().getItemInMainHand());
            if(item == null || item.getMaterial().equals(Material.AIR) 
            || !(item instanceof WeaponAttributeHolder) 
            || !(item instanceof CustomDataHolder)) { 
                setSlow(liv);    
                continue; 
            }
            CustomDataHolder weapon = (CustomDataHolder)item;
            if(!weapon.getCustomData().get("weapon").getAsString().equalsIgnoreCase("sword")) { 
                setSlow(liv);
                continue; 
            }

            // Mob has valid sword in hand. Remove potion effect
            liv.removePotionEffect(PotionEffectType.SLOW);
        }
    }
    
    private void setSlow(LivingEntity liv) {
        ModifierUser mu = (ModifierUser)c;

        double mult = mu.getMultiplier(ModifierType.SPIRITUAL, false);
        int slowLvl = (int) Math.ceil(10 * mult);

        liv.addPotionEffect(new PotionEffect(
            PotionEffectType.SLOW,
            999999,
            slowLvl
        ));
        if(liv instanceof Player) {
            SCOPlayer s = GameManager.findSCOPlayer((Player)liv);
            if(s == null) { return; }
            SkillAnnouncer.messageSkill(
                s, 
                "You must now meet my condition, inflicted slowness " + RomanNumber.toRoman(slowLvl), 
                "Gods Condition: Hostility", 
                tier);
            //SwordCraftOnline.logDebug("[Gods Condition: Hostility] Inflicted.");
        }
    }
}
