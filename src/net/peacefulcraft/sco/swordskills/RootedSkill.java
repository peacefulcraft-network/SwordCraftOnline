package net.peacefulcraft.sco.swordskills;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class RootedSkill extends SwordSkill {

    private int regenModifier;
    private ItemTier tier;

    public RootedSkill(SwordSkillCaster c, int regenModifier, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.regenModifier = regenModifier;
        this.tier = tier;

        this.listenFor(SwordSkillTrigger.PLAYER_MOVE);
    }

    @Override
    public boolean skillSignature(Event ev) {
        return true;
    }

    @Override
    public boolean skillPreconditions(Event ev) {
        ModifierUser mu = (ModifierUser)c;
        
        PlayerMoveEvent evv = (PlayerMoveEvent)ev;
        Location loc = evv.getPlayer().getLocation().clone().add(0, -1, 0);
        if(!loc.getBlock().getType().equals(Material.GRASS_BLOCK)) { 
            mu.getLivingEntity().removePotionEffect(PotionEffectType.REGENERATION);
            return false; 
        }

        if(mu.getLivingEntity().hasPotionEffect(PotionEffectType.REGENERATION)) {
            return false;
        }

        return true;
    }

    @Override
    public void triggerSkill(Event ev) {
        ModifierUser mu = (ModifierUser)c;
        mu.getLivingEntity().addPotionEffect(
            new PotionEffect(PotionEffectType.REGENERATION, 9999, regenModifier)
            );
        SkillAnnouncer.messageSkill(mu, "Regeneration " + regenModifier, "Rooted", tier);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
