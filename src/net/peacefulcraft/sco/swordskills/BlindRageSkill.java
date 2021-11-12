package net.peacefulcraft.sco.swordskills;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;
import net.peacefulcraft.sco.utilities.Pair;

public class BlindRageSkill extends SwordSkill {

    private ItemTier tier;

    public BlindRageSkill(SwordSkillCaster c, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.tier = tier;
        
        this.useModule(new Trigger(SwordSkillType.SECONDARY, (ModifierUser)c));
        this.useModule(new TimedCooldown(25000, (ModifierUser)c, "Blind Rage", tier));
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

        double mult = mu.getMultiplier(ModifierType.PHYSICAL, false);
        int strengthLvl = (int) Math.ceil(2.0 * mult);
        int blindLvl = (int) Math.ceil(1.0 * mult);

        mu.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 300, strengthLvl));
        mu.getLivingEntity().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 300, blindLvl));

        List<Pair<String, Integer>> effects = new ArrayList<>();
        effects.add(new Pair<String, Integer>(PotionEffectType.INCREASE_DAMAGE.toString(), strengthLvl));
        effects.add(new Pair<String, Integer>(PotionEffectType.BLINDNESS.toString(), blindLvl));
        SkillAnnouncer.messageSkill(mu, "Blind Rage", tier, effects);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
