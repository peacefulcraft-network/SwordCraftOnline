package net.peacefulcraft.sco.swordskills;

import java.util.HashMap;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.modules.EffectCombo;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class ThiefKingsDemonLedgerComboSkill extends SwordSkill {

    private ItemTier tier;

    public ThiefKingsDemonLedgerComboSkill(SwordSkillCaster c, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.tier = tier;

        HashMap<Integer, PotionEffectType> effects = new HashMap<>();
        effects.put(1, PotionEffectType.WEAKNESS);
        effects.put(2, PotionEffectType.POISON);
        effects.put(3, PotionEffectType.WITHER);

        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
        this.listenFor(SwordSkillTrigger.ENTITY_DAMAGE_ENTITY_GIVE);

        this.useModule(new Trigger(SwordSkillType.SWORD, (ModifierUser)c));
        this.useModule(new EffectCombo(this, 3, effects, 4, 80, tier, "Thief Kings Demon Ledger Combo"));
        this.useModule(new TimedCooldown(45000, (ModifierUser)c, "Thief Kinds Demon Ledger Combo", tier, null, "PlayerInteractEvent"));
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
        if(ev == null || ev instanceof PlayerInteractEvent) { return; }
        EntityDamageByEntityEvent evv = (EntityDamageByEntityEvent)ev;
        evv.setDamage(evv.getDamage() * 3);

        ModifierUser mu = (ModifierUser)c;

        SkillAnnouncer.messageSkill(
            mu, 
            "Combo completed", 
            "Thief Kings Demon Ledger Combo", 
            tier);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
