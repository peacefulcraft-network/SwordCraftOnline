package net.peacefulcraft.sco.swordskills;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;
import net.peacefulcraft.sco.utilities.LocationUtil;
import net.peacefulcraft.sco.utilities.Pair;

public class SystemDefenseThunderstruckSkill extends SwordSkill {

    private ItemTier tier;

    public SystemDefenseThunderstruckSkill(SwordSkillCaster c, ItemTier tier, SwordSkillProvider provider) {
        super(c, provider);
        this.tier = tier;
        
        this.useModule(new TimedCooldown(30000, (ModifierUser)c, "System Defense: Thunderstruck", tier));
        this.useModule(new Trigger(SwordSkillType.SECONDARY, (ModifierUser)c));
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
        ModifierUser mu = (ModifierUser)c;
        List<Location> blocks = LocationUtil.getCylinderLocations(
            mu.getLivingEntity().getLocation(), 
            1, 
            7, 
            true
        );

        double mult = mu.getMultiplier(ModifierType.LIGHTNING, false);

        for(Block b : LocationUtil.locationsToBlocks(blocks)) {
            for (double i = 0; i < Math.ceil(1 * mult); i++) {
                mu.getLivingEntity().getLocation().getWorld().strikeLightning(b.getLocation());
            }
        }

        if(tier.equals(ItemTier.ETHEREAL)) {
            mu.getLivingEntity().addPotionEffect(
                new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
            SkillAnnouncer.messageSkill(
                mu, 
                "System Defense: Thunderstruck",
                tier, 
                new Pair<String, Integer>(PotionEffectType.REGENERATION.toString(), 5));
        } else if(tier.equals(ItemTier.GODLIKE)) {
            mu.getLivingEntity().addPotionEffect(
                new PotionEffect(PotionEffectType.REGENERATION, 100, 3));
            SkillAnnouncer.messageSkill(
                mu, 
                "System Defense: Thunderstruck",
                tier, 
                new Pair<String, Integer>(PotionEffectType.REGENERATION.toString(), 5));
        }
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
