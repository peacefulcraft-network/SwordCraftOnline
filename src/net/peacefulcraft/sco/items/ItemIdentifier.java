package net.peacefulcraft.sco.items;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.peacefulcraft.sco.SwordCraftOnline;

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
        return generate(name, 1);
    }

    public static ItemStack generate(String name, int amount) {
        try {
            name = name.replaceAll(" ", "");
            Class<?> clazz = Class.forName("net.peacefulcraft.sco.items.customitems." + name);
            Method method = clazz.getMethod("create", int.class);
            Object[] args = { amount };

            return (ItemStack) method.invoke(clazz, args);
        } catch (ClassNotFoundException e) {
            SwordCraftOnline.logSevere("Attempted to create item " + name + ", but no corresponding class was found in net.peacefulcraft.sco.items.customitems");
        } catch (NoSuchMethodException e) {
            SwordCraftOnline.logSevere("net.peacefulcraft.sco.items.customitems." + name + " must have create method.");
        } catch (IllegalAccessException e) {
            SwordCraftOnline.logSevere("net.peacefulcraft.sco.items.customitems" + name + " is an abstract class and cannot be instantiated.");
        } catch (InvocationTargetException e) {

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