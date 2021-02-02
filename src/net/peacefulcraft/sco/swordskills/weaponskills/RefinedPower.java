package net.peacefulcraft.sco.swordskills.weaponskills;

import java.util.UUID;

import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser.CombatModifier;
import net.peacefulcraft.sco.utilities.RomanNumber;

public class RefinedPower implements WeaponModifier {

    private String level;
    private UUID change1;

    @Override
    public String getName() {
        return "Refined Power";
    }

    @Override
    public String getLevel() {
        return this.level;
    }

    @Override
    public Double getModifierAmount() {
        return 0.2 * RomanNumber.romanToDecimal(this.level) + 0.2;
    }

    @Override
    public void applyEffects(ModifierUser user) {
        change1 = user.queueChange(CombatModifier.CRITICAL_MULTIPLIER, getModifierAmount(), -1);
    }

    @Override
    public void removeEffects(ModifierUser user) {
        user.dequeueChange(change1);
    }

    @Override
    public Boolean canReforge() {
        return true;
    }

    @Override
    public Integer getMaxPlayerLevel() {
        return 5;
    }

    public RefinedPower(String level) {
        this.level = level;
    }
}
