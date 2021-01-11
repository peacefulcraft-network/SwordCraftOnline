package net.peacefulcraft.sco.swordskills.weaponskills;

import org.bukkit.attribute.Attribute;

import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.utilities.RomanNumber;

public class EnhancedGuard implements WeaponModifier {

    private String level;

    @Override
    public String getName() {
        return "Enhanced Guard";
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
        user.addToAttribute(Attribute.GENERIC_ARMOR, getModifierAmount(), -1);
    }

    @Override
    public void removeEffects(ModifierUser user) {
        user.addToAttribute(Attribute.GENERIC_ARMOR, getModifierAmount(), -1);
    }

    @Override
    public Boolean canReforge() {
        return true;
    }

    @Override
    public Integer getMaxPlayerLevel() {
        return 5;
    }
    
}
