package net.peacefulcraft.sco.items;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.items.customitems.GoldCoin;

public class ItemIdentifier {
    /**Checking if skill exists without creating instance */
	public static boolean itemExists(String name) {
		try {
			String sName = name.replaceAll(" ", "");
			Class<?> classs = Class.forName("net.peacefulcraft.sco.items.customitems." + sName);
			if(classs == null) { 
                return false;
            }
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
    }

    public static ItemStack generate(String name) {
        return generate(name, 1, false);
    }

    public static ItemStack generate(String name, Integer amount, Boolean shop) {
        try {
            name = name.replaceAll(" ", "");
            Class<?> clazz = Class.forName("net.peacefulcraft.sco.items.customitems." + name);
            Method method = clazz.getMethod("create", Integer.class, Boolean.class);
            
            return (ItemStack) method.invoke(clazz.cast(clazz.newInstance()), amount, shop);
        } catch (ClassNotFoundException e) {
            SwordCraftOnline.logSevere("Attempted to create item " + name + ", but no corresponding class was found in net.peacefulcraft.sco.items.customitems");
        } catch (NoSuchMethodException e) {
            SwordCraftOnline.logSevere("net.peacefulcraft.sco.items.customitems." + name + " must have create method.");
        } catch (IllegalAccessException e) {
            SwordCraftOnline.logSevere("net.peacefulcraft.sco.items.customitems" + name + " is an abstract class and cannot be instantiated.");
        } catch (InstantiationException | InvocationTargetException e) {
            SwordCraftOnline.logSevere("net.peacefulcraft.sco.items.customitems." + name + " generated exception during reflective instantiation:");
        } catch (IllegalArgumentException e) {
            SwordCraftOnline.logSevere("net.peacefulcraft.sco.items.customitems." + name + " received an invalid argument type during insantiation. Arguements must be of type (Integer, Boolean).");
        } catch (ClassCastException e) {
            SwordCraftOnline.logSevere("net.peacefulcraft.sco.items.customitems." + name + " must implement ICustomItem.");
        }

        ItemStack item = new ItemStack(Material.FIRE, 1);
        ArrayList<String> desc = new ArrayList<String>();
        desc.add("Unable to create item " + name);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(desc);
        meta.setDisplayName("The server");
        item.setItemMeta(meta);

        return item;
    }
}