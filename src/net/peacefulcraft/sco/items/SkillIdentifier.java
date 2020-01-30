package net.peacefulcraft.sco.items;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.swordskills.SkillProvider;

/**
 * Item that unique identifies a SwordSkill.
 * Can be used to instantiate a SwordSkillProvider in package net.peacefulcraft.sco.items
 */
public class SkillIdentifier{
	
	private String skillName;
		public String getSkillName() { return skillName; }
	private int skillLevel;;
		public int getSkillLevel() { return skillLevel; }
	private ItemTier rarity;
		public ItemTier getRarity() { return rarity; }
	
	/**
	 * Optional value used when generating inventories from the database
	 */
	private int loc;
		public int getInventoryLocation(){ return loc; }
		public void setInventoryLocation(int loc) { this.loc = loc; }
		
	public SkillIdentifier(String skillName, int skillLevel, ItemTier rarity) {
		this.skillName = skillName;
		this.skillLevel = skillLevel;
		this.rarity = rarity;
	}
	
	public SkillIdentifier(String skillName, int skillLevel, ItemTier rarity, int loc) {
		this.skillName = skillName;
		this.skillLevel = skillLevel;
		this.rarity = rarity;
		this.loc = loc;
	}
	
	public SkillProvider getProvider() throws RuntimeException{
		try {
			
			// Instantiate the provider
			skillName = skillName.replaceAll(" ", "") + "Item";
			Class<?> classs = Class.forName("net.peacefulcraft.sco.items." + skillName);
			Constructor<?> constructor = classs.getConstructor(int.class, ItemTier.class);
			Object[] args = { skillLevel, rarity };
			
			// Add to provider list to setup skills below
			SkillProvider provider = (SkillProvider) constructor.newInstance(args);
			return provider;
			
		} catch (ClassNotFoundException e) {
			SwordCraftOnline.logSevere("Attempted to create item " + skillName + ", but no coresponding class was found in net.peacefulcraft.sco.items");
		} catch (NoSuchMethodException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + skillName + " must have a constuctor with arguments (int, ItemTier)");
		} catch (SecurityException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + skillName + " does not have a public constructor.");
		} catch (InstantiationException | InvocationTargetException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + skillName + " generated exception during reflective instantiation:");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + skillName + " is an abstract class and cannot be instantiated.");
		} catch (IllegalArgumentException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + skillName + " received an invalid arguement type during insantiation. Arguements must be of type (int, ItemTier).");
		} catch (ClassCastException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + skillName + " must implement SwordSkillProvider.");
		}
		
		throw new RuntimeException();
	}

	/**Checking if skill exists without creating instance */
	public static boolean itemExists(String name) {
		try {
			String sName = name.replaceAll(" ", "") + "Item";
			Class<?> classs = Class.forName("net.peacefulcraft.sco.items." + sName);
			if(classs == null) { 
                return false;
            }
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
    }
}
