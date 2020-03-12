package net.peacefulcraft.sco.swordskills.utilities;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
import net.peacefulcraft.sco.swordskills.SkillProvider;

/**
 * Attempts to generate an SCO SkillProvider item
 */
public class Generator {
	public static ItemStack generateItem(String itemName, int skillLevel, ItemTier teir){
		try {
			// Instantiate the provider
			itemName = itemName.replaceAll(" ", "") + "Item";
			Class<?> classs = Class.forName("net.peacefulcraft.sco.items." + itemName);
			Constructor<?> constructor = classs.getConstructor(int.class, ItemTier.class);
			Object[] args = { skillLevel, teir };
			
			return ((SkillProvider) constructor.newInstance(args)).getItem();
			
		} catch (ClassNotFoundException e) {
			SwordCraftOnline.logSevere("Attempted to create item " + itemName + ", but no coresponding class was found in net.peacefulcraft.sco.items");
		} catch (NoSuchMethodException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + itemName + " must have a constuctor with arguments (int, ItemTier)");
		} catch (SecurityException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + itemName + " does not have a public constructor.");
		} catch (InstantiationException | InvocationTargetException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + itemName + " generated exception during reflective instantiation:");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + itemName + " is an abstract class and cannot be instantiated.");
		} catch (IllegalArgumentException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + itemName + " received an invalid arguement type during insantiation. Arguements must be of type (int, ItemTier).");
		} catch (ClassCastException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + itemName + " must implement SwordSkillProvider.");
		}
		
		// Backup item
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("Unable to create item " + itemName + ", level " + skillLevel + " teir " + teir + ". Check console for details.");
		ItemStack item = new ItemStack(Material.FIRE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("The server");
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		return item;
	}

	/**
	 * Takes complete skill string, parses, and applies skill.
	 * Complete String example: CriticalStrike-1-COMMON
	 * @param s Complete skill string
	 */
	public static void readSkill(String s, ActiveMob am) {
		//Splitting string into: 0 Skill, 1 level, 2 ItemTier
		String[] split = s.split("-");
		String skill = split[0].replaceAll(" ", "") + "Item";
		try {
			Class<?> clazz = Class.forName("net.peacefulcraft.sco.items." + skill);
			Constructor<?> constructor = clazz.getConstructor(int.class, ItemTier.class);
			Object[] args = { Integer.valueOf(split[1]), ItemTier.valueOf(split[2]) };

			((SkillProvider) constructor.newInstance(args)).registerSkill(am);

		} catch (ClassNotFoundException e) {
			SwordCraftOnline.logSevere("Attempted to register skill " + skill + " to mob " + am.getDisplayName());
		} catch (NoSuchMethodException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + skill + " must have a constuctor with arguments (int, ItemTier)");
		} catch (SecurityException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + skill + " does not have a public constructor.");
		} catch (InstantiationException | InvocationTargetException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + skill + " generated exception during reflective instantiation:");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + skill + " is an abstract class and cannot be instantiated.");
		} catch (IllegalArgumentException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + skill + " received an invalid arguement type during insantiation. Arguements must be of type (int, ItemTier).");
		} catch (ClassCastException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + skill + " must implement SwordSkillProvider.");
		}
	}
}
