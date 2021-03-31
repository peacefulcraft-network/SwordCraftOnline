package net.peacefulcraft.sco.swordskills;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.items.CustomDataHolder;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;

public interface SwordSkillProvider extends ItemIdentifier, CustomDataHolder {

  /**
   * @return Sword Skill Type
   */
  public abstract SwordSkillType getType();

  /**
   * Intantiates the associated SwordSkill and registers it to the casters SwordSkillManager.
   * @return The registered SwordSkill.
   */
  public abstract SwordSkill registerSwordSkill(SwordSkillCaster caster);

  /**
   * Called in constructor
   * Sets all appropriate modifiers of skill
   */
  public abstract void setModifiers();

  /**
   * Check if a skill with the given long-form name exists (.getProviderName()).
   * @param providerName The .getProviderName() for the desired skill.
   * @return True if the requested skill exists.
   *         False if the requested skill does not exist.
   */
  public static boolean skillExists(String providerName) {
    try {
      Class.forName("net.peacefulcraft.sco.swordskills." + providerName);
      return true;
    } catch (ClassNotFoundException ex) {
      return false;
    }
  }

  /**
   * Checks if a skill with the given attributes exists.
   * @param shortName The short / base name for the skill.
   * @param tier The requested skill tier.
   * @return True if a skill matching the requested parameters exists.
   *         False if no such skill exists.
   */
  public static boolean skillExists(String shortName, ItemTier tier) {
    return SwordSkillProvider.skillExists(SwordSkillProvider.getProviderName(shortName, tier));
  }

  /**
   * Takes skill attributes and generates the ItemIdentifier and SwordSKillProvider's class names.
   * IE: Critical Strike tier Rare -> CriticalStrikeItemRare.
   * @param shortName The base name of the skill, IE: Critical Strike.
   * @param tier The desired skill tier.
   * @return The name of the ItemIdentifier and SwordSkillProvider classes.
   */
  public static String getProviderName(String shortName, ItemTier tier) {
    // Get item tier with first letter capitalized. IE: RARE -> Rare
    String tierString = tier.toString().substring(0, 1).toUpperCase() + tier.toString().toLowerCase().substring(1);

    return shortName.replaceAll(" ", "") + "Item" + tierString;
  }

  /**
   * Use a long-from configuration string, IE CriticalStrike-COMMON, to generate a SwordSkillProvider.
   * @param configString The long-form configuration string.
   * @return The requested SwordSkillProvider.
   */
  public static SwordSkillProvider generateProviderFromLongString(String configString) throws RuntimeException{
    try {
      String parts[] = configString.split("-");
      String name = parts[0].replaceAll(" ", "");
      ItemTier tier = ItemTier.valueOf(parts[1]);
    
      return SwordSkillProvider.generateProvider(name, tier);
    
    } catch(IndexOutOfBoundsException ex) {
      SwordCraftOnline.logSevere("Attempted to generate SwordSkillProvider with invalid long-form configuration string : " + configString);
    } catch (IllegalArgumentException ex) {
      SwordCraftOnline.logSevere("Attemplted to generate SwordSkillProvider from long-form config string with invalid item tier" + configString);
    }

    throw new RuntimeException();
  }

  /**
   * Generates a SwordSkillProvider instance when given a valid SwordSkillProvider longname
   * @param name The long-form name of the SwordSkillProvider class (from .getProviderName())
   * @return An instance of the requested SwordSkillProvider
   */
  public static SwordSkillProvider generateProvider(String name, ItemTier tier) throws RuntimeException {
    return (SwordSkillProvider) ItemIdentifier.generateIdentifier(name, tier, 1);
  }
}