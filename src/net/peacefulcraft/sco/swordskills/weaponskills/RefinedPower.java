package net.peacefulcraft.sco.swordskills.weaponskills;

import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser.CombatModifier;
import net.peacefulcraft.sco.utilities.RomanNumber;

public class RefinedPower implements WeaponModifier {

    private String level;

    private Boolean canReforge;

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
    public ModifierType getModifierType() {
        return null;
    }

    @Override
    public CombatModifier getCombatModifierType() {
        return CombatModifier.CRITICAL_MULTIPLIER;
    }

    @Override
    public Boolean getModifierIncoming() {
        return false;
    }

    @Override
    public void applyEffects(ModifierUser user) {
        user.addToCombatModifier(getCombatModifierType(), getModifierAmount(), -1);
    }

    @Override
    public void removeEffects(ModifierUser user) {
        user.addToCombatModifier(getCombatModifierType(), -getModifierAmount(), -1);
    }

    @Override
    public Boolean canReforge() {
        return this.canReforge;
    }

    @Override
    public void setReforge(Boolean can) {
        this.canReforge = can;
    }

    public RefinedPower(String level) {
        this.level = level;
        this.canReforge = false;
    }
}
