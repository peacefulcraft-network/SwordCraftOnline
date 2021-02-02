package net.peacefulcraft.sco.swordskills;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.utilities.LocationUtil;

public class SystemDefenseThunderstruckSkill extends SwordSkill {

    private ItemTier tier;

    public SystemDefenseThunderstruckSkill(SwordSkillCaster c, ItemTier tier, SwordSkillProvider provider) {
        super(c, provider);
        this.tier = tier;
        
        this.useModule(new TimedCooldown(30000));
        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
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
        EntityInteractEvent evv = (EntityInteractEvent)ev;

        ModifierUser mu = ModifierUser.getModifierUser(evv.getEntity());
        if(mu == null) {
            SwordCraftOnline.logDebug("System Defense: Thunderstruck failed.");
            return;
        }
        List<Location> blocks = LocationUtil.getCylinderLocations(
            mu.getLivingEntity().getLocation(), 0, 7, true);
        for(Location loc : blocks) {
            mu.getLivingEntity().getLocation().getWorld().strikeLightning(loc);
        }

        if(tier.equals(ItemTier.ETHEREAL)) {
            mu.getLivingEntity().addPotionEffect(
                new PotionEffect(PotionEffectType.REGENERATION, 5, 2));
        }
        if(tier.equals(ItemTier.GODLIKE)) {
            mu.getLivingEntity().addPotionEffect(
                new PotionEffect(PotionEffectType.REGENERATION, 5, 3));
        }
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}