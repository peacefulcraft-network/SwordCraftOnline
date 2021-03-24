package net.peacefulcraft.sco.swordskills;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser.CombatModifier;
import net.peacefulcraft.sco.utilities.Pair;

public class KillerSenseSkill extends SwordSkill {

    private int levelModifier;
    private ItemTier tier;

    public KillerSenseSkill(SwordSkillCaster c, int levelModifier, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.tier = tier;
        
        this.levelModifier = levelModifier;

        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
        this.useModule(new Trigger(SwordSkillType.SECONDARY));
        this.useModule(new TimedCooldown(35000, (ModifierUser)c, "Killer Sense", tier));
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
        
        int armorChange = -1 - levelModifier;
        double parryChange = -0.05;

        List<Pair<String,Double>> statPair = new ArrayList<>();
        statPair.add(new Pair<String, Double>(Attribute.GENERIC_ARMOR.toString(), (double)armorChange));
        statPair.add(new Pair<String, Double>(CombatModifier.PARRY_CHANCE.toString(), parryChange));

        for(Entity e : mu.getLivingEntity().getNearbyEntities(10, 10, 10)) {
            ModifierUser muu = ModifierUser.getModifierUser(e);
            if(muu == null) { continue; }

            muu.queueChange(
                Attribute.GENERIC_ARMOR, 
                armorChange, 
                7);
            muu.queueChange(
                CombatModifier.PARRY_CHANCE, 
                parryChange, 
                7);

            if(e instanceof Player) {
                SCOPlayer s = GameManager.findSCOPlayer((Player)e);
                if(s == null) { continue; }
                SkillAnnouncer.messageSkill(
                    s,
                    statPair,
                    "A killer is nearby.",
                    "Kiler Sense",
                    tier
                );
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
