package net.peacefulcraft.sco.swordskills.utilities;

/**
 * Interface for any player or mob that implements
 * abstracted attribute modifiers instead of potion effects.
 * Intended use is to modify these values in sword skills instead
 * of using potion effects for more control when using timed effects.
 */
public interface IDamage {
    double getAttackDamage();
    void setAttackDamage(double mod, boolean multiply);

    double getMovementSpeed();
    void setMovementSpeed(double mod, boolean multiply);

    double getMaxHealth();
    void setMaxHealth(double mod, boolean multiply);

    double getAttackSpeed();
    void setAttackSpeed(double mod, boolean multiply);

    double getKnockResist();
    void setKnockResist(double mod, boolean multiply);

    double getArmor();
    void setArmor(double mod, boolean multiply);

    double getArmorToughness();
    void setArmorToughness(double mod, boolean multiply);
}