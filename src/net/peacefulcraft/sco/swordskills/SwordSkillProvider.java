package net.peacefulcraft.sco.swordskills;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
   * @return Allowed levels for the sword skill
   */
  public abstract Integer[] getAllowedLevels();

  /**
   * @return Sword skill level
   */
  public abstract Integer getLevel();

  /**
   * Intantiates the associated SwordSkill and registers it to the casters SwordSkillManager.
   * @return The registered SwordSkill.
   */
  public abstract SwordSkill registerSwordSkill(SwordSkillCaster caster);

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
      ItemTier tier = ItemTier.valueOf(parts[2]);
    
      return SwordSkillProvider.generateProvider(name, tier);
    
    } catch(IndexOutOfBoundsException ex) {
      SwordCraftOnline.logSevere("Attempted to generate SwordSkillProvider with invalid long-form configuration string : " + configString);
    } catch (IllegalArgumentException ex) {
      SwordCraftOnline.logSevere("Attemplted to generate SwordSkillProvider from long-form config string with invalid item tier" + configString);
    }

    throw new RuntimeException();
  }

  /**
   * Generates a SwordSkillProvider instance when given attributes of a valid SwordSkill.
   * @param shortName The shortname / base name of the skill IE: Critial Strike
   * @param tier the requested ItemTier
   * @return An instance of the requested SwordSkillprovider
   */
  public static SwordSkillProvider generateProvider(String shortName, ItemTier tier) throws RuntimeException {
    return SwordSkillProvider.generateProvider(SwordSkillProvider.getProviderName(shortName, tier));
  }

  /**
   * Generates a SwordSkillProvider instance when given a valid SwordSkillProvider longname
   * @param name The long-form name of the SwordSkillProvider class (from .getProviderName())
   * @return An instance of the requested SwordSkillProvider
   */
  public static SwordSkillProvider generateProvider(String name) throws RuntimeException {
    try {
      Class<?> classs = Class.forName("net.peacefulcraft.sco.swordskills." + name);
      Constructor<?> constructor = classs.getConstructor();
      
      return ((SwordSkillProvider) constructor.newInstance());

		} catch (ClassNotFoundException e) {
			SwordCraftOnline.logSevere("Attempted to create item " + name + ", but no coresponding class was found in net.peacefulcraft.sco.items");
		} catch (NoSuchMethodException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + name + " must have a constuctor with arguments (int, ItemTier)");
		} catch (SecurityException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + name + " does not have a public constructor.");
		} catch (InstantiationException | InvocationTargetException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + name + " generated exception during reflective instantiation:");
		} catch (IllegalAccessException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + name + " is an abstract class and cannot be instantiated.");
		} catch (IllegalArgumentException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + name + " received an invalid arguement type during insantiation. Arguements must be of type (int, ItemTier).");
		} catch (ClassCastException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + name + " must implement SwordSkillProvider.");
    }

    throw new RuntimeException();
  }
}