package net.peacefulcraft.sco.swordskills.weaponskills;

import java.util.UUID;

import com.google.gson.JsonObject;

import org.bukkit.attribute.Attribute;

import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser.CombatModifier;
import net.peacefulcraft.sco.utilities.RomanNumber;

public class HigherKnowledge implements WeaponModifier {

    private String level;
    private UUID change1;
    private UUID change2;

    @Override
    public String getName() {
        return "Higher Knowledge";
    }

    @Override
    public Double getModifierAmount() {
        return 0.1 * RomanNumber.romanToDecimal(this.level);
    }

    @Override
    public String getLevel() {
        return this.level;
    }

    @Override
    public void applyEffects(ModifierUser user) {
        change1 = user.queueChange(Attribute.GENERIC_ATTACK_DAMAGE, getModifierAmount(), -1);
        change2 = user.queueChange(CombatModifier.PARRY_CHANCE, -getModifierAmount(), -1);
    }

    @Override
    public void removeEffects(ModifierUser user) {
        user.dequeueChange(change1);
        user.dequeueChange(change2);
    }

    @Override
    public Boolean canReforge() {
        return true;
    }

    @Override
    public Integer getMaxPlayerLevel() {
        return 6;
    }

    public HigherKnowledge(String level) {
        this.level = level;
    }

    @Override
    public JsonObject getModifiedStats() {
        JsonObject obj = new JsonObject();
        obj.addProperty(Attribute.GENERIC_ATTACK_DAMAGE.toString(), getModifierAmount());
        obj.addProperty(CombatModifier.PARRY_CHANCE.toString(), -getModifierAmount());
        return obj;
    }
    
}
